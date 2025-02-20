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
 * Expected configuration for the Admin Export custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class AdminExportCfg(
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("objects_to_include") val objectsToInclude: List<String> = listOf("users", "groups"),
    @JsonProperty("include_native_policies") val includeNativePolicies: Boolean = false,
    @JsonProperty("file_format") val fileFormat: String = "XLSX",
    @JsonProperty("delivery_type") val deliveryType: String = "DIRECT",
    @JsonProperty("email_addresses") val emailAddresses: String? = null,
    @JsonProperty("target_prefix") val targetPrefix: String = "",
    @JsonProperty("target_key") val targetKey: String = "",
    @JsonProperty("cloud_target") val cloudTarget: String? = null,
    @JsonProperty("custom_filenames") val customFilenames: Boolean = false,
    @JsonProperty("xlsx_filename") val xlsxFilename: String = "admin-export.xlsx",
    @JsonProperty("users_filename") val usersFilename: String = "users.csv",
    @JsonProperty("groups_filename") val groupsFilename: String = "groups.csv",
    @JsonProperty("personas_filename") val personasFilename: String = "personas.csv",
    @JsonProperty("purposes_filename") val purposesFilename: String = "purposes.csv",
    @JsonProperty("policies_filename") val policiesFilename: String = "policies.csv",
) : CustomConfig()
