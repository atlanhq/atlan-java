/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
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
    @JsonProperty("assets_import_type") val assetsImportType: String? = null,
    @JsonProperty("assets_file") val assetsFile: String? = null,
    @JsonProperty("assets_s3_region") val assetsS3Region: String? = null,
    @JsonProperty("assets_s3_bucket") val assetsS3Bucket: String? = null,
    @JsonProperty("assets_s3_object_key") val assetsS3ObjectKey: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("assets_attr_to_overwrite") val assetsAttrToOverwrite: List<String>? = null,
    @JsonProperty("assets_upsert_semantic") val assetsUpsertSemantic: String? = null,
    @JsonProperty("assets_fail_on_errors") val assetsFailOnErrors: Boolean? = null,
    @JsonProperty("assets_case_sensitive") val assetsCaseSensitive: Boolean? = null,
    @JsonProperty("glossaries_import_type") val glossariesImportType: String? = null,
    @JsonProperty("glossaries_file") val glossariesFile: String? = null,
    @JsonProperty("glossaries_s3_region") val glossariesS3Region: String? = null,
    @JsonProperty("glossaries_s3_bucket") val glossariesS3Bucket: String? = null,
    @JsonProperty("glossaries_s3_object_key") val glossariesS3ObjectKey: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("glossaries_attr_to_overwrite") val glossariesAttrToOverwrite: List<String>? = null,
    @JsonProperty("glossaries_upsert_semantic") val glossariesUpsertSemantic: String? = null,
    @JsonProperty("glossaries_fail_on_errors") val glossariesFailOnErrors: Boolean? = null,
) : CustomConfig()
