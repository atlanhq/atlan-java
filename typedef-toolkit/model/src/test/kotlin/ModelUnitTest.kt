/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import org.pkl.config.java.ConfigEvaluator
import org.pkl.config.kotlin.forKotlin
import org.pkl.config.kotlin.to
import org.pkl.core.ModuleSource
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
        assertEquals("STRING", attr.indexType)
        assertEquals("text", attr.indexTypeESFields?.get("text")?.get("type"))
        assertEquals("atlan_text_analyzer", attr.indexTypeESFields?.get("text")?.get("analyzer"))
        assertTrue(attr.indexTypeESConfig.isNullOrEmpty())
        assertEquals("SINGLE", attr.cardinality)
        assertEquals("string", attr.typeName)
    }

    @Test
    fun enumAttribute() {
        val model = evaluateModel("EnumAttribute")
        assertNotNull(model)
        val attr = getAttribute(model)
        assertEquals("SomeEnumeration", attr.typeName)
        assertEquals("SINGLE", attr.cardinality)
    }

    @Test
    fun multiValuedAttribute() {
        val model = evaluateModel("MultiValuedAttribute")
        assertNotNull(model)
        val attr = getAttribute(model)
        assertEquals("SET", attr.cardinality)
        assertEquals("array<string>", attr.typeName)
    }

    private fun evaluateModel(input: String): CustomAtlanModel {
        val source = ModuleSource.path("src/test/resources/$input.pkl")
        return ConfigEvaluator.preconfigured().forKotlin().use { evaluator ->
            evaluator.evaluate(source).to<CustomAtlanModel>()
        }
    }

    private fun getAttribute(model: CustomAtlanModel): CustomAtlanModel.AttributeDef {
        return model.supertype.groupDefinition.attributeDefs?.get(0)!!
    }
}
