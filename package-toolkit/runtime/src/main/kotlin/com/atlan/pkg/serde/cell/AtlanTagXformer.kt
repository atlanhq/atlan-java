/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.AtlanClient
import com.atlan.cache.SourceTagCache.SourceTagName
import com.atlan.exception.NotFoundException
import com.atlan.model.assets.Connection
import com.atlan.model.assets.ITag
import com.atlan.model.core.AtlanTag
import com.atlan.model.core.AtlanTag.AtlanTagBuilder
import com.atlan.model.relations.Reference.SaveSemantic
import com.atlan.model.structs.SourceTagAttachment
import com.atlan.model.structs.SourceTagAttachmentValue
import com.atlan.pkg.serde.FieldSerde.FAIL_ON_ERRORS
import mu.KLogger

object AtlanTagXformer {
    private const val SETTINGS_DELIMITER = ">>"
    private const val PROPAGATED_DELIMITER = "<<"
    private const val ATTACHMENT_DELIMITER = "##"
    private const val VALUE_SEPARATOR = "??"
    private const val VALUE_DELIMITER = "&&"

    enum class PropagationType {
        FULL,
        NONE,
        HIERARCHY_ONLY,
        LINEAGE_ONLY,
        PROPAGATED,
    }

    fun encode(
        client: AtlanClient,
        fromGuid: String,
        atlanTag: AtlanTag,
    ): String {
        val direct = fromGuid == atlanTag.entityGuid
        val attributes = encodeAttributes(client, atlanTag)
        return if (direct) {
            return when (val propagation = encodePropagation(atlanTag)) {
                PropagationType.FULL -> "${atlanTag.typeName}$attributes$SETTINGS_DELIMITER${propagation.name}"
                PropagationType.HIERARCHY_ONLY -> "${atlanTag.typeName}$attributes$SETTINGS_DELIMITER${propagation.name}"
                PropagationType.LINEAGE_ONLY -> "${atlanTag.typeName}$attributes$SETTINGS_DELIMITER${propagation.name}"
                else -> "${atlanTag.typeName}$attributes"
            }
        } else {
            "${atlanTag.typeName}$attributes$PROPAGATED_DELIMITER${PropagationType.PROPAGATED.name}"
        }
    }

    fun decode(
        client: AtlanClient,
        atlanTag: String,
        logger: KLogger,
    ): AtlanTag? =
        if (!atlanTag.endsWith("$PROPAGATED_DELIMITER${PropagationType.PROPAGATED.name}")) {
            val tokens = atlanTag.split(SETTINGS_DELIMITER)
            val (frontMatter, tagSemantic) =
                if (tokens[0].startsWith(AssetRefXformer.APPEND_PREFIX)) {
                    Pair(tokens[0].removePrefix(AssetRefXformer.APPEND_PREFIX), SaveSemantic.APPEND)
                } else if (tokens[0].endsWith(AssetRefXformer.REMOVE_PREFIX)) {
                    Pair(tokens[0].removePrefix(AssetRefXformer.REMOVE_PREFIX), SaveSemantic.REMOVE)
                } else {
                    Pair(tokens[0], SaveSemantic.REPLACE)
                }
            val builder = AtlanTag.builder()
            val typeName = decodeAttributes(client, frontMatter, builder)
            try {
                client.atlanTagCache.getIdForName(typeName)
                builder.typeName(typeName)
                decodePropagation(tokens, builder)
                builder.semantic(tagSemantic)
                builder.build()
            } catch (e: NotFoundException) {
                if (FAIL_ON_ERRORS.get()) {
                    throw e
                } else {
                    logger.warn { "Unable to find specified tag, will skip associating it: $typeName" }
                    logger.debug(e) { "Full stack trace:" }
                }
                null
            }
        } else {
            null
        }

    private fun encodePropagation(atlanTag: AtlanTag): PropagationType {
        return if (atlanTag.propagate) {
            return when {
                !atlanTag.restrictPropagationThroughLineage && !atlanTag.restrictPropagationThroughHierarchy -> PropagationType.FULL
                atlanTag.restrictPropagationThroughLineage && !atlanTag.restrictPropagationThroughHierarchy -> PropagationType.HIERARCHY_ONLY
                !atlanTag.restrictPropagationThroughLineage && atlanTag.restrictPropagationThroughHierarchy -> PropagationType.LINEAGE_ONLY
                else -> PropagationType.NONE
            }
        } else {
            PropagationType.NONE
        }
    }

    private fun decodePropagation(
        atlanTagTokens: List<String>,
        builder: AtlanTagBuilder<*, *>,
    ) {
        if (atlanTagTokens.size > 1) {
            when (atlanTagTokens[1].uppercase()) {
                PropagationType.FULL.name ->
                    builder
                        .propagate(
                            true,
                        ).removePropagationsOnEntityDelete(true)
                        .restrictPropagationThroughLineage(false)
                        .restrictPropagationThroughHierarchy(false)
                PropagationType.HIERARCHY_ONLY.name ->
                    builder
                        .propagate(
                            true,
                        ).removePropagationsOnEntityDelete(true)
                        .restrictPropagationThroughLineage(true)
                        .restrictPropagationThroughHierarchy(false)
                PropagationType.LINEAGE_ONLY.name ->
                    builder
                        .propagate(
                            true,
                        ).removePropagationsOnEntityDelete(true)
                        .restrictPropagationThroughLineage(false)
                        .restrictPropagationThroughHierarchy(true)
                else -> builder.propagate(false)
            }
        } else {
            // If there is no propagation option specified, turn off propagation
            builder.propagate(false)
        }
    }

    private fun encodeAttributes(
        client: AtlanClient,
        atlanTag: AtlanTag,
    ): String =
        if (atlanTag.sourceTagAttachments.isNullOrEmpty()) {
            ""
        } else {
            val attachments = mutableListOf<String>()
            atlanTag.sourceTagAttachments.forEach { sta ->
                val sourceTagIdentity = encodeSourceTagIdentity(client, sta.sourceTagQualifiedName)
                if (sourceTagIdentity.isNotBlank()) {
                    val values = mutableListOf<String>()
                    sta.sourceTagValues.forEach { v ->
                        values.add(
                            if (v.tagAttachmentKey.isNullOrBlank()) "=${v.tagAttachmentValue}" else "${v.tagAttachmentKey}=${v.tagAttachmentValue}",
                        )
                    }
                    if (values.isNotEmpty()) {
                        attachments.add(
                            "$sourceTagIdentity$VALUE_SEPARATOR${values.joinToString(VALUE_DELIMITER)}",
                        )
                    } else {
                        attachments.add(sourceTagIdentity)
                    }
                }
            }
            if (attachments.isNotEmpty()) " {{${attachments.joinToString(ATTACHMENT_DELIMITER)}}}" else ""
        }

    private fun decodeAttributes(
        client: AtlanClient,
        atlanTag: String,
        builder: AtlanTagBuilder<*, *>,
    ): String =
        if (atlanTag.contains(" {{") && atlanTag.contains("}}")) {
            val simpleTagName = atlanTag.substringBefore(" {{")
            val encodedAttrs = atlanTag.substring(atlanTag.indexOf(" {{") + 3, atlanTag.indexOf("}}"))
            val attachments = encodedAttrs.split(ATTACHMENT_DELIMITER)
            attachments.forEach { a ->
                val sta = SourceTagAttachment.builder()
                val valueSplit = a.split(VALUE_SEPARATOR)
                val sourceTagIdentity = valueSplit[0]
                val sourceTag = decodeSourceTag(client, sourceTagIdentity)
                if (sourceTag != null) {
                    sta.sourceTagQualifiedName(sourceTag.qualifiedName)
                    sta.sourceTagName(sourceTag.name)
                    sta.sourceTagGuid(sourceTag.guid)
                    sta.sourceTagConnectorName(Connection.getConnectorTypeFromQualifiedName(sourceTag.qualifiedName).value)
                    if (valueSplit.size > 1) {
                        val valueDetails = valueSplit[1]
                        val values = valueDetails.split(VALUE_DELIMITER)
                        values.forEach { v ->
                            val stv = SourceTagAttachmentValue.builder()
                            val kv = v.split("=")
                            if (kv.size == 2) {
                                if (kv[0].isNotBlank()) {
                                    stv.tagAttachmentKey(kv[0])
                                }
                                stv.tagAttachmentValue(kv[1])
                            }
                            sta.sourceTagValue(stv.build())
                        }
                    }
                    builder.sourceTagAttachment(sta.build())
                }
            }
            simpleTagName
        } else {
            atlanTag
        }

    private fun encodeSourceTagIdentity(
        client: AtlanClient,
        sourceTagQN: String,
    ): String {
        return try {
            val tag = client.sourceTagCache.getByQualifiedName(sourceTagQN) as ITag
            return SourceTagName(client, tag).toString()
        } catch (e: NotFoundException) {
            ""
        }
    }

    private fun decodeSourceTag(
        client: AtlanClient,
        sourceTagIdentity: String,
    ): ITag? {
        val sourceTagId = SourceTagName(sourceTagIdentity)
        return try {
            client.sourceTagCache.getByName(sourceTagId) as ITag
        } catch (e: NotFoundException) {
            null
        }
    }
}
