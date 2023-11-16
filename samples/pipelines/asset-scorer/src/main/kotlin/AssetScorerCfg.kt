/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.atlan.pkg.serde.WidgetSerde
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Asset Scorer custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class AssetScorerCfg(
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonProperty("asset_types") val assetTypes: List<String>?,
    @JsonProperty("credential_usage") val credentialUsage: String?,
    @JsonProperty("api_token") val apiToken: String?,
) : CustomConfig()
