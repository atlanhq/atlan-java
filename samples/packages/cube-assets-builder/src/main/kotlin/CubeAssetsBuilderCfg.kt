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
 * Expected configuration for the Cube Assets Builder custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class CubeAssetsBuilderCfg(
    @JsonProperty("assets_import_type") val assetsImportType: String = "DIRECT",
    @JsonProperty("assets_file") val assetsFile: String = "",
    @JsonProperty("assets_prefix") val assetsPrefix: String = "",
    @JsonProperty("assets_key") val assetsKey: String = "",
    @JsonProperty("cloud_source") val cloudSource: String? = null,
    @JsonProperty("assets_upsert_semantic") val assetsUpsertSemantic: String = "upsert",
    @JsonProperty("delta_semantic") val deltaSemantic: String = "full",
    @JsonProperty("delta_removal_type") val deltaRemovalType: String = "archive",
    @JsonProperty("delta_reload_calculation") val deltaReloadCalculation: String = "all",
    @JsonProperty("previous_file_direct") val previousFileDirect: String = "",
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("assets_attr_to_overwrite") val assetsAttrToOverwrite: List<String> = listOf(),
    @JsonProperty("assets_fail_on_errors") val assetsFailOnErrors: Boolean = true,
    @JsonProperty("assets_field_separator") val assetsFieldSeparator: String = ",",
    @JsonProperty("assets_batch_size") val assetsBatchSize: Number = 20,
    @JsonProperty("assets_cm_handling") val assetsCmHandling: String? = null,
    @JsonProperty("assets_tag_handling") val assetsTagHandling: String? = null,
    @JsonProperty("track_batches") val trackBatches: Boolean = true,
) : CustomConfig<CubeAssetsBuilderCfg>()
