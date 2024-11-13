/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022- Atlan Pte. Ltd. */
package com.atlan.model.assets;

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
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AssetResponse;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.AsyncCreationResponse;
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
import com.atlan.model.enums.PersonaDomainAction;
import com.atlan.model.enums.DataAction;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.mesh.DataProductAssetsDSL;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.lineage.FluentLineage;
import com.atlan.model.relations.Reference;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.assets.IApplicationAsset;
import com.atlan.model.assets.ICatalog;
import com.atlan.model.assets.IAirflowTask;
import com.atlan.model.assets.ILineageProcess;
import com.atlan.model.assets.ISparkJob;
import com.atlan.model.assets.IAirflowTask;
import com.atlan.model.assets.ILineageProcess;
import com.atlan.model.assets.ISparkJob;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
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
import java.util.stream.Collectors;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import com.atlan.model.assets.Attribute;
import com.atlan.model.assets.Date;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.IGlossaryTerm;
import com.atlan.model.assets.IApplication;
import com.atlan.model.assets.ICatalog;
import com.atlan.model.assets.IAsset;
import com.atlan.model.assets.IReferenceable;

import javax.annotation.processing.Generated;

/**
 * Instances of ApplicationAsset in Atlan.
 */
@Generated(value="com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j

public  class ApplicationAsset extends Asset implements IApplicationAsset, IApplication, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ApplicationAsset";

    /** Fixed typeName for ApplicationAssets. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** ApplicationAsset asset containing this Catalog asset. */
    @Attribute



    IApplicationAsset applicationAsset;

    /** Qualified name of the Application Asset that contains this asset. */
    @Attribute



    String applicationAssetQualifiedName;

    /** Catalog assets contained within this ApplicationAsset. */
    @Attribute

    @Singular("applicationCatalog")

    SortedSet<ICatalog> applicationCatalog;

    /** Unique identifier for the Application asset from the source system. */
    @Attribute



    String applicationId;

    /** Tasks to which this asset provides input. */
    @Attribute

    @Singular

    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** Processes to which this asset provides input. */
    @Attribute

    @Singular

    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute

    @Singular

    SortedSet<ISparkJob> inputToSparkJobs;

    /** Tasks from which this asset is output. */
    @Attribute

    @Singular

    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** Processes from which this asset is produced as output. */
    @Attribute

    @Singular

    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute

    @Singular

    SortedSet<ISparkJob> outputFromSparkJobs;

    /**
     * Builds the minimal object necessary to create a relationship to a ApplicationAsset, from a potentially
     * more-complete ApplicationAsset object.
     *
     * @return the minimal object necessary to relate to the ApplicationAsset
     * @throws InvalidRequestException if any of the minimal set of required properties for a ApplicationAsset relationship are not found in the initial object
     */
    @Override
    public ApplicationAsset trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ApplicationAsset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ApplicationAsset assets will be included.
     *
     * @return a fluent search that includes all ApplicationAsset assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all ApplicationAsset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ApplicationAsset assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ApplicationAsset assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ApplicationAsset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ApplicationAssets will be included
     * @return a fluent search that includes all ApplicationAsset assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all ApplicationAsset assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ApplicationAssets will be included
     * @return a fluent search that includes all ApplicationAsset assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(Asset.TYPE_NAME.eq(TYPE_NAME));
        if (!includeArchived) {
            builder.active();
        }
        return builder;
    }

    /**
     * Reference to a ApplicationAsset by GUID. Use this to create a relationship to this ApplicationAsset,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ApplicationAsset to reference
     * @return reference to a ApplicationAsset that can be used for defining a relationship to a ApplicationAsset
     */
    public static ApplicationAsset refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ApplicationAsset by GUID. Use this to create a relationship to this ApplicationAsset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ApplicationAsset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ApplicationAsset that can be used for defining a relationship to a ApplicationAsset
     */
    public static ApplicationAsset refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ApplicationAsset._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ApplicationAsset by qualifiedName. Use this to create a relationship to this ApplicationAsset,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ApplicationAsset to reference
     * @return reference to a ApplicationAsset that can be used for defining a relationship to a ApplicationAsset
     */
    public static ApplicationAsset refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ApplicationAsset by qualifiedName. Use this to create a relationship to this ApplicationAsset,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ApplicationAsset to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ApplicationAsset that can be used for defining a relationship to a ApplicationAsset
     */
    public static ApplicationAsset refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ApplicationAsset._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ApplicationAsset by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the ApplicationAsset to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ApplicationAsset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ApplicationAsset does not exist or the provided GUID is not a ApplicationAsset
     */
    @JsonIgnore
    public static ApplicationAsset get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a ApplicationAsset by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ApplicationAsset to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ApplicationAsset, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ApplicationAsset does not exist or the provided GUID is not a ApplicationAsset
     */
    @JsonIgnore
    public static ApplicationAsset get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a ApplicationAsset by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ApplicationAsset to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ApplicationAsset, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ApplicationAsset does not exist or the provided GUID is not a ApplicationAsset
     */
    @JsonIgnore
    public static ApplicationAsset get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ApplicationAsset) {
                return (ApplicationAsset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof ApplicationAsset) {
                return (ApplicationAsset) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) ApplicationAsset to active.
     *
     * @param qualifiedName for the ApplicationAsset
     * @return true if the ApplicationAsset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ApplicationAsset to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ApplicationAsset
     * @return true if the ApplicationAsset is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ApplicationAsset.
     *
     * @param qualifiedName of the ApplicationAsset
     * @param name of the ApplicationAsset
     * @return the minimal request necessary to update the ApplicationAsset, as a builder
     */
    public static ApplicationAssetBuilder<?, ?> updater(String qualifiedName, String name) {
        return ApplicationAsset._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ApplicationAsset, from a potentially
     * more-complete ApplicationAsset object.
     *
     * @return the minimal object necessary to update the ApplicationAsset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ApplicationAsset are not found in the initial object
     */
    @Override
    public ApplicationAssetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ApplicationAsset.
     *
     * @param qualifiedName of the ApplicationAsset
     * @param name of the ApplicationAsset
     * @return the updated ApplicationAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a ApplicationAsset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ApplicationAsset
     * @param name of the ApplicationAsset
     * @return the updated ApplicationAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset removeDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (ApplicationAsset) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ApplicationAsset.
     *
     * @param qualifiedName of the ApplicationAsset
     * @param name of the ApplicationAsset
     * @return the updated ApplicationAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a ApplicationAsset.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ApplicationAsset
     * @param name of the ApplicationAsset
     * @return the updated ApplicationAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset removeUserDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (ApplicationAsset) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ApplicationAsset.
     *
     * @param qualifiedName of the ApplicationAsset
     * @param name of the ApplicationAsset
     * @return the updated ApplicationAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a ApplicationAsset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ApplicationAsset's owners
     * @param qualifiedName of the ApplicationAsset
     * @param name of the ApplicationAsset
     * @return the updated ApplicationAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (ApplicationAsset) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ApplicationAsset.
     *
     * @param qualifiedName of the ApplicationAsset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ApplicationAsset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a ApplicationAsset.
     *
     * @param client connectivity to the Atlan tenant on which to update the ApplicationAsset's certificate
     * @param qualifiedName of the ApplicationAsset
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ApplicationAsset, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset updateCertificate(AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ApplicationAsset) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ApplicationAsset.
     *
     * @param qualifiedName of the ApplicationAsset
     * @param name of the ApplicationAsset
     * @return the updated ApplicationAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a ApplicationAsset.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ApplicationAsset's certificate
     * @param qualifiedName of the ApplicationAsset
     * @param name of the ApplicationAsset
     * @return the updated ApplicationAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset removeCertificate(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (ApplicationAsset) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ApplicationAsset.
     *
     * @param qualifiedName of the ApplicationAsset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a ApplicationAsset.
     *
     * @param client connectivity to the Atlan tenant on which to update the ApplicationAsset's announcement
     * @param qualifiedName of the ApplicationAsset
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (ApplicationAsset) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ApplicationAsset.
     *
     * @param qualifiedName of the ApplicationAsset
     * @param name of the ApplicationAsset
     * @return the updated ApplicationAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a ApplicationAsset.
     *
     * @param client connectivity to the Atlan client from which to remove the ApplicationAsset's announcement
     * @param qualifiedName of the ApplicationAsset
     * @param name of the ApplicationAsset
     * @return the updated ApplicationAsset, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset removeAnnouncement(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (ApplicationAsset) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ApplicationAsset.
     *
     * @param qualifiedName for the ApplicationAsset
     * @param name human-readable name of the ApplicationAsset
     * @param terms the list of terms to replace on the ApplicationAsset, or null to remove all terms from the ApplicationAsset
     * @return the ApplicationAsset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the ApplicationAsset.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ApplicationAsset's assigned terms
     * @param qualifiedName for the ApplicationAsset
     * @param name human-readable name of the ApplicationAsset
     * @param terms the list of terms to replace on the ApplicationAsset, or null to remove all terms from the ApplicationAsset
     * @return the ApplicationAsset that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ApplicationAsset) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ApplicationAsset, without replacing existing terms linked to the ApplicationAsset.
     * Note: this operation must make two API calls — one to retrieve the ApplicationAsset's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ApplicationAsset
     * @param terms the list of terms to append to the ApplicationAsset
     * @return the ApplicationAsset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the ApplicationAsset, without replacing existing terms linked to the ApplicationAsset.
     * Note: this operation must make two API calls — one to retrieve the ApplicationAsset's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ApplicationAsset
     * @param qualifiedName for the ApplicationAsset
     * @param terms the list of terms to append to the ApplicationAsset
     * @return the ApplicationAsset that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (ApplicationAsset) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ApplicationAsset, without replacing all existing terms linked to the ApplicationAsset.
     * Note: this operation must make two API calls — one to retrieve the ApplicationAsset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ApplicationAsset
     * @param terms the list of terms to remove from the ApplicationAsset, which must be referenced by GUID
     * @return the ApplicationAsset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a ApplicationAsset, without replacing all existing terms linked to the ApplicationAsset.
     * Note: this operation must make two API calls — one to retrieve the ApplicationAsset's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ApplicationAsset
     * @param qualifiedName for the ApplicationAsset
     * @param terms the list of terms to remove from the ApplicationAsset, which must be referenced by GUID
     * @return the ApplicationAsset that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ApplicationAsset removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (ApplicationAsset) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ApplicationAsset, without replacing existing Atlan tags linked to the ApplicationAsset.
     * Note: this operation must make two API calls — one to retrieve the ApplicationAsset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ApplicationAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ApplicationAsset
     */
    public static ApplicationAsset appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ApplicationAsset, without replacing existing Atlan tags linked to the ApplicationAsset.
     * Note: this operation must make two API calls — one to retrieve the ApplicationAsset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ApplicationAsset
     * @param qualifiedName of the ApplicationAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ApplicationAsset
     */
    public static ApplicationAsset appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ApplicationAsset) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ApplicationAsset, without replacing existing Atlan tags linked to the ApplicationAsset.
     * Note: this operation must make two API calls — one to retrieve the ApplicationAsset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ApplicationAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ApplicationAsset
     */
    public static ApplicationAsset appendAtlanTags(
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
     * Add Atlan tags to a ApplicationAsset, without replacing existing Atlan tags linked to the ApplicationAsset.
     * Note: this operation must make two API calls — one to retrieve the ApplicationAsset's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ApplicationAsset
     * @param qualifiedName of the ApplicationAsset
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ApplicationAsset
     */
    public static ApplicationAsset appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ApplicationAsset) Asset.appendAtlanTags(
            client,
            TYPE_NAME,
            qualifiedName,
            atlanTagNames,
            propagate,
            removePropagationsOnDelete,
            restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a ApplicationAsset.
     *
     * @param qualifiedName of the ApplicationAsset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ApplicationAsset
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a ApplicationAsset.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ApplicationAsset
     * @param qualifiedName of the ApplicationAsset
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ApplicationAsset
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName) throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }

    @Override
    public SortedSet<IModelEntity> getModelImplementedEntities() {
        return null;
    }
}
