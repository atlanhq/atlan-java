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
    @JsonProperty("include_views") val includeViews: String? = null,
    @JsonProperty("views_max") val viewsMax: Number? = null,
    @JsonProperty("include_changes") val includeChanges: String? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("changes_by_user") val changesByUser: List<String>? = null,
    @JsonDeserialize(using = WidgetSerde.MultiSelectDeserializer::class)
    @JsonSerialize(using = WidgetSerde.MultiSelectSerializer::class)
    @JsonProperty("changes_types") val changesTypes: List<String>? = null,
    @JsonProperty("changes_from") val changesFrom: Long? = null,
    @JsonProperty("changes_to") val changesTo: Long? = null,
    @JsonProperty("changes_max") val changesMax: Number? = null,
    @JsonProperty("include_searches") val includeSearches: String? = null,
    @JsonProperty("maximum_searches") val maximumSearches: Number? = null,
    @JsonProperty("email_addresses") val emailAddresses: String? = null,
) : CustomConfig()
