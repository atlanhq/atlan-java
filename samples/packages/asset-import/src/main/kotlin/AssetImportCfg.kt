/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.atlan.pkg.serde.WidgetSerde
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Asset Import custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class AssetImportCfg(
    @JsonProperty("assets_file") val assetsFile: String?,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonProperty("assets_attr_to_overwrite") val assetsAttrToOverwrite: List<String>?,
    @JsonProperty("assets_upsert_semantic") val assetsUpsertSemantic: String?,
    @JsonProperty("glossaries_file") val glossariesFile: String?,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonProperty("glossaries_attr_to_overwrite") val glossariesAttrToOverwrite: List<String>?,
    @JsonProperty("glossaries_upsert_semantic") val glossariesUpsertSemantic: String?,
) : CustomConfig()
