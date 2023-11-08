/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package xformers.cell

import com.atlan.model.core.AtlanTag
import com.atlan.model.core.AtlanTag.AtlanTagBuilder

object AtlanTagXformer {
    private const val SETTINGS_DELIMITER = ">>"
    private const val PROPAGATED_DELIMITER = "<<"

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
        return if (direct) {
            return when (val propagation = encodePropagation(atlanTag)) {
                PropagationType.FULL -> "${atlanTag.typeName}$SETTINGS_DELIMITER${propagation.name}"
                PropagationType.HIERARCHY_ONLY -> "${atlanTag.typeName}$SETTINGS_DELIMITER${propagation.name}"
                else -> atlanTag.typeName
            }
        } else {
            "${atlanTag.typeName}$PROPAGATED_DELIMITER${PropagationType.PROPAGATED.name}"
        }
    }

    fun decode(atlanTag: String): AtlanTag? {
        return if (!atlanTag.endsWith("$PROPAGATED_DELIMITER${PropagationType.PROPAGATED.name}")) {
            val tokens = atlanTag.split(SETTINGS_DELIMITER)
            val builder =
                AtlanTag.builder()
                    .typeName(tokens[0])
            decodePropagation(tokens, builder)
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
    ): AtlanTag {
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
        return builder.build()
    }
}
