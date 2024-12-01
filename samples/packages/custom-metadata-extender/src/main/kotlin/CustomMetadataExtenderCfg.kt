/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.atlan.pkg.serde.WidgetSerde
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Custom Metadata Extender custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class CustomMetadataExtenderCfg(
    @JsonProperty("custom_metadata") val customMetadata: String = "",
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("connection_qualified_name") val connectionQualifiedName: List<String> = listOf(),
    @JsonProperty("glossaries") val glossaries: String? = null,
    @JsonProperty("domains") val domains: String = "ALL",
    @JsonProperty("domains_specific") val domainsSpecific: String = "",
) : CustomConfig()
