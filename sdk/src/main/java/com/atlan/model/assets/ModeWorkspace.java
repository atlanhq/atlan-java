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
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.CompoundQuery;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Mode workspace in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class ModeWorkspace extends Asset implements IModeWorkspace, IMode, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "ModeWorkspace";

    /** Fixed typeName for ModeWorkspaces. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Tasks to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** Processes to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** Number of collections in this workspace. */
    @Attribute
    Long modeCollectionCount;

    /** Collections that exist within this workspace. */
    @Attribute
    @Singular
    SortedSet<IModeCollection> modeCollections;

    /** TBC */
    @Attribute
    String modeId;

    /** TBC */
    @Attribute
    String modeQueryName;

    /** TBC */
    @Attribute
    String modeQueryQualifiedName;

    /** TBC */
    @Attribute
    String modeReportName;

    /** TBC */
    @Attribute
    String modeReportQualifiedName;

    /** TBC */
    @Attribute
    String modeToken;

    /** TBC */
    @Attribute
    String modeWorkspaceName;

    /** TBC */
    @Attribute
    String modeWorkspaceQualifiedName;

    /** TBC */
    @Attribute
    String modeWorkspaceUsername;

    /** Tasks from which this asset is output. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** Processes from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Builds the minimal object necessary to create a relationship to a ModeWorkspace, from a potentially
     * more-complete ModeWorkspace object.
     *
     * @return the minimal object necessary to relate to the ModeWorkspace
     * @throws InvalidRequestException if any of the minimal set of required properties for a ModeWorkspace relationship are not found in the initial object
     */
    @Override
    public ModeWorkspace trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all ModeWorkspace assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModeWorkspace assets will be included.
     *
     * @return a fluent search that includes all ModeWorkspace assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all ModeWorkspace assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModeWorkspace assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all ModeWorkspace assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all ModeWorkspace assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ModeWorkspaces will be included
     * @return a fluent search that includes all ModeWorkspace assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all ModeWorkspace assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ModeWorkspaces will be included
     * @return a fluent search that includes all ModeWorkspace assets
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
     * Start an asset filter that will return all ModeWorkspace assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModeWorkspace assets will be included.
     *
     * @return an asset filter that includes all ModeWorkspace assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all ModeWorkspace assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) ModeWorkspace assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all ModeWorkspace assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all ModeWorkspace assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) ModeWorkspaces will be included
     * @return an asset filter that includes all ModeWorkspace assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all ModeWorkspace assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) ModeWorkspaces will be included
     * @return an asset filter that includes all ModeWorkspace assets
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
     * Reference to a ModeWorkspace by GUID. Use this to create a relationship to this ModeWorkspace,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the ModeWorkspace to reference
     * @return reference to a ModeWorkspace that can be used for defining a relationship to a ModeWorkspace
     */
    public static ModeWorkspace refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModeWorkspace by GUID. Use this to create a relationship to this ModeWorkspace,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the ModeWorkspace to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModeWorkspace that can be used for defining a relationship to a ModeWorkspace
     */
    public static ModeWorkspace refByGuid(String guid, Reference.SaveSemantic semantic) {
        return ModeWorkspace._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a ModeWorkspace by qualifiedName. Use this to create a relationship to this ModeWorkspace,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the ModeWorkspace to reference
     * @return reference to a ModeWorkspace that can be used for defining a relationship to a ModeWorkspace
     */
    public static ModeWorkspace refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a ModeWorkspace by qualifiedName. Use this to create a relationship to this ModeWorkspace,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the ModeWorkspace to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a ModeWorkspace that can be used for defining a relationship to a ModeWorkspace
     */
    public static ModeWorkspace refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return ModeWorkspace._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a ModeWorkspace by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the ModeWorkspace to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModeWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeWorkspace does not exist or the provided GUID is not a ModeWorkspace
     */
    @JsonIgnore
    public static ModeWorkspace get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a ModeWorkspace by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModeWorkspace to retrieve, either its GUID or its full qualifiedName
     * @return the requested full ModeWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeWorkspace does not exist or the provided GUID is not a ModeWorkspace
     */
    @JsonIgnore
    public static ModeWorkspace get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a ModeWorkspace by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the ModeWorkspace to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full ModeWorkspace, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeWorkspace does not exist or the provided GUID is not a ModeWorkspace
     */
    @JsonIgnore
    public static ModeWorkspace get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof ModeWorkspace) {
                return (ModeWorkspace) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof ModeWorkspace) {
                return (ModeWorkspace) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a ModeWorkspace by its GUID, complete with all of its relationships.
     *
     * @param guid of the ModeWorkspace to retrieve
     * @return the requested full ModeWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeWorkspace does not exist or the provided GUID is not a ModeWorkspace
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ModeWorkspace retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a ModeWorkspace by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the ModeWorkspace to retrieve
     * @return the requested full ModeWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeWorkspace does not exist or the provided GUID is not a ModeWorkspace
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ModeWorkspace retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a ModeWorkspace by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the ModeWorkspace to retrieve
     * @return the requested full ModeWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeWorkspace does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static ModeWorkspace retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a ModeWorkspace by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the ModeWorkspace to retrieve
     * @return the requested full ModeWorkspace, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the ModeWorkspace does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static ModeWorkspace retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ModeWorkspace to active.
     *
     * @param qualifiedName for the ModeWorkspace
     * @return true if the ModeWorkspace is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) ModeWorkspace to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the ModeWorkspace
     * @return true if the ModeWorkspace is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the minimal request necessary to update the ModeWorkspace, as a builder
     */
    public static ModeWorkspaceBuilder<?, ?> updater(String qualifiedName, String name) {
        return ModeWorkspace._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a ModeWorkspace, from a potentially
     * more-complete ModeWorkspace object.
     *
     * @return the minimal object necessary to update the ModeWorkspace, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for ModeWorkspace are not found in the initial object
     */
    @Override
    public ModeWorkspaceBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "ModeWorkspace", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a ModeWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeWorkspace) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a ModeWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeWorkspace) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a ModeWorkspace.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModeWorkspace's owners
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeWorkspace) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModeWorkspace, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a ModeWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModeWorkspace's certificate
     * @param qualifiedName of the ModeWorkspace
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated ModeWorkspace, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (ModeWorkspace)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a ModeWorkspace.
     *
     * @param client connectivity to the Atlan tenant from which to remove the ModeWorkspace's certificate
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeWorkspace) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a ModeWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to update the ModeWorkspace's announcement
     * @param qualifiedName of the ModeWorkspace
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (ModeWorkspace)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a ModeWorkspace.
     *
     * @param client connectivity to the Atlan client from which to remove the ModeWorkspace's announcement
     * @param qualifiedName of the ModeWorkspace
     * @param name of the ModeWorkspace
     * @return the updated ModeWorkspace, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (ModeWorkspace) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the ModeWorkspace.
     *
     * @param qualifiedName for the ModeWorkspace
     * @param name human-readable name of the ModeWorkspace
     * @param terms the list of terms to replace on the ModeWorkspace, or null to remove all terms from the ModeWorkspace
     * @return the ModeWorkspace that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the ModeWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to replace the ModeWorkspace's assigned terms
     * @param qualifiedName for the ModeWorkspace
     * @param name human-readable name of the ModeWorkspace
     * @param terms the list of terms to replace on the ModeWorkspace, or null to remove all terms from the ModeWorkspace
     * @return the ModeWorkspace that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (ModeWorkspace) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the ModeWorkspace, without replacing existing terms linked to the ModeWorkspace.
     * Note: this operation must make two API calls — one to retrieve the ModeWorkspace's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the ModeWorkspace
     * @param terms the list of terms to append to the ModeWorkspace
     * @return the ModeWorkspace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the ModeWorkspace, without replacing existing terms linked to the ModeWorkspace.
     * Note: this operation must make two API calls — one to retrieve the ModeWorkspace's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the ModeWorkspace
     * @param qualifiedName for the ModeWorkspace
     * @param terms the list of terms to append to the ModeWorkspace
     * @return the ModeWorkspace that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModeWorkspace) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a ModeWorkspace, without replacing all existing terms linked to the ModeWorkspace.
     * Note: this operation must make two API calls — one to retrieve the ModeWorkspace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the ModeWorkspace
     * @param terms the list of terms to remove from the ModeWorkspace, which must be referenced by GUID
     * @return the ModeWorkspace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a ModeWorkspace, without replacing all existing terms linked to the ModeWorkspace.
     * Note: this operation must make two API calls — one to retrieve the ModeWorkspace's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the ModeWorkspace
     * @param qualifiedName for the ModeWorkspace
     * @param terms the list of terms to remove from the ModeWorkspace, which must be referenced by GUID
     * @return the ModeWorkspace that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static ModeWorkspace removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (ModeWorkspace) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a ModeWorkspace, without replacing existing Atlan tags linked to the ModeWorkspace.
     * Note: this operation must make two API calls — one to retrieve the ModeWorkspace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModeWorkspace
     */
    public static ModeWorkspace appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeWorkspace, without replacing existing Atlan tags linked to the ModeWorkspace.
     * Note: this operation must make two API calls — one to retrieve the ModeWorkspace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModeWorkspace
     * @param qualifiedName of the ModeWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated ModeWorkspace
     */
    public static ModeWorkspace appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (ModeWorkspace) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeWorkspace, without replacing existing Atlan tags linked to the ModeWorkspace.
     * Note: this operation must make two API calls — one to retrieve the ModeWorkspace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModeWorkspace
     */
    public static ModeWorkspace appendAtlanTags(
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
     * Add Atlan tags to a ModeWorkspace, without replacing existing Atlan tags linked to the ModeWorkspace.
     * Note: this operation must make two API calls — one to retrieve the ModeWorkspace's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the ModeWorkspace
     * @param qualifiedName of the ModeWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated ModeWorkspace
     */
    public static ModeWorkspace appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (ModeWorkspace) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ModeWorkspace
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ModeWorkspace
     * @param qualifiedName of the ModeWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ModeWorkspace
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ModeWorkspace
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
     * Add Atlan tags to a ModeWorkspace.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the ModeWorkspace
     * @param qualifiedName of the ModeWorkspace
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the ModeWorkspace
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
     * Remove an Atlan tag from a ModeWorkspace.
     *
     * @param qualifiedName of the ModeWorkspace
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModeWorkspace
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a ModeWorkspace.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a ModeWorkspace
     * @param qualifiedName of the ModeWorkspace
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the ModeWorkspace
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
