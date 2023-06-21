/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022- Atlan Pte. Ltd. */
package ${packageRoot}.assets;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.api.EntityBulkEndpoint;
import com.atlan.cache.CustomMetadataCache;
import com.atlan.cache.GroupCache;
import com.atlan.cache.RoleCache;
import com.atlan.cache.UserCache;
import com.atlan.exception.ApiException;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.LogicException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetDeletionResponse;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AssetResponse;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.ConnectionCreationResponse;
import com.atlan.model.core.CustomMetadataAttributes;
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
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.StringUtils;
import com.atlan.util.QueryFactory;
<#if className == "Asset">
import com.atlan.Atlan;
import com.atlan.api.EntityBulkEndpoint;
import com.atlan.api.EntityGuidEndpoint;
import com.atlan.api.EntityUniqueAttributesEndpoint;
import com.atlan.model.relations.Reference;
import com.atlan.net.HttpClient;
import com.atlan.serde.AssetDeserializer;
import com.atlan.serde.AssetSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
</#if>
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import com.atlan.model.assets.Attribute;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.IGlossaryTerm;
<#list superTypes as parent>
<#if isBuiltIn(parent, parent)>
import com.atlan.model.assets.I${resolveSuperTypeName(parent)};
</#if>
</#list>

import javax.annotation.processing.Generated;

/**
 * ${description}
 */
@Generated(value="${generatorName}")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
<#if className == "Asset">
@JsonSerialize(using = AssetSerializer.class)
@JsonDeserialize(using = AssetDeserializer.class)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "typeName",
        defaultImpl = IndistinctAsset.class)
</#if>
@Slf4j
<#if mapContainers?? || className == "Asset">@SuppressWarnings("cast")</#if>
public <#if abstract>abstract</#if> class ${className} extends ${parentClassName} implements <#if className == "TableauCalculatedField" || className == "TableauDatasourceField">ITableauField, </#if>I${className}<#list superTypes as parent>, I${resolveSuperTypeName(parent)}</#list> {
<#if !abstract>    private static final long serialVersionUID = 2L;</#if>

    public static final String TYPE_NAME = "${originalName}";

<#if !abstract>
    /** Fixed typeName for ${className}s. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;
</#if>

<#list classAttributes as attribute>
    /** ${attribute.description} */
    @Attribute
    <#if attribute.singular??>@Singular<#if attribute.singular?has_content>("${attribute.singular}")</#if></#if>
    <#if className == "GlossaryCategory" && attribute.renamed == "childrenCategories">@Setter(AccessLevel.PACKAGE)</#if>
    <#if attribute.renamed != attribute.originalName>
    @JsonProperty("${attribute.originalName}")
    </#if>
    ${attribute.referenceType} ${attribute.renamed};

</#list>
<#if !abstract>
    /**
     * Reference to a ${className} by GUID.
     *
     * @param guid the GUID of the ${className} to reference
     * @return reference to a ${className} that can be used for defining a relationship to a ${className}
     */
    public static ${className} refByGuid(String guid) {
        return ${className}.builder().guid(guid).build();
    }

    /**
     * Reference to a ${className} by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ${className} to reference
     * @return reference to a ${className} that can be used for defining a relationship to a ${className}
     */
    public static ${className} refByQualifiedName(String qualifiedName) {
        return ${className}.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a ${className} by its GUID, complete with all of its relationships.
     *
     * @param guid of the ${className} to retrieve
     * @return the requested full ${className}, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ${className} does not exist or the provided GUID is not a ${className}
     */
    public static ${className} retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof ${className}) {
            return (${className}) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "${className}");
        }
    }

    /**
     * Retrieves a ${className} by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ${className} to retrieve
     * @return the requested full ${className}, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ${className} does not exist
     */
    public static ${className} retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof ${className}) {
            return (${className}) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "${className}");
        }
    }

    /**
     * Restore the archived (soft-deleted) ${className} to active.
     *
     * @param qualifiedName for the ${className}
     * @return true if the ${className} is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

</#if>
<#if classTemplateFile??>
<#import classTemplateFile as methods>
<@methods.all/>
<#elseif !abstract>
    /**
     * Builds the minimal object necessary to update a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @return the minimal request necessary to update the ${className}, as a builder
     */
    public static ${className}Builder<?, ?> updater(String qualifiedName, String name) {
        return ${className}.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ${className}, from a potentially
     * more-complete ${className} object.
     *
     * @return the minimal object necessary to update the ${className}, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ${className} are not found in the initial object
     */
    @Override
    public ${className}Builder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "${className}", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
</#if>

<#if !abstract>
<#if className != "Glossary" && className != "GlossaryCategory" && className != "GlossaryTerm" && className != "Persona" && className != "Purpose">
    /**
     * Remove the system description from a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeDescription(String qualifiedName, String name) throws AtlanException {
        return (${className}) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (${className}) Asset.removeUserDescription(updater(qualifiedName, name));
    }

<#if className != "Readme" && className != "Link" && className != "ReadmeTemplate" && className != "Badge">
    /**
     * Remove the owners from a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeOwners(String qualifiedName, String name) throws AtlanException {
        return (${className}) Asset.removeOwners(updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ${className}, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ${className} updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (${className}) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (${className}) Asset.removeCertificate(updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ${className} updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (${className}) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (${className}) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ${className}.
     *
     * @param qualifiedName for the ${className}
     * @param name human-readable name of the ${className}
     * @param terms the list of terms to replace on the ${className}, or null to remove all terms from the ${className}
     * @return the ${className} that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ${className} replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (${className}) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ${className}, without replacing existing terms linked to the ${className}.
     * Note: this operation must make two API calls — one to retrieve the ${className}'s existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ${className}
     * @param terms the list of terms to append to the ${className}
     * @return the ${className} that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ${className} appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (${className}) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ${className}, without replacing all existing terms linked to the ${className}.
     * Note: this operation must make two API calls — one to retrieve the ${className}'s existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ${className}
     * @param terms the list of terms to remove from the ${className}, which must be referenced by GUID
     * @return the ${className} that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ${className} removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (${className}) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
</#if>
</#if>

<#if className != "Readme" && className != "Link" && className != "ReadmeTemplate" && className != "Badge">
    /**
     * Add Atlan tags to a ${className}, without replacing existing Atlan tags linked to the ${className}.
     * Note: this operation must make two API calls — one to retrieve the ${className}'s existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ${className}
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ${className}
     */
    public static ${className} appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (${className}) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ${className}, without replacing existing Atlan tags linked to the ${className}.
     * Note: this operation must make two API calls — one to retrieve the ${className}'s existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ${className}
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ${className}
     */
    public static ${className} appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (${className}) Asset.appendAtlanTags(
            TYPE_NAME,
            qualifiedName,
            atlanTagNames,
            propagate,
            removePropagationsOnDelete,
            restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ${className}
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ${className}
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ${className}
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
</#if>
</#if>
}
