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
 * Expected configuration for the Adoption Export custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class AdoptionExportCfg(
    @JsonProperty("include_views") val includeViews: String = "BY_VIEWS",
    @JsonProperty("views_max") val viewsMax: Number = 100,
    @JsonProperty("views_details") val viewsDetails: String = "NO",
    @JsonProperty("views_from") val viewsFrom: Long = -90,
    @JsonProperty("views_to") val viewsTo: Long = 0,
    @JsonProperty("include_changes") val includeChanges: String = "NO",
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("changes_by_user") val changesByUser: List<String> = listOf(),
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("changes_types") val changesTypes: List<String> = listOf(),
    @JsonProperty("changes_from") val changesFrom: Long = -90,
    @JsonProperty("changes_to") val changesTo: Long = 0,
    @JsonProperty("changes_max") val changesMax: Number = 100,
    @JsonProperty("changes_details") val changesDetails: String = "NO",
    @JsonProperty("changes_automations") val changesAutomations: String = "NONE",
    @JsonProperty("include_searches") val includeSearches: String = "NO",
    @JsonProperty("searches_from") val searchesFrom: Long = -90,
    @JsonProperty("searches_to") val searchesTo: Long = 0,
    @JsonProperty("file_format") val fileFormat: String = "XLSX",
    @JsonProperty("delivery_type") val deliveryType: String = "DIRECT",
    @JsonProperty("email_addresses") val emailAddresses: String? = null,
    @JsonProperty("target_prefix") val targetPrefix: String? = null,
    @JsonProperty("target_key") val targetKey: String? = null,
    @JsonProperty("cloud_target") val cloudTarget: String? = null,
) : CustomConfig<AdoptionExportCfg>()
