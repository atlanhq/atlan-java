/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.pkg.CustomConfig
import com.atlan.pkg.serde.WidgetSerde
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import javax.annotation.processing.Generated

/**
 * Expected configuration for the OpenAPI Spec Loader custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class OpenAPISpecLoaderCfg(
    @JsonProperty("spec_url") val specUrl: String?,
    @JsonProperty("connection_usage") val connectionUsage: String?,
    @JsonDeserialize(using = WidgetSerde.ConnectionDeserializer::class)
    @JsonProperty("connection") val connection: Connection?,
    @JsonProperty("connection_qualified_name") val connectionQualifiedName: String?,
) : CustomConfig()
