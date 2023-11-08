/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model.workflow

import com.atlan.model.workflow.NameValuePair
import com.atlan.pkg.config.model.ui.UIConfig
import com.fasterxml.jackson.annotation.JsonIgnore

class WorkflowInputs(
    @JsonIgnore val config: UIConfig,
) {
    val parameters: List<NameValuePair>
    val artifacts: List<NamePathS3Tuple>
    init {
        val params = mutableListOf<NameValuePair>()
        val arts = mutableListOf<NamePathS3Tuple>()
        config.properties.forEach { (k, u) ->
            if (u.ui.widget == "fileUpload") {
                arts.add(NamePathS3Tuple(k))
            }
            params.add(NameValuePair.of(k, ""))
        }
        parameters = params.toList()
        artifacts = arts.toList()
    }
}
