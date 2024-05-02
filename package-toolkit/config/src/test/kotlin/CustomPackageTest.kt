/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
import com.atlan.pkg.Config
import com.atlan.pkg.Renderers
import org.pkl.config.java.ConfigEvaluator
import org.pkl.config.kotlin.forKotlin
import org.pkl.config.kotlin.to
import org.pkl.core.ModuleSource
import org.testng.Assert.assertFalse
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

object CustomPackageTest {

    lateinit var config: Config

    @BeforeClass
    fun modelEval() {
        val source = ModuleSource.path("src/test/resources/CustomPackageTest.pkl")
        config = ConfigEvaluator.preconfigured().forKotlin().use { evaluator ->
            evaluator.evaluate(source).to<Config>()
        }
    }

    @Test
    fun testModelBasics() {
        assertNotNull(config)
        assertEquals("@csa/openapi-spec-loader", config.packageId)
        assertEquals("OpenAPI Spec Loader", config.packageName)
        assertEquals("Loads API specs and paths from an OpenAPI (v3) definition.", config.description)
        assertEquals("http://assets.atlan.com/assets/apispec.png", config.iconUrl)
        assertEquals("https://developer.atlan.com/samples/loaders/openapi/", config.docsUrl)
        assertEquals("ghcr.io/atlanhq/atlan-kotlin-samples:1.0.0", config.containerImage)
        assertEquals(listOf("/dumb-init", "--", "java", "OpenAPISpecLoaderKt"), config.containerCommand)
        assertEquals(listOf("kotlin", "crawler", "openapi"), config.keywords)
        assertTrue(config.preview)
        assertEquals("api", config.connectorType?.value)
        assertNotNull(config.outputs)
        assertEquals(Config.ImagePullPolicy.IF_NOT_PRESENT, config.containerImagePullPolicy)
        assertNotNull(config.outputs)
        assertTrue(config.allowSchedule)
        assertTrue(config.certified)
        assertTrue(config.preview)
        assertEquals("custom", config.category)
    }

    @Test
    fun testGeneratedElements() {
        val artifacts = config.outputs!!.artifacts
        assertNotNull(artifacts)
        assertEquals(1, artifacts.size)
        assertEquals(Renderers.NamePathPair("debug-logs", "/tmp/debug.log"), artifacts[0])
    }

    @Test
    fun testProperties() {
        assertNotNull(config.uiConfig.properties)
        assertEquals(5, config.uiConfig.properties.size)
    }

    @Test
    fun testSpecUrl() {
        assertTrue(config.uiConfig.properties.containsKey("spec_url"))
        val specUrl = config.uiConfig.properties["spec_url"]
        assertNotNull(specUrl)
        assertTrue(specUrl is Config.TextInput)
        assertTrue(specUrl.required)
        val widget = specUrl.ui
        assertNotNull(widget)
        assertEquals("Specification URL", widget.label)
        assertEquals("input", widget.widget)
        assertEquals("Full URL to the JSON form of the OpenAPI specification.", widget.help)
        assertEquals("https://petstore3.swagger.io/api/v3/openapi.json", widget.placeholder)
        assertEquals(8, widget.grid)
        assertFalse(widget.hidden)
        assertNull(widget.mode)
        assertNull(widget.start)
    }

    @Test
    fun testConnectionUsage() {
        assertTrue(config.uiConfig.properties.containsKey("connection_usage"))
        val connectionUsage = config.uiConfig.properties["connection_usage"]
        assertNotNull(connectionUsage)
        assertTrue(connectionUsage is Config.Radio)
        assertTrue(connectionUsage.required)
        assertEquals("REUSE", connectionUsage.default)
        assertEquals(2, connectionUsage.enum.size)
        assertEquals("CREATE", connectionUsage.enum[0])
        assertEquals("REUSE", connectionUsage.enum[1])
        assertEquals("Create", connectionUsage.enumNames[0])
        assertEquals("Reuse", connectionUsage.enumNames[1])
        val widget = connectionUsage.ui
        assertNotNull(widget)
        assertEquals("Connection", widget.label)
        assertEquals("radio", widget.widget)
        assertEquals("Whether to create a new connection to hold these API assets, or reuse an existing connection.", widget.help)
        assertEquals(8, widget.grid)
        assertFalse(widget.hidden)
        assertNull(widget.mode)
        assertNull(widget.start)
    }

    @Test
    fun testConnection() {
        assertTrue(config.uiConfig.properties.containsKey("connection"))
        val connection = config.uiConfig.properties["connection"]
        assertNotNull(connection)
        assertTrue(connection is Config.ConnectionCreator)
        assertTrue(connection.required)
        val widget = connection.ui
        assertNotNull(widget)
        assertEquals("Connection", widget.label)
        assertEquals("connection", widget.widget)
        assertEquals("Enter details for a new connection to be created.", widget.help)
        assertEquals(8, widget.grid)
        assertFalse(widget.hidden)
        assertNull(widget.mode)
        assertNull(widget.start)
    }

    @Test
    fun testConnectionQualifiedName() {
        assertTrue(config.uiConfig.properties.containsKey("connection_qualified_name"))
        val connection = config.uiConfig.properties["connection_qualified_name"]
        assertNotNull(connection)
        assertTrue(connection is Config.ConnectionSelector)
        assertTrue(connection.required)
        val widget = connection.ui
        assertNotNull(widget)
        assertEquals("Connection", widget.label)
        assertEquals("connectionSelector", widget.widget)
        assertEquals("Select an existing connection to load assets into.", widget.help)
        assertEquals(8, widget.grid)
        assertFalse(widget.hidden)
        assertEquals("", widget.mode)
        assertEquals(1, widget.start)
    }

    @Test
    fun testCredential() {
        assertTrue(config.uiConfig.properties.containsKey("credential_guid"))
        val credential = config.uiConfig.properties["credential_guid"]
        assertNotNull(credential)
        assertTrue(credential is Config.CredentialInput)
        assertTrue(credential.required)
        val widget = credential.ui
        assertEquals("Credential", widget.label)
        assertEquals("credential", widget.widget)
        assertEquals("Select a credential to use for this connection.", widget.help)
        assertEquals(8, widget.grid)
        assertFalse(widget.hidden)
        assertEquals("csa-connectors-gcs", widget.credentialType)
    }

    // TODO: Test generated credential / connector config
    
    // TODO: Test generated workflow template contents
    // TODO: Test generated package JSON contents
}
