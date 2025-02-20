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
 * Expected configuration for the Enrichment Migrator custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class EnrichmentMigratorCfg(
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("source_connection") val sourceConnection: List<String> = listOf(""),
    @JsonProperty("source_qn_prefix") val sourceQnPrefix: String = "",
    @JsonProperty("target_database_pattern") val targetDatabasePattern: String = "",
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("target_connection") val targetConnection: List<String> = listOf(""),
    @JsonProperty("include_archived") val includeArchived: Boolean = false,
    @JsonProperty("config_type") val configType: String? = null,
    @JsonProperty("fail_on_errors") val failOnErrors: Boolean = true,
    @JsonProperty("case_sensitive") val caseSensitive: Boolean = true,
    @JsonProperty("table_view_agnostic") val tableViewAgnostic: Boolean = false,
    @JsonProperty("field_separator") val fieldSeparator: String = ",",
    @JsonProperty("batch_size") val batchSize: Number = 20,
    @JsonProperty("cm_handling") val cmHandling: String? = null,
    @JsonProperty("tag_handling") val tagHandling: String? = null,
    @JsonProperty("limit_type") val limitType: String = "EXCLUDE",
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("attributes_list") val attributesList: List<String> = listOf(""),
    @JsonProperty("cm_limit_type") val cmLimitType: String = "EXCLUDE",
    @JsonProperty("custom_metadata") val customMetadata: String = "",
) : CustomConfig()
