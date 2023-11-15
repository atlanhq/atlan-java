/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.atlan.pkg.serde.MultiSelectDeserializer
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Asset Import custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class AssetImportCfg(
    @JsonProperty("uploaded_file") val uploadedFile: String?,
    @JsonDeserialize(using = MultiSelectDeserializer::class)
    @JsonProperty("attr_to_overwrite") val attrToOverwrite: List<String>?,
    @JsonProperty("upsert_semantic") val upsertSemantic: String?,
    @JsonProperty("control_config_strategy") val controlConfigStrategy: String?,
    @JsonProperty("batch_size") val batchSize: String?,
) : CustomConfig()
