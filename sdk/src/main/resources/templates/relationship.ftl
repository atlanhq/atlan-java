/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2024- Atlan Pte. Ltd. */
package ${packageRoot}.relations;

import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.${endDef1TypeName};
import com.atlan.model.assets.I${endDef1TypeName};
<#if endDef2AttrName??>
import com.atlan.model.assets.${endDef2TypeName};
import com.atlan.model.assets.I${endDef2TypeName};
</#if>

import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
<#list nonInheritedAttributes as attribute>
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

<#list nonInheritedAttributes as attribute>
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
<#list nonInheritedAttributes as attribute>
        if (${attribute.renamed} != null) {
            map.put("${attribute.renamed}", ${attribute.renamed});
        }
</#list>
        return map;
    }

<#if endDef2AttrName??>
    /** ${description} */
    @Generated(value="${generatorName}")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class ${endDef2AttrName?cap_first} extends ${endDef1TypeName} {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for ${className}. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = ${className}.TYPE_NAME;

        /** Relationship attributes specific to ${className}. */
        ${className} relationshipAttributes;
    }

</#if>
<#if className != "GlossarySemanticAssignment">
    /** ${description} */
    @Generated(value="${generatorName}")
    @Getter
    @SuperBuilder(toBuilder = true, builderMethodName = "_internal")
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class ${endDef1AttrName?cap_first} extends ${endDef2TypeName} {
        private static final long serialVersionUID = 2L;

        /** Fixed typeName for ${className}. */
        @Getter(onMethod_ = {@Override})
        @Builder.Default
        String relationshipType = ${className}.TYPE_NAME;

        /** Relationship attributes specific to ${className}. */
        ${className} relationshipAttributes;
    }
</#if>

    public abstract static class ${className}Builder<
                    C extends ${className}, B extends ${className}Builder<C, B>>
            extends RelationshipAttributes.RelationshipAttributesBuilder<C, B> {

<#if endDef2AttrName??>
        /**
         * Build the ${className} relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public I${endDef1TypeName} ${endDef2AttrName}(I${endDef1TypeName} related, Reference.SaveSemantic semantic) throws InvalidRequestException {
            ${className} attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return ${endDef2AttrName?cap_first}._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return ${endDef2AttrName?cap_first}._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            }
        }

</#if>
<#if className != "GlossarySemanticAssignment">
        /**
         * Build the ${className} relationship (with attributes) into a related object.
         *
         * @param related the related asset to which to build the detailed relationship
         * @param semantic to use for saving the relationship
         * @return a detailed Atlan relationship that conforms to the necessary interface for a related asset
         * @throws InvalidRequestException if the asset provided is without a GUID or qualifiedName
         */
        public I${endDef2TypeName} ${endDef1AttrName}(I${endDef2TypeName} related, Reference.SaveSemantic semantic) throws InvalidRequestException {
            ${className} attributes = build();
            if (related.getGuid() != null && !related.getGuid().isBlank()) {
                return ${endDef1AttrName?cap_first}._internal()
                        .guid(related.getGuid())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            } else {
                return ${endDef1AttrName?cap_first}._internal()
                        .uniqueAttributes(UniqueAttributes.builder()
                                .qualifiedName(related.getQualifiedName())
                                .build())
                        .relationshipAttributes(attributes)
                        .semantic(semantic)
                        .build();
            }
        }
</#if>
    }
}
