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
 * Expected configuration for the Relational Assets Builder custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class RelationalAssetsBuilderCfg(
    @JsonProperty("assets_import_type") val assetsImportType: String? = null,
    @JsonProperty("assets_file") val assetsFile: String? = null,
    @JsonProperty("assets_s3_region") val assetsS3Region: String? = null,
    @JsonProperty("assets_s3_bucket") val assetsS3Bucket: String? = null,
    @JsonProperty("assets_s3_object_key") val assetsS3ObjectKey: String? = null,
    @JsonProperty("assets_upsert_semantic") val assetsUpsertSemantic: String? = null,
    @JsonProperty("assets_config_type") val assetsConfigType: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("assets_attr_to_overwrite") val assetsAttrToOverwrite: List<String>? = null,
    @JsonProperty("assets_fail_on_errors") val assetsFailOnErrors: Boolean? = null,
    @JsonProperty("assets_field_separator") val assetsFieldSeparator: String? = null,
    @JsonProperty("assets_batch_size") val assetsBatchSize: Number? = null,
    @JsonProperty("track_batches") val trackBatches: Boolean? = null,
) : CustomConfig()
