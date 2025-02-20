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
    @JsonProperty("lineage_import_type") val lineageImportType: String = "DIRECT",
    @JsonProperty("lineage_file") val lineageFile: String = "",
    @JsonProperty("lineage_prefix") val lineagePrefix: String = "",
    @JsonProperty("lineage_key") val lineageKey: String = "",
    @JsonProperty("cloud_source") val cloudSource: String? = null,
    @JsonProperty("lineage_upsert_semantic") val lineageUpsertSemantic: String = "partial",
    @JsonProperty("lineage_fail_on_errors") val lineageFailOnErrors: Boolean = true,
    @JsonProperty("lineage_case_sensitive") val lineageCaseSensitive: Boolean = true,
    @JsonProperty("field_separator") val fieldSeparator: String = ",",
    @JsonProperty("batch_size") val batchSize: Number = 20,
    @JsonProperty("cm_handling") val cmHandling: String? = null,
    @JsonProperty("tag_handling") val tagHandling: String? = null,
) : CustomConfig()
