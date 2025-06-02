/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.pkg.CustomConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty
import javax.annotation.processing.Generated

/**
 * Expected configuration for the Owner Propagator custom package.
 */
@Generated("com.atlan.pkg.CustomPackage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class OwnerPropagatorCfg(
    @JsonProperty("qn_prefix") val qnPrefix: String = "default",
) : CustomConfig<OwnerPropagatorCfg>()
