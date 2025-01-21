/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.pkg.CustomConfig
import com.atlan.pkg.model.ConnectorAndConnections
import com.atlan.pkg.serde.WidgetSerde
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import javax.annotation.processing.Generated;

/**
 * Expected configuration for the Metadata Impact Report custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class MetadataImpactReportCfg(
    @JsonProperty("include_glossary") val includeGlossary: String = "TRUE",
    @JsonProperty("glossary_name") val glossaryName: String = "Metadata metrics",
    @JsonProperty("include_details") val includeDetails: Boolean = false,
    @JsonProperty("include_data_products") val includeDataProducts: String = "TRUE",
    @JsonProperty("data_domain") val dataDomain: String = "Metadata metrics",
    @JsonProperty("file_format") val fileFormat: String = "XLSX",
    @JsonProperty("delivery_type") val deliveryType: String = "DIRECT",
    @JsonProperty("email_addresses") val emailAddresses: String? = null,
    @JsonProperty("target_prefix") val targetPrefix: String? = null,
    @JsonProperty("target_key") val targetKey: String? = null,
    @JsonProperty("cloud_target") val cloudTarget: String? = null,
) : CustomConfig()