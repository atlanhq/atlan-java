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
 * Expected configuration for the Asset Import custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class AssetImportCfg(
    @JsonProperty("import_type") val importType: String = "DIRECT",
    @JsonProperty("cloud_source") val cloudSource: String? = null,
    @JsonProperty("assets_file") val assetsFile: String = "",
    @JsonProperty("assets_prefix") val assetsPrefix: String = "",
    @JsonProperty("assets_key") val assetsKey: String = "",
    @JsonProperty("assets_upsert_semantic") val assetsUpsertSemantic: String = "update",
    @JsonProperty("assets_config") val assetsConfig: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("assets_attr_to_overwrite") val assetsAttrToOverwrite: List<String> = listOf(),
    @JsonProperty("assets_fail_on_errors") val assetsFailOnErrors: Boolean = true,
    @JsonProperty("assets_case_sensitive") val assetsCaseSensitive: Boolean = true,
    @JsonProperty("assets_table_view_agnostic") val assetsTableViewAgnostic: Boolean = false,
    @JsonProperty("assets_field_separator") val assetsFieldSeparator: String = ",",
    @JsonProperty("assets_batch_size") val assetsBatchSize: Number = 20,
    @JsonProperty("track_batches") val trackBatches: Boolean = true,
    @JsonProperty("glossaries_file") val glossariesFile: String = "",
    @JsonProperty("glossaries_prefix") val glossariesPrefix: String = "",
    @JsonProperty("glossaries_key") val glossariesKey: String = "",
    @JsonProperty("glossaries_upsert_semantic") val glossariesUpsertSemantic: String = "upsert",
    @JsonProperty("glossaries_config") val glossariesConfig: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("glossaries_attr_to_overwrite") val glossariesAttrToOverwrite: List<String> = listOf(),
    @JsonProperty("glossaries_fail_on_errors") val glossariesFailOnErrors: Boolean = true,
    @JsonProperty("glossaries_field_separator") val glossariesFieldSeparator: String = ",",
    @JsonProperty("glossaries_batch_size") val glossariesBatchSize: Number = 20,
    @JsonProperty("data_products_file") val dataProductsFile: String = "",
    @JsonProperty("data_products_prefix") val dataProductsPrefix: String = "",
    @JsonProperty("data_products_key") val dataProductsKey: String = "",
    @JsonProperty("data_products_upsert_semantic") val dataProductsUpsertSemantic: String = "upsert",
    @JsonProperty("data_products_config") val dataProductsConfig: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("data_products_attr_to_overwrite") val dataProductsAttrToOverwrite: List<String> = listOf(),
    @JsonProperty("data_products_fail_on_errors") val dataProductsFailOnErrors: Boolean = true,
    @JsonProperty("data_products_field_separator") val dataProductsFieldSeparator: String = ",",
    @JsonProperty("data_products_batch_size") val dataProductsBatchSize: Number = 20,
) : CustomConfig()
