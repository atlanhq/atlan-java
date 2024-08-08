/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.typedef.Model
import org.pkl.config.java.ConfigEvaluator
import org.pkl.config.kotlin.forKotlin
import org.pkl.config.kotlin.to
import org.pkl.core.ModuleSource
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object CanonicalExampleTest {

    lateinit var model: Model

    @BeforeClass
    fun modelEval() {
        val source = ModuleSource.path("src/test/resources/CanonicalExample.pkl")
        model = ConfigEvaluator.preconfigured().forKotlin().use { evaluator ->
            evaluator.evaluate(source).to<Model>()
        }
    }

    @Test
    fun testModelBasics() {
        assertNotNull(model)
        assertNotNull(model.t)
        assertNotNull(model.a)
        assertEquals("Custom", model.t)
        assertEquals("custom", model.a)
    }

    @Test
    fun testShared() {
        assertNotNull(model.shared)
        assertNotNull(model.shared.supertypeDefinition)
    }

    @Test
    fun testSupertype() {
        assertEquals("Custom", model.shared.supertypeDefinition.name)
        assertEquals("Base class for all Custom types.", model.shared.supertypeDefinition.description)
        assertNotNull(model.shared.supertypeDefinition.superTypes)
        assertEquals(1, model.shared.supertypeDefinition.superTypes.size)
        assertEquals("Catalog", model.shared.supertypeDefinition.superTypes[0])
        assertNotNull(model.shared.supertypeDefinition.attributeDefs)
        assertEquals(3, model.shared.supertypeDefinition.attributeDefs!!.size)
        assertEquals("customSourceId", model.shared.supertypeDefinition.attributeDefs!![0].name)
        assertEquals("Unique identifier for the Custom asset from the source system.", model.shared.supertypeDefinition.attributeDefs!![0].description)
        assertEquals("string", model.shared.supertypeDefinition.attributeDefs!![0].typeName)
        assertEquals("customDatasetName", model.shared.supertypeDefinition.attributeDefs!![1].name)
        assertEquals("Simple name of the dataset in which this asset exists, or empty if it is itself a dataset.", model.shared.supertypeDefinition.attributeDefs!![1].description)
        assertEquals("string", model.shared.supertypeDefinition.attributeDefs!![1].typeName)
        assertEquals("customDatasetQualifiedName", model.shared.supertypeDefinition.attributeDefs!![2].name)
        assertEquals("Unique name of the dataset in which this asset exists, or empty if it is itself a dataset.", model.shared.supertypeDefinition.attributeDefs!![2].description)
        assertEquals("string", model.shared.supertypeDefinition.attributeDefs!![2].typeName)
    }

    @Test
    fun testCustomTypes() {
        assertNotNull(model.customTypes)
        assertEquals(3, model.customTypes!!.size)
        assertEquals(3, model.customEntityDefs!!.size)
    }

    @Test
    fun testDatasetType() {
        val dataset = model.customEntityDefs!![0]
        assertNotNull(dataset)
        assertEquals("CustomDataset", dataset.name)
        assertEquals("Instances of CustomDataset in Atlan.", dataset.description)
        assertEquals(1, dataset.superTypes.size)
        assertEquals("Custom", dataset.superTypes[0])
        assertTrue(dataset.attributeDefs!!.isEmpty())
    }

    @Test
    fun testTableType() {
        val table = model.customEntityDefs!![1]
        assertNotNull(table)
        assertEquals("CustomTable", table.name)
        assertEquals("Instances of CustomTable in Atlan.", table.description)
        assertEquals(2, table.superTypes.size)
        assertEquals("Custom", table.superTypes[0])
        assertEquals("Table", table.superTypes[1])
        assertEquals(1, table.attributeDefs!!.size)
        assertEquals("customRatings", table.attributeDefs!![0].name)
        assertEquals("Ratings for the CustomTable asset from the source system.", table.attributeDefs!![0].description)
        assertEquals("array<CustomRatings>", table.attributeDefs!![0].typeName)
    }

    @Test
    fun testCustomStructs() {
        assertNotNull(model.customStructDefs)
        assertEquals(1, model.customStructDefs!!.size)
    }

    @Test
    fun testTableStruct() {
        val ratings = model.customStructDefs!![0]
        assertNotNull(ratings)
        assertEquals("CustomRatings", ratings.name)
        assertEquals("Ratings for an asset from the source system.", ratings.description)
        assertEquals(2, ratings.attributeDefs!!.size)
        assertEquals("customRatingFrom", ratings.attributeDefs!![0].name)
        assertEquals("Username of the user who left the rating.", ratings.attributeDefs!![0].description)
        assertEquals("string", ratings.attributeDefs!![0].typeName)
        assertEquals("customRatingOf", ratings.attributeDefs!![1].name)
        assertEquals("Numeric score for the rating left by the user.", ratings.attributeDefs!![1].description)
        assertEquals("long", ratings.attributeDefs!![1].typeName)
    }

    @Test
    fun testFieldType() {
        val field = model.customEntityDefs!![2]
        assertNotNull(field)
        assertEquals("CustomField", field.name)
        assertEquals("Instances of CustomField in Atlan.", field.description)
        assertEquals(2, field.superTypes.size)
        assertEquals("Custom", field.superTypes[0])
        assertEquals("Column", field.superTypes[1])
        assertEquals(1, field.attributeDefs!!.size)
        assertEquals("customTemperature", field.attributeDefs!![0].name)
        assertEquals("Temperature of the CustomTable asset.", field.attributeDefs!![0].description)
        assertEquals("CustomTemperatureType", field.attributeDefs!![0].typeName)
    }

    @Test
    fun testCustomEnums() {
        assertNotNull(model.customEnumDefs)
        assertEquals(1, model.customEnumDefs!!.size)
    }

    @Test
    fun testFieldEnum() {
        val enum = model.customEnumDefs!![0]
        assertNotNull(enum)
        assertEquals("CustomTemperatureType", enum.name)
        assertEquals("Valid values for CustomTable temperatures.", enum.description)
        assertEquals(2, enum.elementDefs.size)
        assertEquals(0, enum.elementDefs[0].ordinal)
        assertEquals("COLD", enum.elementDefs[0].value)
        assertEquals("Lowest availability, can be offline storage.", enum.elementDefs[0].description)
        assertEquals(1, enum.elementDefs[1].ordinal)
        assertEquals("HOT", enum.elementDefs[1].value)
        assertEquals("Highest availability, must be on solid-state or in-memory storage.", enum.elementDefs[1].description)
    }

    @Test
    fun testCustomRelationships() {
        assertNotNull(model.customRelationshipDefs)
        assertEquals(3, model.customRelationshipDefs!!.size)
    }

    @Test
    fun testFieldRelationship() {
        val r1 = model.customRelationshipDefs!![0]
        assertNotNull(r1)
        assertEquals("custom_dataset_custom_tables", r1.name)
        assertEquals("Containment relationship between CustomDataset and CustomTable.", r1.description)
        assertEquals("CustomDataset", r1.endDef1.type)
        assertEquals("CustomTable", r1.endDef2.type)
        assertEquals("customTables", r1.endDef1.name)
        assertEquals("customDataset", r1.endDef2.name)
        assertEquals("CustomTable assets contained within this CustomDataset.", r1.endDef1.description)
        assertEquals("CustomDataset asset containing this CustomTable.", r1.endDef2.description)
        assertTrue(r1.endDef1.isContainer)
        assertFalse(r1.endDef2.isContainer)
        assertEquals(Model.Cardinality.SET, r1.endDef1.cardinality)
        assertEquals(Model.Cardinality.SINGLE, r1.endDef2.cardinality)
        assertEquals(Model.PropagationType.NONE, r1.propagateTags)

        val r2 = model.customRelationshipDefs!![1]
        assertNotNull(r2)
        assertEquals("custom_table_custom_fields", r2.name)
        assertEquals("Containment relationship between CustomTable and CustomField.", r2.description)
        assertEquals("CustomTable", r2.endDef1.type)
        assertEquals("CustomField", r2.endDef2.type)
        assertEquals("customFields", r2.endDef1.name)
        assertEquals("customTable", r2.endDef2.name)
        assertEquals("CustomField assets contained within this CustomTable.", r2.endDef1.description)
        assertEquals("CustomTable asset containing this CustomField.", r2.endDef2.description)
        assertTrue(r2.endDef1.isContainer)
        assertFalse(r2.endDef2.isContainer)
        assertEquals(Model.Cardinality.SET, r2.endDef1.cardinality)
        assertEquals(Model.Cardinality.SINGLE, r2.endDef2.cardinality)
        assertEquals(Model.PropagationType.NONE, r2.propagateTags)

        val r3 = model.customRelationshipDefs!![2]
        assertNotNull(r3)
        assertEquals("custom_from_field_custom_to_field", r3.name)
        assertEquals("Many-to-many peer-to-peer relationship between CustomFields.", r3.description)
        assertEquals("CustomField", r3.endDef1.type)
        assertEquals("CustomField", r3.endDef2.type)
        assertEquals("customToField", r3.endDef1.name)
        assertEquals("customFromField", r3.endDef2.name)
        assertEquals("CustomFields to which this CustomField is related.", r3.endDef1.description)
        assertEquals("CustomFields from which this CustomField is related.", r3.endDef2.description)
        assertFalse(r3.endDef1.isContainer)
        assertFalse(r3.endDef2.isContainer)
        assertEquals(Model.Cardinality.SET, r3.endDef1.cardinality)
        assertEquals(Model.Cardinality.SET, r3.endDef2.cardinality)
        assertEquals(Model.PropagationType.NONE, r3.propagateTags)
    }
}
