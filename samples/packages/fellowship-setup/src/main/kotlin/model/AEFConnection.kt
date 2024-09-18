/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package model

import com.atlan.model.assets.Connection
import com.atlan.model.assets.Readme
import com.atlan.model.enums.AtlanConnectorType

object AEFConnection {
    val CONNECTOR_TYPE = AtlanConnectorType.ICEBERG
    val SUPER_ADMINS = listOf("chris")
    lateinit var referenceConnection: Connection

    fun create(scholar: Fellowship.Scholar? = null) {
        val connectionName = scholar?.id ?: "AEF Reference"
        val description =
            if (scholar != null) {
                "Connection to uniquely isolate assets for user ID: ${scholar.id} during the Atlan Engineering Fellowship."
            } else {
                "Connection containing an immutable reference set of assets for the Atlan Engineering Fellowship."
            }
        val toCreate =
            Connection.creator(connectionName, CONNECTOR_TYPE, null, null, SUPER_ADMINS)
                .description(description)
                .build()
        val response = toCreate.save().block()
        val result = response.getResult(toCreate)
        if (scholar != null) {
            Fellowship.connections[scholar.id] = result
        } else {
            referenceConnection = result
        }
        Readme.creator(result, AEFRichText.getConnectionReadme())
            .build().save()
    }

    private fun createAssets(connection: Connection) {
        TODO("Implement the creation of assets within the connection.")
    }
}
