/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Lake Formation Tag Sync custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class LakeFormationTagSyncCfg(
    @JsonProperty("import_type") val importType: String = "CLOUD",
    @JsonProperty("cloud_source") val cloudSource: String? = null,
    @JsonProperty("remove_schema") val removeSchema: Boolean = false,
    @JsonProperty("config_type") val configType: String? = null,
    @JsonProperty("fail_on_errors") val failOnErrors: Boolean = true,
    @JsonProperty("batch_size") val batchSize: Number = 20,
) : CustomConfig<LakeFormationTagSyncCfg>()
