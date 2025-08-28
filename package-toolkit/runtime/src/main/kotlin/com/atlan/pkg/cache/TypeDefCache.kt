/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cache

import com.atlan.cache.AbstractMassCache
import com.atlan.exception.AtlanException
import com.atlan.model.enums.AtlanTypeCategory
import com.atlan.model.typedefs.EntityDef
import com.atlan.model.typedefs.RelationshipDef
import com.atlan.model.typedefs.TypeDef
import com.atlan.net.RequestOptions
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

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

    private var relationshipsPreloaded = AtomicBoolean(false)

    private val directSupertypesCache: MutableMap<String, Set<String>> = ConcurrentHashMap()
    private val transitiveSupertypesCache: MutableMap<String, Set<String>> = ConcurrentHashMap()
    private val typeToRelationships: MutableMap<String, MutableSet<String>> = ConcurrentHashMap()
    private val cyclicalRelationshipsMap: MutableMap<String, MutableSet<RelationshipEnds>> = ConcurrentHashMap()

    private val inheritanceLock = ReentrantReadWriteLock()
    private val relationshipLock = ReentrantReadWriteLock()

    init {
        this.bulkRefresh.set(false)
    }

    /** {@inheritDoc} */
    override fun lookupById(id: String?) {
        var result: TypeDef? = null
        try {
            logger.debug { " ... looking up: $id" }
            result =
                client.typeDefs.getByGuid(
                    id,
                    RequestOptions.from(client).skipLogging(true).build(),
                )
        } catch (e: AtlanException) {
            logger.warn { "Unable to lookup or find typedef: $id" }
            logger.debug(e) { "Full stack trace:" }
        }
        if (result != null) cache(result.guid, result.name, result)
    }

    /** {@inheritDoc} */
    override fun lookupByName(name: String?) {
        var result: TypeDef? = null
        try {
            logger.debug { " ... looking up: $name" }
            result =
                client.typeDefs.get(
                    name,
                    RequestOptions.from(client).skipLogging(true).build(),
                )
        } catch (e: AtlanException) {
            logger.warn { "Unable to lookup or find typedef: $name" }
            logger.debug(e) { "Full stack trace:" }
        }
        if (result != null) cache(result.guid, name, result)
    }

    /**
     * Construct a reusable, unique identity for the typedef.
     *
     * @param typedef for which to construct the identity
     * @return the unique string identity
     */
    fun getIdentityForTypedef(typedef: TypeDef): String = "${typedef.category.value}$typeDelimiter${typedef.name}"

    /**
     * Load all relationship definitions to identify cyclical relationships
     */
    @Synchronized
    fun preloadRelationships() {
        if (!relationshipsPreloaded.get()) {
            logger.debug { "Loading relationship definitions for cycle detection..." }
            val response =
                client.typeDefs.list(
                    listOf(AtlanTypeCategory.RELATIONSHIP),
                    RequestOptions.from(client).skipLogging(true).build(),
                )
            response.relationshipDefs.forEach { relationshipDef ->
                cache(relationshipDef.guid, relationshipDef.name, relationshipDef)
                val type1 = relationshipDef.endDef1.type
                val type2 = relationshipDef.endDef2.type
                typeToRelationships.getOrPut(type1) { mutableSetOf() }.add(relationshipDef.name)
                typeToRelationships.getOrPut(type2) { mutableSetOf() }.add(relationshipDef.name)
            }
            relationshipsPreloaded.set(true)
        }
    }

    /**
     * Get the direct supertypes for a given type name
     *
     * @param typeName the name of the type
     * @return set of direct supertype names, or empty set if none
     */
    private fun getDirectSupertypes(typeName: String): Set<String> {
        directSupertypesCache[typeName]?.let { return it }
        val type = getByName(typeName, true)
        if (type is EntityDef) {
            // Cache direct supertypes, and if we've fallen through to here invalidate transitive cache
            // (to force it to be recalculated next time it's looked up)
            directSupertypesCache[typeName] = type.superTypes?.toSet() ?: setOf()
            inheritanceLock.write {
                transitiveSupertypesCache.remove(typeName)
            }
        }
        return directSupertypesCache.getOrDefault(typeName, emptySet())
    }

    /**
     * Get all transitive supertypes for a given type name
     *
     * @param typeName the name of the type
     * @return set of all supertype names (including transitive ones)
     */
    private fun getTransitiveSupertypes(typeName: String): Set<String> {
        // Short-circuit from transitive cache, if it's there
        inheritanceLock.read {
            transitiveSupertypesCache[typeName]?.let { return it }
        }

        inheritanceLock.write {
            // Double-check after acquiring the write lock
            transitiveSupertypesCache[typeName]?.let { return it }
            val result = computeTransitiveSupertypes(typeName)
            transitiveSupertypesCache[typeName] = result
            return result
        }
    }

    /**
     * Compute the complete set of transitive supertypes for a type
     */
    private fun computeTransitiveSupertypes(typeName: String): Set<String> {
        val direct = getDirectSupertypes(typeName)
        val transitive = mutableSetOf<String>()

        // Add direct supertypes
        transitive.addAll(direct)

        // Recursively add supertypes of supertypes
        for (supertype in direct) {
            // Use already cached results if available
            val cachedTransitive = transitiveSupertypesCache[supertype]
            if (cachedTransitive != null) {
                transitive.addAll(cachedTransitive)
            } else {
                // Recursively compute
                val supertypeTransitive = computeTransitiveSupertypes(supertype)
                transitive.addAll(supertypeTransitive)
                // Cache the result for this supertype too
                transitiveSupertypesCache[supertype] = supertypeTransitive
            }
        }

        return transitive.toSet()
    }

    /**
     * Check if one type is a subtype of another
     *
     * @param type1 the potential subtype
     * @param type2 the potential supertype
     * @return true if type1 is a subtype of (or equal to) type2
     */
    fun isSubtypeOf(
        type1: String,
        type2: String,
    ): Boolean {
        if (type1 == type2) return true
        return type2 in getTransitiveSupertypes(type1)
    }

    /**
     * Check if two types form a cycle in the inheritance hierarchy
     *
     * @param type1 first type to compare
     * @param type2 second type to compare
     * @return true if the types are the same or cyclically related through inheritance
     */
    fun formsCycle(
        type1: String,
        type2: String,
    ): Boolean = isSubtypeOf(type1, type2) || isSubtypeOf(type2, type1)

    private fun getRelationshipsForType(typeName: String): Set<String> {
        preloadRelationships()
        return typeToRelationships[typeName]?.toSet() ?: emptySet()
    }

    /**
     * Compute and cache cyclical relationships for a type
     */
    private fun computeCyclicalRelationshipsForType(typeName: String) {
        relationshipLock.write {
            // Skip if already computed
            if (cyclicalRelationshipsMap.containsKey(typeName)) {
                return
            }

            logger.debug { " ... calculating cyclical relationships for type: $typeName" }
            val cyclicalRelationships = mutableSetOf<RelationshipEnds>()

            // Get all relationships involving this type
            val relationshipNames = getRelationshipsForType(typeName)

            // Check each relationship for cycles
            relationshipNames.forEach { relationshipName ->
                val relationshipDef = getByName(relationshipName)
                if (relationshipDef is RelationshipDef) {
                    val type1 = relationshipDef.endDef1.type
                    val type2 = relationshipDef.endDef2.type

                    if (formsCycle(type1, type2)) {
                        val re =
                            RelationshipEnds(
                                relationshipDef.name,
                                relationshipDef.endDef1.name,
                                relationshipDef.endDef2.name,
                            )
                        cyclicalRelationships.add(re)

                        // Also add to the map for the other type
                        val otherType = if (type1 == typeName) type2 else type1
                        cyclicalRelationshipsMap.getOrPut(otherType) { mutableSetOf() }.add(re)
                    }
                }
            }

            // Store the results (even if empty)
            cyclicalRelationshipsMap[typeName] = cyclicalRelationships
        }
    }

    /** {@inheritDoc}  */
    override fun refreshCache() {
        logger.warn { "Full refresh requested, but not handled - will only lazy-load..." }
    }

    /**
     * Fetch the cyclical relationships for the provided entity type.
     *
     * @param typeName of the entity
     * @return the set of cyclical relationships that exist for that type of entity
     */
    fun getCyclicalRelationshipsForType(typeName: String): Set<RelationshipEnds> {
        logger.info { "Checking cyclical relationships for type: $typeName" }
        if (relationshipLock.read { !cyclicalRelationshipsMap.containsKey(typeName) }) {
            computeCyclicalRelationshipsForType(typeName)
        }

        // Start with any direct cyclical relationships for this type
        val consolidated = relationshipLock.read { cyclicalRelationshipsMap[typeName]?.toMutableSet() ?: mutableSetOf() }

        // Then for each supertype, ensure its cyclical relationships are computed and added
        val supertypes = getTransitiveSupertypes(typeName)
        supertypes.forEach { supertype ->
            if (relationshipLock.read { !cyclicalRelationshipsMap.containsKey(supertype) }) {
                computeCyclicalRelationshipsForType(supertype)
            }
            relationshipLock.read {
                cyclicalRelationshipsMap[supertype]?.let { consolidated.addAll(it) }
            }
        }

        return consolidated.toSet()
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
