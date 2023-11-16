/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.atlan.pkg.serde.WidgetSerde
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Asset Export (Basic) custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class AssetExportBasicCfg(
    @JsonProperty("export_scope") val exportScope: String?,
    @JsonProperty("qn_prefix") val qnPrefix: String?,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonProperty("include_glossaries") val includeGlossaries: List<String>?,
) : CustomConfig()
