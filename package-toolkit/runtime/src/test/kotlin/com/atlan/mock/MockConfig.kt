/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.mock

import com.atlan.pkg.CustomConfig
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonProperty

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
data class MockConfig(
    @JsonProperty("unused") val unused: String = "UNUSED",
) : CustomConfig<MockConfig>()
