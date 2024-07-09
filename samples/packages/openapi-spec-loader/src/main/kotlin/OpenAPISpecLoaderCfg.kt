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
    @JsonProperty("import_type") val importType: String? = null,
    @JsonProperty("spec_url") val specUrl: String? = null,
    @JsonProperty("spec_file") val specFile: String? = null,
    @JsonProperty("spec_prefix") val specPrefix: String? = null,
    @JsonProperty("spec_key") val specKey: String? = null,
    @JsonProperty("cloud_source") val cloudSource: String? = null,
    @JsonProperty("connection_usage") val connectionUsage: String? = null,
    @JsonDeserialize(using = WidgetSerde.ConnectionDeserializer::class)
    @JsonSerialize(using = WidgetSerde.ConnectionSerializer::class)
    @JsonProperty("connection") val connection: Connection? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("connection_qualified_name") val connectionQualifiedName: List<String>? = null,
) : CustomConfig()
