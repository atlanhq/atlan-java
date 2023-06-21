/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package ${packageRoot}.assets;

import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.enums.AtlanPolicyAction;
import com.atlan.model.enums.AuthPolicyCategory;
import com.atlan.model.enums.AuthPolicyResourceCategory;
import com.atlan.model.enums.AuthPolicyType;
import com.atlan.model.enums.PersonaMetadataAction;
import com.atlan.model.enums.PersonaGlossaryAction;
import com.atlan.model.enums.PurposeMetadataAction;
import com.atlan.model.enums.DataAction;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.enums.KeywordFields;
import com.atlan.model.relations.UniqueAttributes;
<#list interfaceAttributes as attribute>
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
import com.atlan.serde.AssetDeserializer;
import com.atlan.serde.AssetSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.annotation.processing.Generated;

/**
 * ${description}
 */
@Generated(value="${generatorName}")
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
public interface I${className} {

    public static final String TYPE_NAME = "${originalName}";

<#if interfaceTemplateFile??>
<#import interfaceTemplateFile as methods>
<@methods.all/>
</#if>

    <#list interfaceAttributes as attribute>
    /** ${attribute.description} */
    ${attribute.referenceType} get${attribute.renamed?cap_first}();

    </#list>
    /** Name of the type that defines the asset. */
    String getTypeName();

    /** Globally-unique identifier for the asset. */
    String getGuid();

    /** Human-readable name of the asset. */
    String getDisplayText();

    /** Status of the asset (if this is a related asset). */
    String getEntityStatus();

    /** Type of the relationship (if this is a related asset). */
    String getRelationshipType();

    /** Unique identifier of the relationship (when this is a related asset). */
    String getRelationshipGuid();

    /** Status of the relationship (when this is a related asset). */
    AtlanStatus getRelationshipStatus();

    /** Attributes specific to the relationship (unused). */
    Map<String, Object> getRelationshipAttributes();

    /**
     * Attribute(s) that uniquely identify the asset (when this is a related asset).
     * If the guid is not provided, these must be provided.
     */
    UniqueAttributes getUniqueAttributes();

    /**
     * When true, indicates that this object represents a complete view of the entity.
     * When false, this object is only a reference or some partial view of the entity.
     */
    boolean isComplete();

    /**
     * Indicates whether this object can be used as a valid reference by GUID.
     * @return true if it is a valid GUID reference, false otherwise
     */
    boolean isValidReferenceByGuid();

    /**
     * Indicates whether this object can be used as a valid reference by qualifiedName.
     * @return true if it is a valid qualifiedName reference, false otherwise
     */
    boolean isValidReferenceByQualifiedName();
}
