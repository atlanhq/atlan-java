/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
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
    @JsonProperty("lineage_s3_region") val lineageS3Region: String? = null,
    @JsonProperty("lineage_s3_bucket") val lineageS3Bucket: String? = null,
    @JsonProperty("lineage_s3_object_key") val lineageS3ObjectKey: String? = null,
    @JsonProperty("lineage_upsert_semantic") val lineageUpsertSemantic: String? = null,
    @JsonProperty("lineage_fail_on_errors") val lineageFailOnErrors: Boolean? = null,
    @JsonProperty("lineage_case_sensitive") val lineageCaseSensitive: Boolean? = null,
) : CustomConfig()
