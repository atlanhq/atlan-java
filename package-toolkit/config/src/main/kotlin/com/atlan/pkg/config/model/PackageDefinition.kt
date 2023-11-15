/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.config.model

import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.config.model.pkg.PackageConfig
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

/**
 * Root class through which to define the package.json for the custom package.
 *
 * @param packageId unique name for the package, including namespace
 * @param packageName name of the package as it should appear in the UI
 * @param description explanation of the package, as it should appear in the UI
 * @param iconUrl link to an icon to use for the package in the UI
 * @param docsUrl link to the documentation for the package
 * @param keywords list of keywords to apply as labels to the package
 * @param allowSchedule whether to allow the package to be scheduled (default, true), or not (false)
 * @param certified whether the package should be visually labeled as certified (default, true) or not (false)
 * @param preview whether the package should be given a visual indication it is an early preview (true) or not (default, false)
 * @param connectorType if the package needs to configure a connector, specify its type here
 * @param category name of the pill under which the package should be categorized in the marketplace in the UI
 */
@JsonPropertyOrder(
    "name",
    "version",
    "description",
    "keywords",
    "homepage",
    "main",
    "scripts",
    "author",
    "repository",
    "license",
    "bugs",
    "config",
)
class PackageDefinition(
    @JsonProperty("name") val packageId: String,
    @JsonIgnore val packageName: String,
    val description: String,
    @JsonIgnore val iconUrl: String,
    @JsonIgnore val docsUrl: String,
    val keywords: List<String> = listOf(),
    @JsonIgnore val allowSchedule: Boolean = true,
    @JsonIgnore val certified: Boolean = true,
    @JsonIgnore val preview: Boolean = false,
    @JsonIgnore val connectorType: AtlanConnectorType? = null,
    @JsonIgnore val category: String = "custom",
) {
    val version = "0.0.1"
    val homepage = "https://packages.atlan.com/-/web/detail/$packageId"
    val main = "index.js"
    val scripts = mapOf<String, String>()
    val author = mapOf(
        "name" to "Atlan CSA",
        "email" to "csa@atlan.com",
        "url" to "https://atlan.com",
    )
    val repository = mapOf(
        "type" to "git",
        "url" to "https://github.com/atlanhq/marketplace-packages.git",
    )
    val license = "MIT"
    val bugs = mapOf(
        "url" to "https://atlan.com",
        "email" to "support@atlan.com",
    )

    @JsonIgnore private val source = connectorType?.value ?: "atlan"

    @JsonIgnore private val sourceCategory = connectorType?.category?.value ?: "utility"
    val config = PackageConfig(
        labels = mapOf(
            "orchestration.atlan.com/verified" to "true",
            "orchestration.atlan.com/type" to category,
            "orchestration.atlan.com/source" to source,
            "orchestration.atlan.com/sourceCategory" to sourceCategory,
            "orchestration.atlan.com/certified" to certified.toString(),
            "orchestration.atlan.com/preview" to preview.toString(),
        ),
        annotations = mapOf(
            "orchestration.atlan.com/name" to packageName,
            "orchestration.atlan.com/allowSchedule" to allowSchedule.toString(),
            "orchestration.atlan.com/dependentPackage" to "",
            "orchestration.atlan.com/emoji" to "🚀",
            "orchestration.atlan.com/categories" to keywords.joinToString(","),
            "orchestration.atlan.com/icon" to iconUrl,
            "orchestration.atlan.com/logo" to iconUrl,
            "orchestration.atlan.com/docsUrl" to docsUrl,
        ),
    )
}
