/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.pkg.lftag

import org.testng.annotations.Test
import kotlin.test.assertEquals

private const val BUSINESS_DOMAIN = "business_domain"

class TagToMetadataMapperTest {
    @Test
    fun whenTagKeyFoundGetSetAndAttributeNamesReturnsNames() {
        val tagToMetadataMapper = TagToMetadataMapper(mapOf(BUSINESS_DOMAIN to "Data Domain::Business Domain"))
        val (setName, attributeName) = tagToMetadataMapper.getSetAndAttributeNames(BUSINESS_DOMAIN)
        assertEquals("Data Domain", setName)
        assertEquals("Business Domain", attributeName)
    }

    @Test
    fun whenTagKeyNotFoundGetSetAndAttributeNamesReturnsEmptyStrings() {
        val tagToMetadataMapper = TagToMetadataMapper(mapOf(BUSINESS_DOMAIN to "Data Domain::Business Domain"))
        val (setName, attributeName) = tagToMetadataMapper.getSetAndAttributeNames("bogus")
        assertEquals("", setName)
        assertEquals("", attributeName)
    }
}
