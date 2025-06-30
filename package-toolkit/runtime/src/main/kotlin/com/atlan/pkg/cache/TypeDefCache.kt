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
import java.util.stream.Stream

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
    private val cyclicalRelationshipsMap: MutableMap<String, Set<RelationshipEnds>> = ConcurrentHashMap()

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
    fun getIdentityForTypedef(typedef: TypeDef): String {
        return "${typedef.category.value}$typeDelimiter${typedef.name}"
    }

    /** {@inheritDoc}  */
    override fun refreshCache() {
        logger.debug { "Refreshing cache of typedefs..." }
        val response = client.typeDefs.list(
            listOf(AtlanTypeCategory.ENTITY, AtlanTypeCategory.RELATIONSHIP),
            RequestOptions.from(client).skipLogging(true).build(),
        )
        cacheInheritance(response.entityDefs)
        response.relationshipDefs.forEach { relationDef ->
            cache(getIdentityForTypedef(relationDef), relationDef.name, relationDef)
        }
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

    /**
     * Fetch the cyclical relationships for the provided entity type.
     *
     * @param typeName of the entity
     * @return the set of cyclical relationships that exist for that type of entity
     */
    fun getCyclicalRelationshipsForType(
        typeName: String,
        alreadyHandled: Set<String> = emptySet(),
    ): Set<RelationshipEnds> {
        // Return cached result if available
        cyclicalRelationshipsMap[typeName]?.let { return it }

        val cycles = mutableSetOf<RelationshipEnds>()
        listAll()
            .filter { it.value is RelationshipDef }
            .map { it.value as RelationshipDef }
            .forEach { relationshipDef ->
                if (!alreadyHandled.contains(relationshipDef.name)) {
                    if (isCyclical(relationshipDef)) {
                        cycles.add(
                            RelationshipEnds(
                                relationshipDef.name,
                                relationshipDef.endDef1.type,
                                relationshipDef.endDef2.type,
                            )
                        )
                    }
                    inheritanceMap.getOrDefault(typeName, null)?.forEach { superType ->
                        cycles.addAll(getCyclicalRelationshipsForType(superType))
                    }
                }
        }
        cyclicalRelationshipsMap[typeName] = cycles.toSet()
        return cyclicalRelationshipsMap[typeName]!!
    }

    private fun isCyclical(
        relationshipDef: RelationshipDef,
    ): Boolean {
        if (relationshipDef.endDef1.type == relationshipDef.endDef2.type) {
            return true
        } else {
            val end1Supers = inheritanceMap.getOrElse(relationshipDef.endDef1.type) { emptySet() }
            val end2Supers = inheritanceMap.getOrElse(relationshipDef.endDef2.type) { emptySet() }
            if (end1Supers.contains(relationshipDef.endDef2.type) || end2Supers.contains(relationshipDef.endDef1.type)) {
                return true
            }
        }
        return false
    }

    /**
     * List all the typedefs held in the cache.
     *
     * @return the set of all typedefs in the cache
     */
    fun listAll(): Stream<Map.Entry<String, TypeDef>> = entrySet()

    /** Preload the cache (will only act once, in case called multiple times on the same cache) */
    @Synchronized
    fun preload() {
        if (!preloaded.get()) {
            refreshIfNeeded()
            preloaded.set(true)
        }
    }

    data class RelationshipEnds(
        val name: String,
        val end1: String,
        val end2: String,
    )
}
