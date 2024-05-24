/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.pkg.Config
import com.atlan.pkg.Credential
import org.pkl.config.java.ConfigEvaluator
import org.pkl.config.kotlin.forKotlin
import org.pkl.config.kotlin.to
import org.pkl.core.ModuleSource
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object CredentialTest {

    lateinit var credential: Credential

    @BeforeClass
    fun modelEval() {
        val source = ModuleSource.path("src/test/resources/CredentialTest.pkl")
        credential = ConfigEvaluator.preconfigured().forKotlin().use { evaluator ->
            evaluator.evaluate(source).to<Credential>()
        }
    }

    @Test
    fun testConnectorCredential() {
        assertNotNull(credential)
        assertEquals(9, credential.properties.size)
        val basic = credential.properties["basic"]
        assertTrue(basic is Config.NestedInput)
        assertEquals("Basic", basic.ui.label)
        assertEquals("nested", basic.ui.widget)
        assertEquals(3, basic.properties.size)
        val extra = basic.properties["extra"]
        assertTrue(extra is Config.NestedInput)
        assertEquals("Role and Warehouse", extra.ui.label)
        assertEquals(2, extra.properties.size)
        val role = extra.properties["role"]
        assertTrue(role is Config.SQLExecutor)
        assertEquals("show grants", role.ui.query)
        assertEquals(3, credential.anyOf?.size)
    }

    // TODO: Test generated configmap YAML
}
