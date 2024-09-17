/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.model.admin.ApiToken
import com.atlan.model.admin.AtlanUser
import com.atlan.model.assets.Connection
import com.atlan.model.assets.Persona
import kotlin.math.absoluteValue

object Fellowship {
    data class Scholar(
        val firstName: String,
        val lastName: String,
        val emailAddress: String,
        val id: String = "$firstName.$lastName@$emailAddress".lowercase().hashCode().absoluteValue.toString(36),
    )

    data class Roster(
        val scholars: Set<Scholar>,
    )

    val users = mutableMapOf<String, AtlanUser>()
    val connections = mutableMapOf<String, Connection>()
    val personas = mutableMapOf<String, Persona>()
    val apiTokens = mutableMapOf<String, ApiToken>()
}
