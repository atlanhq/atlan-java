/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Adoption Export custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class AdoptionExportCfg(
    @JsonProperty("include_searches") val includeSearches: Boolean?,
    @JsonProperty("maximum_searches") val maximumSearches: Number?,
    @JsonProperty("include_changes") val includeChanges: Boolean?,
    @JsonProperty("include_views") val includeViews: String?,
    @JsonProperty("maximum_assets") val maximumAssets: Number?,
    @JsonProperty("email_addresses") val emailAddresses: String?,
) : CustomConfig()
