/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.pkg.CustomConfig
import com.atlan.pkg.model.ConnectorAndConnections
import com.atlan.pkg.serde.WidgetSerde
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Enrichment Migrator custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class EnrichmentMigratorCfg(
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("source_connection") val sourceConnection: List<String>? = null,
    @JsonProperty("source_qn_prefix") val sourceQnPrefix: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("target_connection") val targetConnection: List<String>? = null,
    @JsonProperty("config_type") val configType: String? = null,
    @JsonProperty("fail_on_errors") val failOnErrors: Boolean? = null,
    @JsonProperty("field_separator") val fieldSeparator: String? = null,
    @JsonProperty("batch_size") val batchSize: Number? = null,
    @JsonProperty("limit_type") val limitType: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("attributes_list") val attributesList: List<String>? = null,
    @JsonProperty("cm_limit_type") val cmLimitType: String? = null,
    @JsonProperty("custom_metadata") val customMetadata: String? = null,
) : CustomConfig()
