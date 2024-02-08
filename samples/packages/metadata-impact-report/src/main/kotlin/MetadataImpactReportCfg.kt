/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Metadata Impact Report custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class MetadataImpactReportCfg(
    @JsonProperty("include_glossary") val includeGlossary: String? = null,
    @JsonProperty("glossary_name") val glossaryName: String? = null,
    @JsonProperty("include_details") val includeDetails: Boolean? = null,
    @JsonProperty("email_addresses") val emailAddresses: String? = null,
) : CustomConfig()
