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
            val connectionQN = Fellowship.dbConnections[scholar.id]!!.qualifiedName
            Persona.createMetadataPolicy(
                "All assets for ${scholar.id}",
                persona.guid,
                AuthPolicyType.ALLOW,
                PersonaMetadataAction.entries,
                connectionQN,
                listOf("entity:$connectionQN"),
            ).build().save()
        } else {
            val connectionQN = AEFConnection.referenceConnection.qualifiedName
            Persona.createMetadataPolicy(
                "Read-only for AEF Reference assets",
                persona.guid,
                AuthPolicyType.ALLOW,
                listOf(PersonaMetadataAction.READ),
                connectionQN,
                listOf("entity:$connectionQN"),
            ).build().save()
        }
    }
}
