/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import com.atlan.model.enums.AtlanCustomAttributePrimitiveType
import com.atlan.model.typedefs.AttributeDef
import com.atlan.model.typedefs.CustomMetadataDef
import com.atlan.model.typedefs.EnumDef
import com.atlan.pkg.PackageTest
import mu.KotlinLogging
import org.testng.annotations.Test
import kotlin.test.assertEquals

private const val ONE = "one"
private const val TWO = "two"
private const val THREE = "three"

private const val FOUR = "four"
private const val ATTR1 = "An Option"
private const val ATTR2 = "A String"
private const val TAG_KEY_OPTION = "someone"
private const val TAG_KEY_STRING = "other"
private val ORIGINAL_VALUES = setOf(ONE, FOUR)

class EnumCreatorTest : PackageTest("en") {
    override val logger = KotlinLogging.logger {}
    private val enum1 = makeUnique("enum")
    private val cm1 = makeUnique("cm")
    private val tagToMetadataMapper =
        TagToMetadataMapper(
            mapOf(
                TAG_KEY_OPTION to "$cm1::$ATTR1",
                TAG_KEY_STRING to "$cm1::$ATTR2",
            ),
        )
    private val enumCreator = EnumCreator(client, tagToMetadataMapper)
    private val enumCache = client.enumCache

    private fun createEnum() {
        val enumDef =
            EnumDef.creator(
                enum1,
                listOf(ONE, FOUR),
            )
                .build()
        enumDef.create(client)
    }

    private fun createCustomMetadata() {
        CustomMetadataDef.creator(cm1)
            .attributeDef(AttributeDef.of(client, ATTR1, AtlanCustomAttributePrimitiveType.OPTIONS, enum1, false))
            .attributeDef(AttributeDef.of(client, ATTR2, AtlanCustomAttributePrimitiveType.STRING, enum1, false))
            .build()
            .create(client)
    }

    override fun setup() {
        createEnum()
        createCustomMetadata()
    }

    @Test
    fun whenAllValuesExistThenNoChange() {
        enumCreator.createOptions(TAG_KEY_OPTION, ORIGINAL_VALUES)
        assertEnumValuesEqual(ORIGINAL_VALUES)
    }

    @Test
    fun whenValuesAddedThenValueAdded() {
        enumCreator.createOptions(TAG_KEY_OPTION, setOf(TWO, THREE))
        assertEnumValuesEqual(setOf(ONE, TWO, THREE, FOUR))
    }

    @Test
    fun whenAttributeIsNotOptionThenNoChange() {
        enumCreator.createOptions(TAG_KEY_STRING, setOf(TWO, THREE))
        assertEnumValuesEqual(ORIGINAL_VALUES)
    }

    @Test
    fun whenAttributeIsNotFoundThenNoChange() {
        enumCreator.createOptions("bogus", setOf(TWO, THREE))
        assertEnumValuesEqual(ORIGINAL_VALUES)
    }

    private fun assertEnumValuesEqual(expected: Set<String>) {
        enumCache.refreshCache()
        val enum = enumCache.getByName(enum1)
        val newValues = enum.elementDefs.map { it.value }.toSet()
        assertEquals(expected, newValues)
    }

    override fun teardown() {
        client.typeDefs.purge(client.customMetadataCache.getSidForName(cm1))
        EnumDef.purge(client, enum1)
    }
}
