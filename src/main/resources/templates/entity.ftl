/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022- Atlan Pte. Ltd. */
package ${packageRoot}.assets;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.ApiException;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.LogicException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.ApiToken;
import com.atlan.model.core.AssetDeletionResponse;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AssetResponse;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.ConnectionCreationResponse;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanConnectionCategory;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.AtlanDeleteType;
import com.atlan.model.enums.AtlanIcon;
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
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.lineage.FluentLineage;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.search.CompoundQuery;
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
import java.util.concurrent.ThreadLocalRandom;
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
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
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
@ToString(callSuper = true)
@Slf4j
<#if mapContainers?? || className == "Asset">@SuppressWarnings("cast")</#if>
public <#if abstract>abstract</#if> class ${className} extends ${parentClassName} implements <#if className == "TableauCalculatedField" || className == "TableauDatasourceField">ITableauField, </#if>I${className}<#list superTypes as parent>, I${resolveSuperTypeName(parent)}</#list> {
    private static final long serialVersionUID = 2L;

<#if className != "Asset">
    public static final String TYPE_NAME = "${originalName}";
</#if>

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
     * Builds the minimal object necessary to create a relationship to a ${className}, from a potentially
     * more-complete ${className} object.
     *
     * @return the minimal object necessary to relate to the ${className}
     * @throws InvalidRequestException if any of the minimal set of required properties for a ${className} relationship are not found in the initial object
     */
    @Override
    public ${className} trimToReference() throws InvalidRequestException {
        if (this.getGuid() != null && !this.getGuid().isEmpty()) {
            return refByGuid(this.getGuid());
        }
        if (this.getQualifiedName() != null && !this.getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getQualifiedName());
        }
        if (this.getUniqueAttributes() != null
                && this.getUniqueAttributes().getQualifiedName() != null
                && !this.getUniqueAttributes().getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getUniqueAttributes().getQualifiedName());
        }
        throw new InvalidRequestException(
            ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, TYPE_NAME, "guid, qualifiedName");
    }

    /**
     * Start a fluent search that will return all ${className} assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ${className} assets will be included.
     *
     * @return a fluent search that includes all ${className} assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all ${className} assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ${className} assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ${className} assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ${className} assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ${className}s will be included
     * @return a fluent search that includes all ${className} assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all ${className} assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ${className}s will be included
     * @return a fluent search that includes all ${className} assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(CompoundQuery.assetType(TYPE_NAME));
        if (!includeArchived) {
            builder.where(CompoundQuery.ACTIVE);
        }
        return builder;
    }

    /**
     * Start an asset filter that will return all ${className} assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ${className} assets will be included.
     *
     * @return an asset filter that includes all ${className} assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all ${className} assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ${className} assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all ${className} assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all ${className} assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ${className}s will be included
     * @return an asset filter that includes all ${className} assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all ${className} assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ${className}s will be included
     * @return an asset filter that includes all ${className} assets
     * @deprecated replaced by {@link #select(AtlanClient, boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder = AssetFilter.builder()
            .client(client)
            .filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a ${className} by GUID.
     *
     * @param guid the GUID of the ${className} to reference
     * @return reference to a ${className} that can be used for defining a relationship to a ${className}
     */
    public static ${className} refByGuid(String guid) {
        return ${className}._internal().guid(guid).build();
    }

    /**
     * Reference to a ${className} by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the ${className} to reference
     * @return reference to a ${className} that can be used for defining a relationship to a ${className}
     */
    public static ${className} refByQualifiedName(String qualifiedName) {
        return ${className}._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a ${className} by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the ${className} to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ${className}, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ${className} does not exist or the provided GUID is not a ${className}
     */
    @JsonIgnore
    public static ${className} get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a ${className} by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ${className} to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ${className}, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ${className} does not exist or the provided GUID is not a ${className}
     */
    @JsonIgnore
    public static ${className} get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a ${className} by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ${className} to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ${className}, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ${className} does not exist or the provided GUID is not a ${className}
     */
    @JsonIgnore
    public static ${className} get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ${className}) {
                return (${className}) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof ${className}) {
                return (${className}) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ${className} by its GUID, complete with all of its relationships.
     *
     * @param guid of the ${className} to retrieve
     * @return the requested full ${className}, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ${className} does not exist or the provided GUID is not a ${className}
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ${className} retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a ${className} by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the ${className} to retrieve
     * @return the requested full ${className}, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ${className} does not exist or the provided GUID is not a ${className}
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ${className} retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a ${className} by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ${className} to retrieve
     * @return the requested full ${className}, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ${className} does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ${className} retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a ${className} by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the ${className} to retrieve
     * @return the requested full ${className}, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ${className} does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ${className} retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ${className} to active.
     *
     * @param qualifiedName for the ${className}
     * @return true if the ${className} is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ${className} to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ${className}
     * @return true if the ${className} is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
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
        return ${className}._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
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
<#if className != "Glossary" && className != "GlossaryCategory" && className != "GlossaryTerm" && className != "Persona" && className != "Purpose" && className != "AtlanQuery">
    /**
     * Remove the system description from a ${className}.
     *
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a ${className}.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (${className}) Asset.removeDescription(client, updater(qualifiedName, name));
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
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a ${className}.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeUserDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (${className}) Asset.removeUserDescription(client, updater(qualifiedName, name));
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
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a ${className}.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ${className}'s owners
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (${className}) Asset.removeOwners(client, updater(qualifiedName, name));
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
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a ${className}.
     *
     * @param client connectivity to the Atlan tenant on which to update the ${className}'s certificate
     * @param qualifiedName of the ${className}
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ${className}, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ${className} updateCertificate(AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (${className}) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
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
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a ${className}.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ${className}'s certificate
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeCertificate(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (${className}) Asset.removeCertificate(client, updater(qualifiedName, name));
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
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a ${className}.
     *
     * @param client connectivity to the Atlan tenant on which to update the ${className}'s announcement
     * @param qualifiedName of the ${className}
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ${className} updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (${className}) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
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
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a ${className}.
     *
     * @param client connectivity to the Atlan client from which to remove the ${className}'s announcement
     * @param qualifiedName of the ${className}
     * @param name of the ${className}
     * @return the updated ${className}, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ${className} removeAnnouncement(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (${className}) Asset.removeAnnouncement(client, updater(qualifiedName, name));
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
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the ${className}.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ${className}'s assigned terms
     * @param qualifiedName for the ${className}
     * @param name human-readable name of the ${className}
     * @param terms the list of terms to replace on the ${className}, or null to remove all terms from the ${className}
     * @return the ${className} that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ${className} replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (${className}) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
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
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the ${className}, without replacing existing terms linked to the ${className}.
     * Note: this operation must make two API calls — one to retrieve the ${className}'s existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ${className}
     * @param qualifiedName for the ${className}
     * @param terms the list of terms to append to the ${className}
     * @return the ${className} that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ${className} appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (${className}) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
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
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a ${className}, without replacing all existing terms linked to the ${className}.
     * Note: this operation must make two API calls — one to retrieve the ${className}'s existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ${className}
     * @param qualifiedName for the ${className}
     * @param terms the list of terms to remove from the ${className}, which must be referenced by GUID
     * @return the ${className} that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ${className} removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (${className}) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
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
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ${className}, without replacing existing Atlan tags linked to the ${className}.
     * Note: this operation must make two API calls — one to retrieve the ${className}'s existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ${className}
     * @param qualifiedName of the ${className}
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ${className}
     */
    public static ${className} appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (${className}) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
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
        return appendAtlanTags(
            Atlan.getDefaultClient(),
            qualifiedName,
            atlanTagNames,
            propagate,
            removePropagationsOnDelete,
            restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a ${className}, without replacing existing Atlan tags linked to the ${className}.
     * Note: this operation must make two API calls — one to retrieve the ${className}'s existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ${className}
     * @param qualifiedName of the ${className}
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ${className}
     */
    public static ${className} appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (${className}) Asset.appendAtlanTags(
            client,
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
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ${className}.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ${className}
     * @param qualifiedName of the ${className}
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ${className}
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
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
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a ${className}.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ${className}
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
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
                client,
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
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a ${className}.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ${className}
     * @param qualifiedName of the ${className}
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ${className}
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
</#if>
</#if>
}
