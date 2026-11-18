/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package cache

import com.atlan.mock.MockAtlanTenant
import com.atlan.mock.MockConfig
import com.atlan.model.assets.DataDomain
import com.atlan.model.enums.AtlanStatus
import com.atlan.pkg.PackageContext
import com.atlan.pkg.Utils
import com.atlan.pkg.cache.DataDomainCache
import com.atlan.pkg.serde.cell.DataDomainXformer.DATA_DOMAIN_DELIMITER
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * A sub-domain looked up by GUID on a cold (not preloaded) cache must still resolve to its
 * full path. If the parent is read cache-only, the sub-domain's identity collapses to just its
 * own name -- and an import, whose preloaded cache keys domains by full path, can then no
 * longer find that domain.
 */
class DataDomainCacheSubDomainTest {
    companion object {
        init {
            MockAtlanTenant.initializeClient()
        }

        private val config = Utils.parseConfig<MockConfig>("{}", "{}")
        private val ctx = PackageContext(config, MockAtlanTenant.client, true)

        private const val GRANDPARENT_GUID = "0b9f5a52-0000-4000-8000-000000000001"
        private const val PARENT_GUID = "0b9f5a52-0000-4000-8000-000000000002"
        private const val CHILD_GUID = "0b9f5a52-0000-4000-8000-000000000003"
    }

    /** Serves domains from memory instead of searching a tenant. */
    private class InMemoryDomainCache(
        ctx: PackageContext<*>,
        domains: List<DataDomain>,
    ) : DataDomainCache(ctx) {
        private val byGuid = domains.associateBy { it.guid }

        override fun fetchById(guid: String): DataDomain? = byGuid[guid]
    }

    private fun domain(
        guid: String,
        name: String,
        parentGuid: String? = null,
    ): DataDomain {
        val builder =
            DataDomain
                ._internal()
                .guid(guid)
                .qualifiedName("default/domain/$name/super")
                .name(name)
                .status(AtlanStatus.ACTIVE)
        if (parentGuid != null) builder.parentDomain(DataDomain.refByGuid(parentGuid))
        return builder.build()
    }

    @Test
    fun subDomainOnColdCacheResolvesToFullPath() {
        val cache =
            InMemoryDomainCache(
                ctx,
                listOf(
                    domain(PARENT_GUID, "Parent"),
                    domain(CHILD_GUID, "Child", PARENT_GUID),
                ),
            )
        // Same two calls DataDomainXformer.encodeFromGuid makes.
        cache.getByGuid(CHILD_GUID)
        assertEquals("Parent${DATA_DOMAIN_DELIMITER}Child", cache.getIdentity(CHILD_GUID))
    }

    @Test
    fun nestedSubDomainOnColdCacheResolvesEveryAncestor() {
        val cache =
            InMemoryDomainCache(
                ctx,
                listOf(
                    domain(GRANDPARENT_GUID, "Grandparent"),
                    domain(PARENT_GUID, "Parent", GRANDPARENT_GUID),
                    domain(CHILD_GUID, "Child", PARENT_GUID),
                ),
            )
        cache.getByGuid(CHILD_GUID)
        assertEquals(
            "Grandparent${DATA_DOMAIN_DELIMITER}Parent${DATA_DOMAIN_DELIMITER}Child",
            cache.getIdentity(CHILD_GUID),
        )
    }
}
