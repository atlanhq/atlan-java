/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.model.assets.Connection
import com.atlan.pkg.CustomConfig
import com.atlan.pkg.serde.WidgetSerde
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import javax.annotation.processing.Generated

/**
 * Expected configuration for the OpenAPI Spec Loader custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class OpenAPISpecLoaderCfg(
    @JsonProperty("import_type") val importType: String = "URL",
    @JsonProperty("spec_url") val specUrl: String = "",
    @JsonProperty("spec_file") val specFile: String = "",
    @JsonProperty("spec_prefix") val specPrefix: String = "",
    @JsonProperty("spec_key") val specKey: String = "",
    @JsonProperty("cloud_source") val cloudSource: String? = null,
    @JsonProperty("connection_usage") val connectionUsage: String? = null,
    @JsonDeserialize(using = WidgetSerde.ConnectionDeserializer::class)
    @JsonSerialize(using = WidgetSerde.ConnectionSerializer::class)
    @JsonProperty("connection") val connection: Connection? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("connection_qualified_name") val connectionQualifiedName: List<String>? = null,
) : CustomConfig<OpenAPISpecLoaderCfg>()
