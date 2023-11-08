/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.CompoundQuery;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Matillion component in Atlan. Components in Matillion are a part of a job, where each component is responsible for accomplishing a task based on the type of component used.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class MatillionComponent extends Asset
        implements IMatillionComponent, IMatillion, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MatillionComponent";

    /** Fixed typeName for MatillionComponents. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** Processes to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** Unique identifier of the component in Matillion. */
    @Attribute
    String matillionComponentId;

    /** Unique identifier for the type of the component in Matillion. */
    @Attribute
    String matillionComponentImplementationId;

    /** Last five run statuses of the component within a job. */
    @Attribute
    String matillionComponentLastFiveRunStatus;

    /** Latest run status of the component within a job. */
    @Attribute
    String matillionComponentLastRunStatus;

    /** Job details of the job to which the component internally links. */
    @Attribute
    @Singular("putMatillionComponentLinkedJob")
    Map<String, String> matillionComponentLinkedJob;

    /** SQL queries used by the component. */
    @Attribute
    @Singular
    SortedSet<String> matillionComponentSqls;

    /** Job in which this component exists. */
    @Attribute
    IMatillionJob matillionJob;

    /** Simple name of the job to which the component belongs. */
    @Attribute
    String matillionJobName;

    /** Unique name of the job to which the component belongs. */
    @Attribute
    String matillionJobQualifiedName;

    /** Lineage process that represents this Matillion component. */
    @Attribute
    ILineageProcess matillionProcess;

    /** TBC */
    @Attribute
    String matillionVersion;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** Processes from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Builds the minimal object necessary to create a relationship to a MatillionComponent, from a potentially
     * more-complete MatillionComponent object.
     *
     * @return the minimal object necessary to relate to the MatillionComponent
     * @throws InvalidRequestException if any of the minimal set of required properties for a MatillionComponent relationship are not found in the initial object
     */
    @Override
    public MatillionComponent trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all MatillionComponent assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MatillionComponent assets will be included.
     *
     * @return a fluent search that includes all MatillionComponent assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all MatillionComponent assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MatillionComponent assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all MatillionComponent assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all MatillionComponent assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) MatillionComponents will be included
     * @return a fluent search that includes all MatillionComponent assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all MatillionComponent assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) MatillionComponents will be included
     * @return a fluent search that includes all MatillionComponent assets
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
     * Start an asset filter that will return all MatillionComponent assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MatillionComponent assets will be included.
     *
     * @return an asset filter that includes all MatillionComponent assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all MatillionComponent assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MatillionComponent assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all MatillionComponent assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all MatillionComponent assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) MatillionComponents will be included
     * @return an asset filter that includes all MatillionComponent assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all MatillionComponent assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) MatillionComponents will be included
     * @return an asset filter that includes all MatillionComponent assets
     * @deprecated replaced by {@link #select(AtlanClient, boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a MatillionComponent by GUID.
     *
     * @param guid the GUID of the MatillionComponent to reference
     * @return reference to a MatillionComponent that can be used for defining a relationship to a MatillionComponent
     */
    public static MatillionComponent refByGuid(String guid) {
        return MatillionComponent._internal().guid(guid).build();
    }

    /**
     * Reference to a MatillionComponent by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MatillionComponent to reference
     * @return reference to a MatillionComponent that can be used for defining a relationship to a MatillionComponent
     */
    public static MatillionComponent refByQualifiedName(String qualifiedName) {
        return MatillionComponent._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MatillionComponent by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the MatillionComponent to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MatillionComponent, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MatillionComponent does not exist or the provided GUID is not a MatillionComponent
     */
    @JsonIgnore
    public static MatillionComponent get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a MatillionComponent by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MatillionComponent to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MatillionComponent, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MatillionComponent does not exist or the provided GUID is not a MatillionComponent
     */
    @JsonIgnore
    public static MatillionComponent get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a MatillionComponent by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MatillionComponent to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full MatillionComponent, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MatillionComponent does not exist or the provided GUID is not a MatillionComponent
     */
    @JsonIgnore
    public static MatillionComponent get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof MatillionComponent) {
                return (MatillionComponent) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof MatillionComponent) {
                return (MatillionComponent) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a MatillionComponent by its GUID, complete with all of its relationships.
     *
     * @param guid of the MatillionComponent to retrieve
     * @return the requested full MatillionComponent, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MatillionComponent does not exist or the provided GUID is not a MatillionComponent
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static MatillionComponent retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a MatillionComponent by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the MatillionComponent to retrieve
     * @return the requested full MatillionComponent, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MatillionComponent does not exist or the provided GUID is not a MatillionComponent
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static MatillionComponent retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a MatillionComponent by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MatillionComponent to retrieve
     * @return the requested full MatillionComponent, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MatillionComponent does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static MatillionComponent retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a MatillionComponent by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the MatillionComponent to retrieve
     * @return the requested full MatillionComponent, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MatillionComponent does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static MatillionComponent retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) MatillionComponent to active.
     *
     * @param qualifiedName for the MatillionComponent
     * @return true if the MatillionComponent is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) MatillionComponent to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MatillionComponent
     * @return true if the MatillionComponent is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MatillionComponent.
     *
     * @param qualifiedName of the MatillionComponent
     * @param name of the MatillionComponent
     * @return the minimal request necessary to update the MatillionComponent, as a builder
     */
    public static MatillionComponentBuilder<?, ?> updater(String qualifiedName, String name) {
        return MatillionComponent._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MatillionComponent, from a potentially
     * more-complete MatillionComponent object.
     *
     * @return the minimal object necessary to update the MatillionComponent, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MatillionComponent are not found in the initial object
     */
    @Override
    public MatillionComponentBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MatillionComponent", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MatillionComponent.
     *
     * @param qualifiedName of the MatillionComponent
     * @param name of the MatillionComponent
     * @return the updated MatillionComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a MatillionComponent.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MatillionComponent
     * @param name of the MatillionComponent
     * @return the updated MatillionComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MatillionComponent) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MatillionComponent.
     *
     * @param qualifiedName of the MatillionComponent
     * @param name of the MatillionComponent
     * @return the updated MatillionComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a MatillionComponent.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MatillionComponent
     * @param name of the MatillionComponent
     * @return the updated MatillionComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MatillionComponent) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MatillionComponent.
     *
     * @param qualifiedName of the MatillionComponent
     * @param name of the MatillionComponent
     * @return the updated MatillionComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a MatillionComponent.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MatillionComponent's owners
     * @param qualifiedName of the MatillionComponent
     * @param name of the MatillionComponent
     * @return the updated MatillionComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MatillionComponent) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MatillionComponent.
     *
     * @param qualifiedName of the MatillionComponent
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MatillionComponent, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a MatillionComponent.
     *
     * @param client connectivity to the Atlan tenant on which to update the MatillionComponent's certificate
     * @param qualifiedName of the MatillionComponent
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MatillionComponent, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MatillionComponent)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MatillionComponent.
     *
     * @param qualifiedName of the MatillionComponent
     * @param name of the MatillionComponent
     * @return the updated MatillionComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a MatillionComponent.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MatillionComponent's certificate
     * @param qualifiedName of the MatillionComponent
     * @param name of the MatillionComponent
     * @return the updated MatillionComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MatillionComponent) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MatillionComponent.
     *
     * @param qualifiedName of the MatillionComponent
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a MatillionComponent.
     *
     * @param client connectivity to the Atlan tenant on which to update the MatillionComponent's announcement
     * @param qualifiedName of the MatillionComponent
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MatillionComponent)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MatillionComponent.
     *
     * @param qualifiedName of the MatillionComponent
     * @param name of the MatillionComponent
     * @return the updated MatillionComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a MatillionComponent.
     *
     * @param client connectivity to the Atlan client from which to remove the MatillionComponent's announcement
     * @param qualifiedName of the MatillionComponent
     * @param name of the MatillionComponent
     * @return the updated MatillionComponent, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MatillionComponent) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MatillionComponent.
     *
     * @param qualifiedName for the MatillionComponent
     * @param name human-readable name of the MatillionComponent
     * @param terms the list of terms to replace on the MatillionComponent, or null to remove all terms from the MatillionComponent
     * @return the MatillionComponent that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the MatillionComponent.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MatillionComponent's assigned terms
     * @param qualifiedName for the MatillionComponent
     * @param name human-readable name of the MatillionComponent
     * @param terms the list of terms to replace on the MatillionComponent, or null to remove all terms from the MatillionComponent
     * @return the MatillionComponent that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MatillionComponent) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MatillionComponent, without replacing existing terms linked to the MatillionComponent.
     * Note: this operation must make two API calls — one to retrieve the MatillionComponent's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MatillionComponent
     * @param terms the list of terms to append to the MatillionComponent
     * @return the MatillionComponent that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the MatillionComponent, without replacing existing terms linked to the MatillionComponent.
     * Note: this operation must make two API calls — one to retrieve the MatillionComponent's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MatillionComponent
     * @param qualifiedName for the MatillionComponent
     * @param terms the list of terms to append to the MatillionComponent
     * @return the MatillionComponent that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MatillionComponent) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MatillionComponent, without replacing all existing terms linked to the MatillionComponent.
     * Note: this operation must make two API calls — one to retrieve the MatillionComponent's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MatillionComponent
     * @param terms the list of terms to remove from the MatillionComponent, which must be referenced by GUID
     * @return the MatillionComponent that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a MatillionComponent, without replacing all existing terms linked to the MatillionComponent.
     * Note: this operation must make two API calls — one to retrieve the MatillionComponent's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MatillionComponent
     * @param qualifiedName for the MatillionComponent
     * @param terms the list of terms to remove from the MatillionComponent, which must be referenced by GUID
     * @return the MatillionComponent that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MatillionComponent removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MatillionComponent) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MatillionComponent, without replacing existing Atlan tags linked to the MatillionComponent.
     * Note: this operation must make two API calls — one to retrieve the MatillionComponent's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MatillionComponent
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MatillionComponent
     */
    public static MatillionComponent appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MatillionComponent, without replacing existing Atlan tags linked to the MatillionComponent.
     * Note: this operation must make two API calls — one to retrieve the MatillionComponent's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MatillionComponent
     * @param qualifiedName of the MatillionComponent
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MatillionComponent
     */
    public static MatillionComponent appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (MatillionComponent) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MatillionComponent, without replacing existing Atlan tags linked to the MatillionComponent.
     * Note: this operation must make two API calls — one to retrieve the MatillionComponent's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MatillionComponent
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MatillionComponent
     */
    public static MatillionComponent appendAtlanTags(
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
     * Add Atlan tags to a MatillionComponent, without replacing existing Atlan tags linked to the MatillionComponent.
     * Note: this operation must make two API calls — one to retrieve the MatillionComponent's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MatillionComponent
     * @param qualifiedName of the MatillionComponent
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MatillionComponent
     */
    public static MatillionComponent appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MatillionComponent) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MatillionComponent.
     *
     * @param qualifiedName of the MatillionComponent
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MatillionComponent
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MatillionComponent.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MatillionComponent
     * @param qualifiedName of the MatillionComponent
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MatillionComponent
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MatillionComponent.
     *
     * @param qualifiedName of the MatillionComponent
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MatillionComponent
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
     * Add Atlan tags to a MatillionComponent.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MatillionComponent
     * @param qualifiedName of the MatillionComponent
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MatillionComponent
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
     * Remove an Atlan tag from a MatillionComponent.
     *
     * @param qualifiedName of the MatillionComponent
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MatillionComponent
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a MatillionComponent.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MatillionComponent
     * @param qualifiedName of the MatillionComponent
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MatillionComponent
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
