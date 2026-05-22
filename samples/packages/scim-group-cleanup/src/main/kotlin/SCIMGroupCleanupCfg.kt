/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import javax.annotation.processing.Generated

/**
 * Expected configuration for the SCIM Group Cleanup custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class SCIMGroupCleanupCfg(
    @JsonProperty("group_name") val groupName: String? = null,
    @JsonProperty("operation_mode") val operationMode: String = "DIAGNOSTIC",
    @JsonProperty("recreate_group") val recreateGroup: Boolean = true,
) : CustomConfig<SCIMGroupCleanupCfg>()
