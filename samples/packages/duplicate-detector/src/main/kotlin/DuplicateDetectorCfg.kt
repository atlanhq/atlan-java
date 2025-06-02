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
 * Expected configuration for the Duplicate Detector custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class DuplicateDetectorCfg(
    @JsonProperty("glossary_name") val glossaryName: String = "Duplicate assets",
    @JsonProperty("qn_prefix") val qnPrefix: String = "default",
    @JsonProperty("control_config_strategy") val controlConfigStrategy: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("asset_types") val assetTypes: List<String> = listOf("Table", "View", "MaterialisedView"),
) : CustomConfig<DuplicateDetectorCfg>()
