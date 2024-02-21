/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.Atlan
import com.atlan.cache.AbstractLazyCache
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.ITag
import com.atlan.model.core.AtlanTag
import com.atlan.model.core.AtlanTag.AtlanTagBuilder
import com.atlan.model.structs.SourceTagAttachment
import com.atlan.model.structs.SourceTagAttachmentValue
import com.atlan.util.StringUtils
import java.util.concurrent.ConcurrentHashMap

object AtlanTagXformer {
    private const val SETTINGS_DELIMITER = ">>"
    private const val PROPAGATED_DELIMITER = "<<"
    private const val ATTACHMENT_DELIMITER = "##"
    private const val CONNECTION_DELIMITER = "@@"
    private const val VALUE_SEPARATOR = "??"
    private const val VALUE_DELIMITER = "&&"

    private val localTagCache = LocalTagCache()
    private val localConnectionCache = LocalConnectionCache()

    enum class PropagationType {
        FULL,
        NONE,
        HIERARCHY_ONLY,
        PROPAGATED,
    }

    fun encode(
        fromGuid: String,
        atlanTag: AtlanTag,
    ): String {
        val direct = fromGuid == atlanTag.entityGuid
        val attributes = encodeAttributes(atlanTag)
        return if (direct) {
            return when (val propagation = encodePropagation(atlanTag)) {
                PropagationType.FULL -> "${atlanTag.typeName}$attributes$SETTINGS_DELIMITER${propagation.name}"
                PropagationType.HIERARCHY_ONLY -> "${atlanTag.typeName}$attributes$SETTINGS_DELIMITER${propagation.name}"
                else -> "${atlanTag.typeName}$attributes"
            }
        } else {
            "${atlanTag.typeName}$attributes$PROPAGATED_DELIMITER${PropagationType.PROPAGATED.name}"
        }
    }

    fun decode(atlanTag: String): AtlanTag? {
        return if (!atlanTag.endsWith("$PROPAGATED_DELIMITER${PropagationType.PROPAGATED.name}")) {
            val tokens = atlanTag.split(SETTINGS_DELIMITER)
            val builder = AtlanTag.builder()
            val typeName = decodeAttributes(tokens[0], builder)
            builder.typeName(typeName)
            decodePropagation(tokens, builder)
            builder.build()
        } else {
            null
        }
    }

    private fun encodePropagation(atlanTag: AtlanTag): PropagationType {
        return if (atlanTag.propagate) {
            return when {
                atlanTag.removePropagationsOnEntityDelete && !atlanTag.restrictPropagationThroughLineage -> PropagationType.FULL
                atlanTag.removePropagationsOnEntityDelete && atlanTag.restrictPropagationThroughLineage -> PropagationType.HIERARCHY_ONLY
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
                    builder.propagate(
                        true,
                    ).removePropagationsOnEntityDelete(true).restrictPropagationThroughLineage(false)
                PropagationType.HIERARCHY_ONLY.name ->
                    builder.propagate(
                        true,
                    ).removePropagationsOnEntityDelete(true).restrictPropagationThroughLineage(true)
                else -> builder.propagate(false)
            }
        } else {
            // If there is no propagation option specified, turn off propagation
            builder.propagate(false)
        }
    }

    private fun encodeAttributes(atlanTag: AtlanTag): String {
        return if (atlanTag.sourceTagAttachments.isNullOrEmpty()) {
            ""
        } else {
            val attachments = mutableListOf<String>()
            atlanTag.sourceTagAttachments.forEach { sta ->
                val sourceTagIdentity = encodeSourceTagIdentity(sta.sourceTagQualifiedName)
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
            " {{${attachments.joinToString(ATTACHMENT_DELIMITER)}}}"
        }
    }

    private fun decodeAttributes(
        atlanTag: String,
        builder: AtlanTagBuilder<*, *>,
    ): String {
        return if (atlanTag.contains(" {{") && atlanTag.contains("}}")) {
            val simpleTagName = atlanTag.substringBefore(" {{")
            val encodedAttrs = atlanTag.substring(atlanTag.indexOf(" {{") + 3, atlanTag.indexOf("}}"))
            val attachments = encodedAttrs.split(ATTACHMENT_DELIMITER)
            attachments.forEach { a ->
                val sta = SourceTagAttachment.builder()
                val valueSplit = a.split(VALUE_SEPARATOR)
                val sourceTagIdentity = valueSplit[0]
                val sourceTag = decodeSourceTag(sourceTagIdentity)
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
    }

    private fun encodeSourceTagIdentity(sourceTagQN: String): String {
        return localTagCache.getNameForId(sourceTagQN)
    }

    private fun decodeSourceTag(sourceTagIdentity: String): ITag? {
        return localTagCache.getSourceTagByName(sourceTagIdentity)
    }

    /**
     * Local cache for source tags.
     * - ID will be the fully qualifiedName of the Tag asset
     * - Name will be connector/name@@partialQN
     */
    private class LocalTagCache : AbstractLazyCache() {
        private val idToTag = ConcurrentHashMap<String, ITag>()
        private val tagAttributes = listOf(Asset.NAME)
        override fun lookupById(sourceTagQN: String?) {
            if (sourceTagQN != null) {
                Atlan.getDefaultClient().assets.select()
                    .where(Asset.SUPER_TYPE_NAMES.eq(ITag.TYPE_NAME))
                    .where(Asset.QUALIFIED_NAME.eq(sourceTagQN))
                    .includesOnResults(tagAttributes)
                    .stream()
                    .findFirst()
                    .ifPresent { tag ->
                        tag as ITag
                        val connectionQN = StringUtils.getConnectionQualifiedName(sourceTagQN)
                        val connectionName = localConnectionCache.getNameForId(connectionQN)
                        val sourceTagPartial = sourceTagQN.substring(connectionQN.length + 1)
                        cache(sourceTagQN, "$connectionName$CONNECTION_DELIMITER$sourceTagPartial")
                        idToTag[sourceTagQN] = tag
                    }
            }
        }

        override fun lookupByName(name: String?) {
            if (name != null) {
                val tokens = name.split(CONNECTION_DELIMITER)
                if (tokens.size == 2) {
                    val connectionString = tokens[0]
                    val sourceTagPartialQN = tokens[1]
                    val connectionQN = localConnectionCache.getIdForName(connectionString)
                    val sourceTagQN = "$connectionQN/$sourceTagPartialQN"
                    Atlan.getDefaultClient().assets.select()
                        .where(Asset.SUPER_TYPE_NAMES.eq(ITag.TYPE_NAME))
                        .where(Asset.QUALIFIED_NAME.eq(sourceTagQN))
                        .includesOnResults(tagAttributes)
                        .stream()
                        .findFirst()
                        .ifPresent { tag ->
                            tag as ITag
                            cache(sourceTagQN, name)
                            idToTag[sourceTagQN] = tag
                        }
                }
            }
        }

        fun getSourceTagById(sourceTagQN: String): ITag? {
            getNameForId(sourceTagQN) // Make sure it's cached before trying to retrieve it
            return idToTag[sourceTagQN]
        }

        fun getSourceTagByName(name: String): ITag? {
            val id = getIdForName(name) // Make sure it's cached before trying to retrieve it
            return idToTag[id]
        }
    }

    /**
     * Local cache for connections.
     * - ID will be the fully qualifiedName of the Connection asset
     * - Name will be connector/name
     */
    private class LocalConnectionCache : AbstractLazyCache() {
        private val connectionAttributes = listOf(Connection.CONNECTOR_TYPE)
        override fun lookupById(connectionQN: String?) {
            if (connectionQN != null) {
                Connection.select()
                    .where(Connection.QUALIFIED_NAME.eq(connectionQN))
                    .includesOnResults(connectionAttributes)
                    .stream()
                    .findFirst()
                    .ifPresent { connection ->
                        connection as Connection
                        cache(connectionQN, "${connection.connectorType.value}/${connection.name}")
                    }
            }
        }

        override fun lookupByName(name: String?) {
            if (name != null) {
                val tokens = name.split("/")
                if (tokens.size > 1) {
                    val connectorType = tokens[0]
                    val connectorName = name.substring(connectorType.length + 1)
                    Connection.select()
                        .where(Connection.NAME.eq(connectorName))
                        .where(Connection.CONNECTOR_TYPE.eq(connectorType))
                        .includesOnResults(connectionAttributes)
                        .stream()
                        .findFirst()
                        .ifPresent { connection ->
                            connection as Connection
                            cache(connection.qualifiedName, name)
                        }
                }
            }
        }
    }
}
