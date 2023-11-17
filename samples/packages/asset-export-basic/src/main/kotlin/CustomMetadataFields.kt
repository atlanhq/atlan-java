/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.model.fields.CustomMetadataField

/**
 * Singleton for capturing all the custom metadata fields that exist in the tenant.
 */
object CustomMetadataFields {

    val all: List<CustomMetadataField>
    init {
        all = loadCustomMetadataFields()
    }

    /**
     * Retrieve all custom metadata fields for attributes that exist in the tenant.
     *
     * @return a list of all custom metadata fields defined in the tenant
     */
    private fun loadCustomMetadataFields(): List<CustomMetadataField> {
        val customMetadataDefs = Atlan.getDefaultClient().customMetadataCache
            .getAllCustomAttributes(false, true)
        val fields = mutableListOf<CustomMetadataField>()
        for ((setName, attributes) in customMetadataDefs) {
            for (attribute in attributes) {
                fields.add(CustomMetadataField(Atlan.getDefaultClient(), setName, attribute.displayName))
            }
        }
        return fields
    }
}
