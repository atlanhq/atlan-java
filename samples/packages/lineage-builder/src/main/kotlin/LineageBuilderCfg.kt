/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Lineage Builder custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class LineageBuilderCfg(
    @JsonProperty("lineage_import_type") val lineageImportType: String? = null,
    @JsonProperty("lineage_file") val lineageFile: String? = null,
    @JsonProperty("lineage_prefix") val lineagePrefix: String? = null,
    @JsonProperty("lineage_key") val lineageKey: String? = null,
    @JsonProperty("cloud_source") val cloudSource: String? = null,
    @JsonProperty("lineage_upsert_semantic") val lineageUpsertSemantic: String? = null,
    @JsonProperty("lineage_fail_on_errors") val lineageFailOnErrors: Boolean? = null,
    @JsonProperty("lineage_case_sensitive") val lineageCaseSensitive: Boolean? = null,
    @JsonProperty("field_separator") val fieldSeparator: String? = null,
    @JsonProperty("batch_size") val batchSize: Number? = null,
) : CustomConfig()
