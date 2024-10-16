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
 * Expected configuration for the Lake Formation Tag Sync custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class LakeFormationTagSyncCfg(
    @JsonProperty("import_type") val importType: String? = null,
    @JsonProperty("cloud_source") val cloudSource: String? = null,
    @JsonProperty("remove_schema") val removeSchema: Boolean? = null,
    @JsonProperty("config_type") val configType: String? = null,
    @JsonProperty("fail_on_errors") val failOnErrors: Boolean? = null,
    @JsonProperty("batch_size") val batchSize: Number? = null,
) : CustomConfig()