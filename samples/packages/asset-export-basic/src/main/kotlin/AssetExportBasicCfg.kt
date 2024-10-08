/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Asset Export (Basic) custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class AssetExportBasicCfg(
    @JsonProperty("export_scope") val exportScope: String? = null,
    @JsonProperty("qn_prefix") val qnPrefix: String? = null,
    @JsonProperty("include_description") val includeDescription: Boolean? = null,
    @JsonProperty("include_glossaries") val includeGlossaries: Boolean? = null,
    @JsonProperty("include_products") val includeProducts: Boolean? = null,
    @JsonProperty("include_archived") val includeArchived: Boolean? = null,
    @JsonProperty("asset_types_to_include") val assetTypesToInclude: String? = null,
    @JsonProperty("attributes_to_include") val attributesToInclude: String? = null,
    @JsonProperty("qn_prefixes") val qnPrefixes: String? = null,
    @JsonProperty("all_attributes") val allAttributes: Boolean? = null,
    @JsonProperty("delivery_type") val deliveryType: String? = null,
    @JsonProperty("email_addresses") val emailAddresses: String? = null,
    @JsonProperty("target_prefix") val targetPrefix: String? = null,
    @JsonProperty("cloud_target") val cloudTarget: String? = null,
) : CustomConfig()
