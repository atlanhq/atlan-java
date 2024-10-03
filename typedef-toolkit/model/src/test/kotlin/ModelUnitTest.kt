/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import CanonicalExampleTest.model
import com.atlan.typedef.Model
import org.pkl.config.java.ConfigEvaluator
import org.pkl.config.kotlin.forKotlin
import org.pkl.config.kotlin.to
import org.pkl.core.ModuleSource
import org.pkl.core.PklException
import org.testng.Assert.expectThrows
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ModelUnitTest {
    @Test
    fun noIndexNonString() {
        val model = evaluateModel("NoIndexNonString")
        assertNotNull(model)
        val attr = getAttribute(model)
        assertNull(attr.indexType)
        assertTrue(attr.indexTypeESFields.isNullOrEmpty())
        assertTrue(attr.indexTypeESConfig.isNullOrEmpty())
    }

    @Test
    fun noIndexString() {
        val model = evaluateModel("NoIndexString")
        assertNotNull(model)
        val attr = getAttribute(model)
        assertEquals("STRING", attr.indexType)
        assertTrue(attr.indexTypeESFields.isNullOrEmpty())
        assertTrue(attr.indexTypeESConfig.isNullOrEmpty())
    }

    @Test
    fun keywordIndexOnly() {
        val model = evaluateModel("KeywordIndexOnly")
        assertNotNull(model)
        val attr = getAttribute(model)
        assertEquals("STRING", attr.indexType)
        assertTrue(attr.indexTypeESFields.isNullOrEmpty())
        assertTrue(attr.indexTypeESConfig.isNullOrEmpty())
    }

    @Test
    fun textIndexOnly() {
        val model = evaluateModel("TextIndexOnly")
        assertNotNull(model)
        val attr = getAttribute(model)
        assertNull(attr.indexType)
        assertEquals("atlan_text_analyzer", attr.indexTypeESConfig?.get("analyzer"))
    }

    @Test
    fun bothIndexes() {
        val model = evaluateModel("BothIndexes")
        assertNotNull(model)
        val attr = getAttribute(model)
        assertNull(attr.indexType)
        assertEquals("keyword", attr.indexTypeESFields?.get("keyword")?.get("type"))
        assertEquals("atlan_normalizer", attr.indexTypeESFields?.get("keyword")?.get("normalizer"))
        assertEquals("atlan_text_analyzer", attr.indexTypeESConfig?.get("analyzer"))
        assertEquals(Model.Cardinality.SINGLE, attr.cardinality)
        assertEquals("string", attr.typeName)
    }

    @Test
    fun enumAttribute() {
        val model = evaluateModel("EnumAttribute")
        assertNotNull(model)
        val attr = getAttribute(model)
        assertEquals("SomeEnumeration", attr.typeName)
        assertEquals(Model.Cardinality.SINGLE, attr.cardinality)
    }

    @Test
    fun multiValuedAttribute() {
        val model = evaluateModel("MultiValuedAttribute")
        assertNotNull(model)
        val attr = getAttribute(model)
        assertEquals(Model.Cardinality.SET, attr.cardinality)
        assertEquals("array<string>", attr.typeName)
    }

    @Test
    fun multipleSupertypes() {
        val model = evaluateModel("MultipleSupertypes")
        assertNotNull(model)
        assertNotNull(model.customEnumDefs)
        assertEquals(1, model.customEntityDefs?.size)
        val customType = model.customEntityDefs?.get(0)
        assertNotNull(customType)
        assertEquals(2, customType.superTypes.size)
        assertEquals("MultipleSupertypes", customType.superTypes[0])
        assertEquals("Table", customType.superTypes[1])
    }

    @Test
    fun multipleSupertypesRedundant() {
        val model = evaluateModel("MultipleSuperRedundant")
        assertNotNull(model)
        assertNotNull(model.customEnumDefs)
        assertEquals(1, model.customEntityDefs?.size)
        val customType = model.customEntityDefs?.get(0)
        assertNotNull(customType)
        assertEquals(2, customType.superTypes.size)
        assertEquals("MultipleSuperRedundant", customType.superTypes[0])
        assertEquals("Table", customType.superTypes[1])
    }

    @Test
    fun conflictingAttributes() {
        val exception =
            expectThrows(PklException::class.java) {
                evaluateModel("ConflictingAttributes")
            }
        assertNotNull(exception.message)
        assertTrue(exception.message!!.startsWith("–– Pkl Error ––"))
        val lines = exception.message!!.split("\n")
        val errorMsg = lines[1]
        assertEquals(
            "Relationship conflicting_attributes_parent_table_conflicting_attributes_child_tables's endDef1 attribute 'conflictingAttributesChildTables' conflicts with an existing attribute name on type ConflictingAttributesTable.",
            errorMsg,
        )
    }

    private fun evaluateModel(input: String): Model {
        val source = ModuleSource.path("src/test/resources/$input.pkl")
        return ConfigEvaluator.preconfigured().forKotlin().use { evaluator ->
            evaluator.evaluate(source).to<Model>()
        }
    }

    private fun getAttribute(model: Model): Model.AttributeDef {
        return model.shared.supertypeDefinition.attributeDefs!![0]
    }
}
