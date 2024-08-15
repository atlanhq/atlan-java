/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2024- Atlan Pte. Ltd. */
package ${packageRoot}.relations;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
<#list classAttributes as attribute>
<#if attribute.type.type == "ENUM">
<#if isBuiltIn(attribute.type.originalBase, attribute.type.name)>
import com.atlan.model.enums.${attribute.type.name};
<#else>
import ${packageRoot}.enums.${attribute.type.name};
</#if>
<#elseif attribute.type.type == "STRUCT">
<#if isBuiltIn(attribute.type.originalBase, attribute.type.name)>
import com.atlan.model.structs.${attribute.type.name};
<#else>
import ${packageRoot}.structs.${attribute.type.name};
</#if>
<#elseif attribute.type.type == "ASSET">
<#if isBuiltIn(attribute.type.originalBase, attribute.type.name)>
import com.atlan.model.assets.I${attribute.type.name};
<#else>
import ${packageRoot}.assets.I${attribute.type.name};
</#if>
</#if>
</#list>


<#if classTemplateFile??>
<#import classTemplateFile as methods>
<#if methods.imports??>
<@methods.imports/>
</#if>
</#if>

import javax.annotation.processing.Generated;

/**
 * ${description}
 */
@Generated(value="${generatorName}")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
<#if mapContainers??>@SuppressWarnings("cast")</#if>
public class ${className} extends RelationshipAttributes {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "${originalName}";

    /** Fixed typeName for ${className}s. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

<#list classAttributes as attribute>
    /** ${attribute.description} */
    <#if attribute.date>@Date</#if>
    <#if attribute.singular??>@Singular<#if attribute.singular?has_content>("${attribute.singular}")</#if></#if>
    <#if className == "GlossaryCategory" && attribute.renamed == "childrenCategories">@Setter(AccessLevel.PACKAGE)</#if>
    <#if attribute.renamed != attribute.originalName>
    @JsonProperty("${attribute.originalName}")
    </#if>
    ${attribute.referenceType} ${attribute.renamed};

</#list>

    /** {@inheritDoc} */
    @Override
    public Map<String, Object> getAll() {
        Map<String, Object> map = new HashMap<>();
<#list classAttributes as attribute>
        if (${attribute.renamed} != null) {
            map.put("${attribute.renamed}", ${attribute.renamed});
        }
</#list>
        return map;
    }

    /** ${description} */
    @Generated(value="${generatorName}")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class ${endDef1.attrName} extends ${endDef1.typeName} {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for ${className}. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = ${className}.TYPE_NAME;

        /** Relationship attributes specific to ${className}. */
        ${className} relationshipAttributes;
    }

    public abstract static class ${className}Builder<
                    C extends ${className}, B extends ${className}Builder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

        /**
         * Build the ${className} relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public I${endDef1.typeName} build(I${endDef1.typeName} related) throws InvalidRequestException {
            ${className} attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return ${endDef1.attrName}._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .build();
            } else {
                return ${endDef1.attrName}._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .build();
            }
        }
    }
}
