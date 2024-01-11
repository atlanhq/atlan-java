/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import javax.annotation.processing.Generated

/**
 * Expected configuration for the API Token Connection Admin custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class APITokenConnectionAdminCfg(
    @JsonProperty("connection_qualified_name") val connectionQualifiedName: String? = null,
    @JsonProperty("api_token_guid") val apiTokenGuid: String? = null,
) : CustomConfig()
