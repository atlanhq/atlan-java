/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import javax.annotation.processing.Generated

/**
 * Expected configuration for the OpenAPI Spec Loader custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class OpenAPISpecLoaderCfg(
    @JsonProperty("spec_url") val specUrl: String?,
    @JsonProperty("connection_usage") val connectionUsage: String?,
    @JsonProperty("connection") val connection: String?,
    @JsonProperty("connection_qualified_name") val connectionQualifiedName: String?,
) : CustomConfig()
