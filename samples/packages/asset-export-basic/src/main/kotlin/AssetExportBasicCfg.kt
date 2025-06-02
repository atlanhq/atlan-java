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
    @JsonProperty("export_scope") val exportScope: String = "ENRICHED_ONLY",
    @JsonProperty("qn_prefix") val qnPrefix: String = "default",
    @JsonProperty("include_description") val includeDescription: Boolean = true,
    @JsonProperty("include_glossaries") val includeGlossaries: Boolean = false,
    @JsonProperty("include_products") val includeProducts: Boolean = false,
    @JsonProperty("include_archived") val includeArchived: Boolean = false,
    @JsonProperty("asset_types_to_include") val assetTypesToInclude: String = "",
    @JsonProperty("attributes_to_include") val attributesToInclude: String = "",
    @JsonProperty("qn_prefixes") val qnPrefixes: String = "",
    @JsonProperty("all_attributes") val allAttributes: Boolean = false,
    @JsonProperty("delivery_type") val deliveryType: String = "DIRECT",
    @JsonProperty("email_addresses") val emailAddresses: String? = null,
    @JsonProperty("target_prefix") val targetPrefix: String? = null,
    @JsonProperty("cloud_target") val cloudTarget: String? = null,
) : CustomConfig<AssetExportBasicCfg>()
