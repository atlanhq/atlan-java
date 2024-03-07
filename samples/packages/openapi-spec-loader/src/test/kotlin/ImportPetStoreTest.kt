/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.Atlan
import com.atlan.model.assets.APIPath
import com.atlan.model.assets.APISpec
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test import of the canonical PetStore example from Swagger.
 */
class ImportPetStoreTest : PackageTest() {

    private val testId = makeUnique("oapi")
    private val files = listOf(
        "debug.log",
    )

    @BeforeClass
    fun beforeClass() {
        setup(
            OpenAPISpecLoaderCfg(
                specUrl = "https://petstore3.swagger.io/api/v3/openapi.json",
                connectionUsage = "CREATE",
                connection = Connection.creator(testId, AtlanConnectorType.API, listOf(Atlan.getDefaultClient().roleCache.getIdForName("\$admin")), null, null).build(),
            ),
        )
        OpenAPISpecLoader.main(arrayOf(testDirectory))
    }

    @Test
    fun connectionCreated() {
        val results = Connection.findByName(testId, AtlanConnectorType.API)
        assertNotNull(results)
        assertEquals(1, results.size)
        assertEquals(testId, results[0].name)
    }

    @Test
    fun specCreated() {
        val connectionQN = Connection.findByName(testId, AtlanConnectorType.API)?.get(0)?.qualifiedName!!
        val request = APISpec.select()
            .where(APISpec.QUALIFIED_NAME.startsWith(connectionQN))
            .includeOnResults(APISpec.NAME)
            .includeOnResults(APISpec.API_SPEC_TYPE)
            .includeOnResults(APISpec.API_SPEC_LICENSE_URL)
            .includeOnResults(APISpec.API_SPEC_TERMS_OF_SERVICE_URL)
            .includeOnResults(APISpec.API_IS_AUTH_OPTIONAL)
            .toRequest()
        val response = retrySearchUntil(request, 1)
        val results = response.stream().toList()
        assertEquals(1, results.size)
        val one = results[0] as APISpec
        assertEquals("Swagger Petstore - OpenAPI 3.0", one.name)
        assertEquals("3.0.2", one.apiSpecType)
        assertEquals("http://www.apache.org/licenses/LICENSE-2.0.html", one.apiSpecLicenseURL)
        assertEquals("http://swagger.io/terms/", one.apiSpecTermsOfServiceURL)
        assertFalse(one.apiIsAuthOptional)
        assertTrue(one.qualifiedName.startsWith(connectionQN))
    }

    @Test
    fun pathsCreated() {
        val connectionQN = Connection.findByName(testId, AtlanConnectorType.API)?.get(0)?.qualifiedName!!
        val request = APIPath.select()
            .where(APIPath.QUALIFIED_NAME.startsWith(connectionQN))
            .includeOnResults(APIPath.NAME)
            .includeOnResults(APIPath.DESCRIPTION)
            .includeOnResults(APIPath.API_PATH_RAW_URI)
            .includeOnResults(APIPath.API_PATH_AVAILABLE_OPERATIONS)
            .includeOnResults(APIPath.API_SPEC)
            .includeOnRelations(APISpec.QUALIFIED_NAME)
            .toRequest()
        val response = retrySearchUntil(request, 13)
        val results = response.stream().toList()
        assertEquals(13, results.size)
        results.forEach {
            val one = it as APIPath
            assertTrue(one.qualifiedName.startsWith(connectionQN))
            assertFalse(one.name.isNullOrBlank())
            assertFalse(one.description.isNullOrBlank())
            assertFalse(one.apiPathRawURI.isNullOrBlank())
            assertFalse(one.apiPathAvailableOperations.isNullOrEmpty())
            assertNotNull(one.apiSpec)
            assertTrue(one.apiSpec is APISpec)
            assertNotNull(one.apiSpec.uniqueAttributes)
            assertTrue(one.apiSpec.uniqueAttributes.qualifiedName.startsWith(connectionQN))
        }
    }

    @Test
    fun filesCreated() {
        validateFilesExist(files)
    }

    @Test
    fun errorFreeLog() {
        validateErrorFreeLog()
    }

    @AfterClass(alwaysRun = true)
    fun afterClass(context: ITestContext) {
        removeConnection(testId, AtlanConnectorType.API)
        teardown(context.failedTests.size() > 0)
    }
}
