/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import com.atlan.model.assets.Asset
import com.atlan.model.relations.Reference
import com.atlan.model.relations.UserDefRelationship
import com.atlan.pkg.PackageContext

/**
 * Static object to transform user-defined relationship references.
 */
object UserDefRelationshipXformer : AbstractRelationshipAttributesXformer() {
    val USER_DEF_RELN_FIELDS =
        setOf(
            Asset.USER_DEF_RELATIONSHIP_TOS.atlanFieldName,
            "userDefRelationshipTos",
            Asset.USER_DEF_RELATIONSHIP_FROMS.atlanFieldName,
            "userDefRelationshipFroms",
        )

    /**
     * Decodes (deserializes) a string form into a user-defined relationship reference object.
     *
     * @param ctx context in which the package is running
     * @param baseRef the base asset reference to be enhanced with the relationship-level attributes
     * @param semantic to use for persisting relationship
     * @param properties the relationship-level attribute to enhance the base asset reference with
     * @return the user-defined relationship reference represented by the string
     */
    fun decode(
        ctx: PackageContext<*>,
        baseRef: Asset,
        semantic: Reference.SaveSemantic,
        properties: Map<String, String>,
    ): Asset {
        val toLabel = properties.getOrDefault("toTypeLabel", "")
        val fromLabel = properties.getOrDefault("fromTypeLabel", "")
        return UserDefRelationship
            .builder()
            .toTypeLabel(toLabel)
            .fromTypeLabel(fromLabel)
            .userDefRelationshipTo(baseRef, semantic) as Asset
    }
}
