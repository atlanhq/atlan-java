/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.cache.AbstractMassCache
import com.atlan.model.enums.AtlanTypeCategory
import com.atlan.model.typedefs.EntityDef
import com.atlan.model.typedefs.RelationshipDef
import com.atlan.model.typedefs.TypeDef
import com.atlan.net.RequestOptions
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Utility class for bulk-loading a cache of typedefs.
 *
 * @param ctx context for the custom package
 */
class TypeDefCache(
    ctx: PackageContext<*>,
) : AbstractMassCache<TypeDef>(
        ctx.client,
        "typedef",
    ) {
    private val logger = Utils.getLogger(this.javaClass.name)

    private val typeDelimiter = "|"
    private var preloaded = AtomicBoolean(false)
    private val inheritanceMap: MutableMap<String, Set<String>> = ConcurrentHashMap()
    private val cyclicalRelationshipsMap: MutableMap<String, MutableSet<RelationshipEnds>> = ConcurrentHashMap()

    init {
        this.bulkRefresh.set(true)
    }

    /** {@inheritDoc} */
    override fun lookupById(id: String?) {
        // Nothing to do -- only allow bulk-loading
    }

    /** {@inheritDoc} */
    override fun lookupByName(name: String?) {
        // Nothing to do -- only allow bulk-loading
    }

    /**
     * Construct a reusable, unique identity for the typedef.
     *
     * @param typedef for which to construct the identity
     * @return the unique string identity
     */
    fun getIdentityForTypedef(typedef: TypeDef): String = "${typedef.category.value}$typeDelimiter${typedef.name}"

    /** {@inheritDoc}  */
    override fun refreshCache() {
        logger.debug { "Refreshing cache of typedefs..." }
        val response =
            client.typeDefs.list(
                listOf(AtlanTypeCategory.ENTITY, AtlanTypeCategory.RELATIONSHIP),
                RequestOptions.from(client).skipLogging(true).build(),
            )
        cacheInheritance(response.entityDefs)
        cacheRelationshipCycles(response.relationshipDefs)
    }

    private fun cacheInheritance(entityDefs: List<EntityDef>) {
        // First, create a map of direct supertype relationships
        val directSupertypes = entityDefs.associate { it.name to it.superTypes.toSet() }

        // Memoization cache for computed transitive supertypes
        val cache = mutableMapOf<String, Set<String>>()

        fun getTransitiveSupertypes(typeName: String): Set<String> {
            // Return cached result if available
            cache[typeName]?.let { return it }

            val direct = directSupertypes[typeName] ?: emptySet()
            val transitive = mutableSetOf<String>()

            // Add direct supertypes
            transitive.addAll(direct)

            // Recursively add supertypes of supertypes
            for (supertype in direct) {
                transitive.addAll(getTransitiveSupertypes(supertype))
            }

            // Cache and return result
            val result = transitive.toSet()
            cache[typeName] = result
            return result
        }

        // Compute for all entity types
        entityDefs.forEach { entityDef ->
            inheritanceMap[entityDef.name] = getTransitiveSupertypes(entityDef.name)
        }
    }

    private fun cacheRelationshipCycles(relationshipDefs: List<RelationshipDef>) {

        fun isSubtypeOf(type1: String, type2: String): Boolean {
            if (type1 == type2) return true

            val visited = mutableSetOf<String>()
            val stack = mutableListOf(type1)

            while (stack.isNotEmpty()) {
                val current = stack.removeLastOrNull() ?: break
                if (current in visited) continue
                visited.add(current)
                inheritanceMap[current]?.forEach { supertype ->
                    if (supertype == type2) return true
                    stack.add(supertype)
                }
            }

            return false
        }

        fun formsCycle(type1: String, type2: String): Boolean {
            return isSubtypeOf(type1, type2) ||
                isSubtypeOf(type2, type1)
        }

        relationshipDefs
            .filter { formsCycle(it.endDef1.type, it.endDef2.type) }
            .forEach { relationshipDef ->
                val re = RelationshipEnds(
                    relationshipDef.name,
                    relationshipDef.endDef1.name,
                    relationshipDef.endDef2.name,
                )
                cyclicalRelationshipsMap.getOrPut(relationshipDef.endDef1.type) { mutableSetOf() }.add(re)
                cyclicalRelationshipsMap.getOrPut(relationshipDef.endDef2.type) { mutableSetOf() }.add(re)
            }
    }

    /**
     * Fetch the cyclical relationships for the provided entity type.
     *
     * @param typeName of the entity
     * @return the set of cyclical relationships that exist for that type of entity
     */
    fun getCyclicalRelationshipsForType(
        typeName: String,
    ): Set<RelationshipEnds> {
        // Look up the cyclical relationships in this type and all of its supertypes
        val consolidated = cyclicalRelationshipsMap.getOrElse(typeName) { mutableSetOf() }.toMutableSet()
        inheritanceMap[typeName]?.forEach {
            consolidated.addAll(getCyclicalRelationshipsForType(it))
        }
        return consolidated.toSet()
    }

    /** Preload the cache (will only act once, in case called multiple times on the same cache) */
    @Synchronized
    fun preload() {
        if (!preloaded.get()) {
            refreshIfNeeded()
            preloaded.set(true)
        }
    }

    /**
     * @param name of the relationship
     * @param end1 name of the attribute at end1 of the relationship
     * @param end2 name of the attribute at end2 of the relationship
     */
    data class RelationshipEnds(
        val name: String,
        val end1: String,
        val end2: String,
    )
}
