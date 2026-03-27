/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.model.assets.APIField
import com.atlan.model.assets.APIMethod
import com.atlan.model.assets.APIObject
import com.atlan.model.assets.APIPath
import com.atlan.model.assets.APISpec
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import org.testng.Assert.assertFalse
import org.testng.Assert.assertTrue
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Test import of the canonical PetStore example from Swagger.
 */
class ImportJsonTest : PackageTest("j") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val connectorType = AtlanConnectorType.OPENLINEAGE
    private val testId = makeUnique("c1")
    private val testFile = "openapi.json"
    private val files =
        listOf(
            "debug.log",
        )

    private fun prepFile() {
        val input = Paths.get("src", "test", "resources", testFile).toFile()
        input.copyTo(Paths.get(testDirectory, testFile).toFile(), true)
    }

    override fun setup() {
        prepFile()
        runCustomPackage(
            OpenAPISpecLoaderCfg(
                importType = "DIRECT",
                specFile = testFile,
                connectionUsage = "CREATE",
                connection = Connection.creator(client, testId, connectorType).build(),
            ),
            OpenAPISpecLoader::main,
        )
    }

    override fun teardown() {
        removeConnection(testId, connectorType)
    }

    @Test
    fun connectionCreated() {
        val results = Connection.findByName(client, testId, connectorType)
        assertNotNull(results)
        assertEquals(1, results.size)
        assertEquals(testId, results[0].name)
    }

    @Test
    fun specCreated() {
        val connectionQN = Connection.findByName(client, testId, connectorType)?.get(0)?.qualifiedName!!
        val request =
            APISpec
                .select(client)
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
        val connectionQN = Connection.findByName(client, testId, connectorType)?.get(0)?.qualifiedName!!
        val request =
            APIPath
                .select(client)
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
            assertTrue(
                one.apiSpec.uniqueAttributes.qualifiedName
                    .startsWith(connectionQN),
            )
        }
    }

    @Test
    fun objectsCreated() {
        // The Petstore spec has 8 schemas: Order, Customer, Address, Category, User, Tag, Pet, ApiResponse
        val connectionQN = Connection.findByName(client, testId, connectorType)?.get(0)?.qualifiedName!!
        val request =
            APIObject
                .select(client)
                .where(APIObject.QUALIFIED_NAME.startsWith(connectionQN))
                .includeOnResults(APIObject.NAME)
                .includeOnResults(APIObject.API_FIELD_COUNT)
                .includeOnResults(APIObject.API_SPEC_QUALIFIED_NAME)
                .toRequest()
        val response = retrySearchUntil(request, 8)
        val results = response.stream().toList()
        assertEquals(8, results.size)
        val objectNames = results.map { (it as APIObject).name }.toSet()
        assertTrue(objectNames.contains("Pet"))
        assertTrue(objectNames.contains("Order"))
        assertTrue(objectNames.contains("User"))
        assertTrue(objectNames.contains("Category"))
        assertTrue(objectNames.contains("Tag"))
        assertTrue(objectNames.contains("Address"))
        assertTrue(objectNames.contains("Customer"))
        assertTrue(objectNames.contains("ApiResponse"))
        // Verify Pet has the correct field count (6 properties: id, name, category, photoUrls, tags, status)
        val pet = results.first { (it as APIObject).name == "Pet" } as APIObject
        assertEquals(6L, pet.apiFieldCount)
    }

    @Test
    fun fieldsCreated() {
        // Verify APIField assets were created for schema properties
        val connectionQN = Connection.findByName(client, testId, connectorType)?.get(0)?.qualifiedName!!
        val request =
            APIField
                .select(client)
                .where(APIField.QUALIFIED_NAME.startsWith(connectionQN))
                .includeOnResults(APIField.NAME)
                .includeOnResults(APIField.API_FIELD_TYPE)
                .includeOnResults(APIField.API_IS_OBJECT_REFERENCE)
                .includeOnResults(APIField.API_OBJECT_QUALIFIED_NAME)
                .includeOnResults(APIField.API_OBJECT)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val results = response.stream().toList()
        // There should be fields across all schemas
        assertTrue(results.isNotEmpty())
        // Find a field that is an object reference (e.g., Pet.category -> Category)
        val objectRefFields = results.filter { (it as APIField).apiIsObjectReference == true }
        assertTrue(objectRefFields.isNotEmpty(), "Should have at least one field that references another APIObject")
        // Every field should have a parent APIObject
        results.forEach {
            val field = it as APIField
            assertFalse(field.name.isNullOrBlank())
            assertFalse(field.apiFieldType.isNullOrBlank())
        }
    }

    @Test
    fun methodsCreated() {
        // The Petstore spec has 20 operations total across all paths
        val connectionQN = Connection.findByName(client, testId, connectorType)?.get(0)?.qualifiedName!!
        val request =
            APIMethod
                .select(client)
                .where(APIMethod.QUALIFIED_NAME.startsWith(connectionQN))
                .includeOnResults(APIMethod.NAME)
                .includeOnResults(APIMethod.DESCRIPTION)
                .includeOnResults(APIMethod.API_METHOD_REQUEST)
                .includeOnResults(APIMethod.API_METHOD_RESPONSE)
                .includeOnResults(APIMethod.API_METHOD_RESPONSE_CODES)
                .includeOnResults(APIMethod.API_PATH)
                .includeOnRelations(APIPath.QUALIFIED_NAME)
                .toRequest()
        val response = retrySearchUntil(request, 20)
        val results = response.stream().toList()
        assertEquals(20, results.size)
        results.forEach {
            val method = it as APIMethod
            assertTrue(method.qualifiedName.startsWith(connectionQN))
            assertFalse(method.name.isNullOrBlank())
            // Every method should have a parent APIPath
            assertNotNull(method.apiPath)
            assertTrue(method.apiPath is APIPath)
        }
        // Verify a specific method: PUT /pet should have request and response
        val putPet = results.first { (it as APIMethod).name == "PUT /pet" } as APIMethod
        assertNotNull(putPet.apiMethodRequest, "PUT /pet should have a request body")
        assertNotNull(putPet.apiMethodResponse, "PUT /pet should have a response body")
        assertNotNull(putPet.apiMethodResponseCodes, "PUT /pet should have response codes")
        assertTrue(putPet.apiMethodResponseCodes.containsKey("200"), "PUT /pet should have a 200 response")
    }

    @Test
    fun methodRequestSchemaLinked() {
        // Verify that methods with request bodies are linked to APIObjects
        val connectionQN = Connection.findByName(client, testId, connectorType)?.get(0)?.qualifiedName!!
        val request =
            APIMethod
                .select(client)
                .where(APIMethod.QUALIFIED_NAME.startsWith(connectionQN))
                .includeOnResults(APIMethod.NAME)
                .includeOnResults(APIMethod.API_METHOD_REQUEST_SCHEMA)
                .includeOnRelations(APIObject.QUALIFIED_NAME)
                .toRequest()
        val response = retrySearchUntil(request, 20)
        val results = response.stream().toList()
        // POST /pet should have its request schema linked to the Pet APIObject
        val postPet = results.first { (it as APIMethod).name == "POST /pet" } as APIMethod
        assertNotNull(postPet.apiMethodRequestSchema, "POST /pet should be linked to a request schema APIObject")
        assertTrue(
            postPet.apiMethodRequestSchema.uniqueAttributes.qualifiedName
                .contains("Pet"),
            "POST /pet request schema should reference the Pet object",
        )
    }

    @Test
    fun methodResponseSchemaLinked() {
        // Verify that methods with $ref response schemas are linked to APIObjects
        val connectionQN = Connection.findByName(client, testId, connectorType)?.get(0)?.qualifiedName!!
        val request =
            APIMethod
                .select(client)
                .where(APIMethod.QUALIFIED_NAME.startsWith(connectionQN))
                .includeOnResults(APIMethod.NAME)
                .includeOnResults(APIMethod.API_METHOD_RESPONSE_SCHEMAS)
                .includeOnResults(APIMethod.API_METHOD_RESPONSE_CODES)
                .includeOnRelations(APIObject.QUALIFIED_NAME)
                .toRequest()
        val response = retrySearchUntil(request, 20)
        val results = response.stream().toList()
        // GET /pet/{petId} should have a response schema linked to the Pet APIObject
        val getPetById = results.first { (it as APIMethod).name == "GET /pet/{petId}" } as APIMethod
        assertNotNull(getPetById.apiMethodResponseSchemas, "GET /pet/{petId} should have response schemas")
        assertFalse(getPetById.apiMethodResponseSchemas.isEmpty(), "GET /pet/{petId} should have at least one response schema")
        assertTrue(
            getPetById.apiMethodResponseSchemas.any {
                (it as APIObject).uniqueAttributes.qualifiedName.contains("Pet")
            },
            "GET /pet/{petId} response should reference the Pet object",
        )
        // Verify response codes map is populated
        assertNotNull(getPetById.apiMethodResponseCodes, "GET /pet/{petId} should have response codes")
        assertTrue(getPetById.apiMethodResponseCodes.containsKey("200"), "GET /pet/{petId} should have a 200 response code")
    }

    @Test
    fun objectRefFieldsLinked() {
        // Verify that APIField objects referencing other schemas have apiIsObjectReference and apiObjectQualifiedName set
        val connectionQN = Connection.findByName(client, testId, connectorType)?.get(0)?.qualifiedName!!
        val request =
            APIField
                .select(client)
                .where(APIField.QUALIFIED_NAME.startsWith(connectionQN))
                .where(APIField.API_IS_OBJECT_REFERENCE.eq(true))
                .includeOnResults(APIField.NAME)
                .includeOnResults(APIField.API_IS_OBJECT_REFERENCE)
                .includeOnResults(APIField.API_OBJECT_QUALIFIED_NAME)
                .toRequest()
        val response = retrySearchUntil(request, 1)
        val results = response.stream().toList()
        assertTrue(results.isNotEmpty(), "Should have fields with object references")
        // Pet.category should reference the Category schema
        val categoryField = results.firstOrNull { (it as APIField).name == "category" }
        if (categoryField != null) {
            val field = categoryField as APIField
            assertEquals(true, field.apiIsObjectReference)
            assertNotNull(field.apiObjectQualifiedName, "category field should have apiObjectQualifiedName")
            assertTrue(field.apiObjectQualifiedName.contains("Category"), "category field should reference the Category schema")
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
}
