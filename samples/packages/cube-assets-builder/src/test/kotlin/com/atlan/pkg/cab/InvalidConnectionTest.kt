/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.cab

import CubeAssetsBuilderCfg
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.pkg.PackageContext
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import kotlin.test.Test

/**
 * Test caching when there is an invalid connection defined in a tenant.
 */
class InvalidConnectionTest : PackageTest("ic") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val conn1 = makeUnique("c1")
    private val conn1Type = AtlanConnectorType.AZURE_ANALYSIS_SERVICES
    private lateinit var connection: Connection
    private lateinit var ctx: PackageContext<CubeAssetsBuilderCfg>

    private fun createInvalidConnection() {
        val req = Connection.creator(client, conn1, conn1Type)
            .qualifiedName(conn1)
            .nullField("connectorName")
            .build()
        val resp = req.save(client).block()
        connection = resp.getResult(req)
    }

    override fun setup() {
        createInvalidConnection()
        ctx =
            PackageContext(
                config = CubeAssetsBuilderCfg(),
                client = client,
                reusedClient = true,
            )
    }

    override fun teardown() {
        client.assets.delete(connection.guid, AtlanDeleteType.PURGE)
    }

    @Test
    fun preloadCache() {
        ctx.connectionCache.preload()
        ctx.connectionCache.getIdentityMap()
    }

    @Test
    fun warningLogged() {
        logHasMessage(
            "WARN",
            """
                Unable to uniquely identify asset for cache -- skipping it: $conn1
            """.trimIndent(),
        )
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }
}
