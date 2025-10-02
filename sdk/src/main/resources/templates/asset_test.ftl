/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022- Atlan Pte. Ltd. */
package ${packageRoot}.assets;

import static org.testng.Assert.*;

import com.atlan.mock.MockAtlanTenant;
import com.atlan.model.assets.Meaning;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.*;
import com.atlan.model.structs.*;
import ${packageRoot}.enums.*;
import ${packageRoot}.structs.*;
import java.io.IOException;
import java.util.*;
import javax.annotation.processing.Generated;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Generated(value="${generatorName}")
@SuppressWarnings("deprecation")
public class ${className}Test {

    private final ${className} full = ${className}._internal()
            .guid("guid")
            .displayText("displayText")
            .status(AtlanStatus.ACTIVE)
            .createdBy("createdBy")
            .updatedBy("updatedBy")
            .createTime(123456789L)
            .updateTime(123456789L)
            .isIncomplete(false)
            .deleteHandler("SOFT")
            .meaningNames(Set.of("meaningName1", "meaningName2"))
            .meanings(Set.of(
                Meaning.builder()
                    .termGuid("termGuid1")
                    .relationGuid("relationGuid1")
                    .displayText("displayText1")
                    .confidence(100)
                    .build(),
                Meaning.builder()
                    .termGuid("termGuid2")
                    .relationGuid("relationGuid2")
                    .displayText("displayText2")
                    .confidence(100)
                    .build()))
            .qualifiedName("qualifiedName")
            .atlanTag(AtlanTag.of("String0"))
            .atlanTag(AtlanTag.builder().typeName("String1").propagate(false).build())
            .customMetadata("String0", CustomMetadataAttributes.builder()
                    .attribute("String0", 123.456)
                    .attribute("String1", true)
                    .build())
            .customMetadata("String1", CustomMetadataAttributes.builder()
                // Note: for equivalency this MUST be a Long (not an Integer), as deserialization
                // will always produce a Long
                    .attribute("String0", 789L)
                    .attribute("String1", "AnotherString")
                    .build())
<#list testAttributes as testAttribute>
    <#list testAttribute.values as value>
        <#if className == "CustomEntity" && testAttribute.builderMethod == "assetIcon">
            .iconUrl("http://example.com/example-image.png")
        <#else>
            .${testAttribute.builderMethod}(${value})
        </#if>
    </#list>
</#list>
            .build();

    @BeforeClass
    void init() throws InterruptedException {
        MockAtlanTenant.initializeClient();
    }

    @Test
    void serdeCycle${className}() throws IOException {
        assertNotNull(full, "Unable to build sample instance of ${className},");
        final int hash = full.hashCode();
        // Builder equivalency
        assertEquals(full.toBuilder().build(), full, "Unable to converting ${className} via builder back to its original state,");
        // Serialization
        final String serialized = full.toJson(MockAtlanTenant.client);
        assertNotNull(serialized, "Unable to serialize sample instance of ${className},");
        assertEquals(full.hashCode(), hash, "Serialization mutated the original value,");
        // Deserialization
        final ${className} frodo = MockAtlanTenant.client.readValue(serialized, ${className}.class);
        assertNotNull(frodo, "Unable to reverse-read serialized value back into an instance of ${className},");
        // Serialized equivalency
        String backAgain = frodo.toJson(MockAtlanTenant.client);
        assertEquals(backAgain, serialized, "Serialization is not equivalent after serde loop,");
        // Deserialized equivalency
        assertEquals(frodo, full, "Deserialization is not equivalent after serde loop,");
    }
}
