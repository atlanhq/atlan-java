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
 * Expected configuration for the Asset Import custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class AssetImportCfg(
    @JsonProperty("import_type") val importType: String? = null,
    @JsonProperty("cloud_source") val cloudSource: String? = null,
    @JsonProperty("assets_file") val assetsFile: String? = null,
    @JsonProperty("assets_prefix") val assetsPrefix: String? = null,
    @JsonProperty("assets_key") val assetsKey: String? = null,
    @JsonProperty("assets_upsert_semantic") val assetsUpsertSemantic: String? = null,
    @JsonProperty("assets_config") val assetsConfig: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("assets_attr_to_overwrite") val assetsAttrToOverwrite: List<String>? = null,
    @JsonProperty("assets_fail_on_errors") val assetsFailOnErrors: Boolean? = null,
    @JsonProperty("assets_case_sensitive") val assetsCaseSensitive: Boolean? = null,
    @JsonProperty("assets_table_view_agnostic") val assetsTableViewAgnostic: Boolean? = null,
    @JsonProperty("assets_field_separator") val assetsFieldSeparator: String? = null,
    @JsonProperty("assets_batch_size") val assetsBatchSize: Number? = null,
    @JsonProperty("track_batches") val trackBatches: Boolean? = null,
    @JsonProperty("glossaries_file") val glossariesFile: String? = null,
    @JsonProperty("glossaries_prefix") val glossariesPrefix: String? = null,
    @JsonProperty("glossaries_key") val glossariesKey: String? = null,
    @JsonProperty("glossaries_upsert_semantic") val glossariesUpsertSemantic: String? = null,
    @JsonProperty("glossaries_config") val glossariesConfig: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("glossaries_attr_to_overwrite") val glossariesAttrToOverwrite: List<String>? = null,
    @JsonProperty("glossaries_fail_on_errors") val glossariesFailOnErrors: Boolean? = null,
    @JsonProperty("glossaries_field_separator") val glossariesFieldSeparator: String? = null,
    @JsonProperty("glossaries_batch_size") val glossariesBatchSize: Number? = null,
    @JsonProperty("data_products_file") val dataProductsFile: String? = null,
    @JsonProperty("data_products_prefix") val dataProductsPrefix: String? = null,
    @JsonProperty("data_products_key") val dataProductsKey: String? = null,
    @JsonProperty("data_products_upsert_semantic") val dataProductsUpsertSemantic: String? = null,
    @JsonProperty("data_products_config") val dataProductsConfig: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("data_products_attr_to_overwrite") val dataProductsAttrToOverwrite: List<String>? = null,
    @JsonProperty("data_products_fail_on_errors") val dataProductsFailOnErrors: Boolean? = null,
    @JsonProperty("data_products_field_separator") val dataProductsFieldSeparator: String? = null,
    @JsonProperty("data_products_batch_size") val dataProductsBatchSize: Number? = null,
) : CustomConfig()