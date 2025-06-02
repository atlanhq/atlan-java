/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Tests Cleanup custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class TestsCleanupCfg(
    @JsonProperty("prefix") val prefix: String = "",
) : CustomConfig<TestsCleanupCfg>()
