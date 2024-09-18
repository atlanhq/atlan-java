/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package model

import com.atlan.model.assets.Persona
import com.atlan.model.enums.AuthPolicyType
import com.atlan.model.enums.PersonaMetadataAction

object AEFPersona {
    lateinit var referencePersona: Persona

    fun create(scholar: Fellowship.Scholar? = null) {
        val personaName = scholar?.id ?: "AEF Reference"
        val description =
            if (scholar != null) {
                "Access control for user ID ${scholar.id} during the Atlan Engineering Fellowship."
            } else {
                "Read-only access to the immutable reference set of assets for the Atlan Engineering Fellowship."
            }
        val builder =
            Persona.creator(personaName)
                .description(description)
                .personaUsers(AEFConnection.SUPER_ADMINS)
        if (scholar != null) {
            builder.personaUser(Fellowship.users[scholar.id]!!.username)
        } else {
            builder.personaUsers(Fellowship.users.values.map { it.username })
        }
        val toCreate = builder.build()
        val response = toCreate.save()
        val result = response.getResult(toCreate)
        if (scholar != null) {
            Fellowship.personas[scholar.id] = result
        } else {
            referencePersona = result
        }
        createPolicies(result, scholar)
    }

    private fun createPolicies(
        persona: Persona,
        scholar: Fellowship.Scholar?,
    ) {
        if (scholar != null) {
            createAllAccessPolicy(persona.guid, Fellowship.dbConnections[scholar.id]!!.qualifiedName, "All access for ${scholar.id} DB assets")
            createAllAccessPolicy(persona.guid, Fellowship.biConnections[scholar.id]!!.qualifiedName, "All access for ${scholar.id} BI assets")
        } else {
            createReadOnlyPolicy(persona.guid, AEFConnection.db.qualifiedName, "Read-only for AEF DB assets")
            createReadOnlyPolicy(persona.guid, AEFConnection.bi.qualifiedName, "Read-only for AEF BI assets")
        }
    }

    private fun createReadOnlyPolicy(
        personaGuid: String,
        connectionQN: String,
        description: String,
    ) {
        Persona.createMetadataPolicy(
            description,
            personaGuid,
            AuthPolicyType.ALLOW,
            listOf(PersonaMetadataAction.READ),
            connectionQN,
            listOf("entity:$connectionQN"),
        ).build().save()
    }

    private fun createAllAccessPolicy(
        personaGuid: String,
        connectionQN: String,
        description: String,
    ) {
        Persona.createMetadataPolicy(
            description,
            personaGuid,
            AuthPolicyType.ALLOW,
            PersonaMetadataAction.entries,
            connectionQN,
            listOf("entity:$connectionQN"),
        ).build().save()
    }
}
