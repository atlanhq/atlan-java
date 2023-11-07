/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
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
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a column within a dbt model in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class DbtModelColumn extends Asset implements IDbtModelColumn, IDbt, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DbtModelColumn";

    /** Fixed typeName for DbtModelColumns. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String dbtAccountName;

    /** TBC */
    @Attribute
    String dbtAlias;

    /** TBC */
    @Attribute
    String dbtConnectionContext;

    /** TBC */
    @Attribute
    String dbtEnvironmentDbtVersion;

    /** TBC */
    @Attribute
    String dbtEnvironmentName;

    /** TBC */
    @Attribute
    Long dbtJobLastRun;

    /** TBC */
    @Attribute
    String dbtJobName;

    /** TBC */
    @Attribute
    Long dbtJobNextRun;

    /** TBC */
    @Attribute
    String dbtJobNextRunHumanized;

    /** TBC */
    @Attribute
    String dbtJobSchedule;

    /** TBC */
    @Attribute
    String dbtJobScheduleCronHumanized;

    /** TBC */
    @Attribute
    String dbtJobStatus;

    /** TBC */
    @Attribute
    String dbtMeta;

    /** TBC */
    @Attribute
    IDbtModel dbtModel;

    /** TBC */
    @Attribute
    String dbtModelColumnDataType;

    /** TBC */
    @Attribute
    Integer dbtModelColumnOrder;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IColumn> dbtModelColumnSqlColumns;

    /** TBC */
    @Attribute
    String dbtModelQualifiedName;

    /** TBC */
    @Attribute
    String dbtPackageName;

    /** TBC */
    @Attribute
    String dbtProjectName;

    /** TBC */
    @Attribute
    String dbtSemanticLayerProxyUrl;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> dbtTags;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtTest> dbtTests;

    /** TBC */
    @Attribute
    String dbtUniqueId;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    IColumn sqlColumn;

    /**
     * Builds the minimal object necessary to create a relationship to a DbtModelColumn, from a potentially
     * more-complete DbtModelColumn object.
     *
     * @return the minimal object necessary to relate to the DbtModelColumn
     * @throws InvalidRequestException if any of the minimal set of required properties for a DbtModelColumn relationship are not found in the initial object
     */
    @Override
    public DbtModelColumn trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all DbtModelColumn assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DbtModelColumn assets will be included.
     *
     * @return a fluent search that includes all DbtModelColumn assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all DbtModelColumn assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DbtModelColumn assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all DbtModelColumn assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all DbtModelColumn assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DbtModelColumns will be included
     * @return a fluent search that includes all DbtModelColumn assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all DbtModelColumn assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DbtModelColumns will be included
     * @return a fluent search that includes all DbtModelColumn assets
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
     * Start an asset filter that will return all DbtModelColumn assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DbtModelColumn assets will be included.
     *
     * @return an asset filter that includes all DbtModelColumn assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all DbtModelColumn assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) DbtModelColumn assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all DbtModelColumn assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all DbtModelColumn assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) DbtModelColumns will be included
     * @return an asset filter that includes all DbtModelColumn assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all DbtModelColumn assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) DbtModelColumns will be included
     * @return an asset filter that includes all DbtModelColumn assets
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
     * Reference to a DbtModelColumn by GUID.
     *
     * @param guid the GUID of the DbtModelColumn to reference
     * @return reference to a DbtModelColumn that can be used for defining a relationship to a DbtModelColumn
     */
    public static DbtModelColumn refByGuid(String guid) {
        return DbtModelColumn._internal().guid(guid).build();
    }

    /**
     * Reference to a DbtModelColumn by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the DbtModelColumn to reference
     * @return reference to a DbtModelColumn that can be used for defining a relationship to a DbtModelColumn
     */
    public static DbtModelColumn refByQualifiedName(String qualifiedName) {
        return DbtModelColumn._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a DbtModelColumn by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the DbtModelColumn to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DbtModelColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist or the provided GUID is not a DbtModelColumn
     */
    @JsonIgnore
    public static DbtModelColumn get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a DbtModelColumn by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtModelColumn to retrieve, either its GUID or its full qualifiedName
     * @return the requested full DbtModelColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist or the provided GUID is not a DbtModelColumn
     */
    @JsonIgnore
    public static DbtModelColumn get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a DbtModelColumn by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the DbtModelColumn to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full DbtModelColumn, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist or the provided GUID is not a DbtModelColumn
     */
    @JsonIgnore
    public static DbtModelColumn get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof DbtModelColumn) {
                return (DbtModelColumn) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof DbtModelColumn) {
                return (DbtModelColumn) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a DbtModelColumn by its GUID, complete with all of its relationships.
     *
     * @param guid of the DbtModelColumn to retrieve
     * @return the requested full DbtModelColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist or the provided GUID is not a DbtModelColumn
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static DbtModelColumn retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a DbtModelColumn by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the DbtModelColumn to retrieve
     * @return the requested full DbtModelColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist or the provided GUID is not a DbtModelColumn
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static DbtModelColumn retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a DbtModelColumn by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the DbtModelColumn to retrieve
     * @return the requested full DbtModelColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static DbtModelColumn retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a DbtModelColumn by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the DbtModelColumn to retrieve
     * @return the requested full DbtModelColumn, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the DbtModelColumn does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static DbtModelColumn retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DbtModelColumn to active.
     *
     * @param qualifiedName for the DbtModelColumn
     * @return true if the DbtModelColumn is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) DbtModelColumn to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the DbtModelColumn
     * @return true if the DbtModelColumn is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the minimal request necessary to update the DbtModelColumn, as a builder
     */
    public static DbtModelColumnBuilder<?, ?> updater(String qualifiedName, String name) {
        return DbtModelColumn._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a DbtModelColumn, from a potentially
     * more-complete DbtModelColumn object.
     *
     * @return the minimal object necessary to update the DbtModelColumn, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for DbtModelColumn are not found in the initial object
     */
    @Override
    public DbtModelColumnBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "DbtModelColumn", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModelColumn) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModelColumn) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtModelColumn's owners
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModelColumn) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtModelColumn, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtModelColumn's certificate
     * @param qualifiedName of the DbtModelColumn
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated DbtModelColumn, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (DbtModelColumn)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant from which to remove the DbtModelColumn's certificate
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModelColumn) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to update the DbtModelColumn's announcement
     * @param qualifiedName of the DbtModelColumn
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (DbtModelColumn)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a DbtModelColumn.
     *
     * @param client connectivity to the Atlan client from which to remove the DbtModelColumn's announcement
     * @param qualifiedName of the DbtModelColumn
     * @param name of the DbtModelColumn
     * @return the updated DbtModelColumn, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (DbtModelColumn) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the DbtModelColumn.
     *
     * @param qualifiedName for the DbtModelColumn
     * @param name human-readable name of the DbtModelColumn
     * @param terms the list of terms to replace on the DbtModelColumn, or null to remove all terms from the DbtModelColumn
     * @return the DbtModelColumn that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to replace the DbtModelColumn's assigned terms
     * @param qualifiedName for the DbtModelColumn
     * @param name human-readable name of the DbtModelColumn
     * @param terms the list of terms to replace on the DbtModelColumn, or null to remove all terms from the DbtModelColumn
     * @return the DbtModelColumn that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (DbtModelColumn) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the DbtModelColumn, without replacing existing terms linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the DbtModelColumn
     * @param terms the list of terms to append to the DbtModelColumn
     * @return the DbtModelColumn that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the DbtModelColumn, without replacing existing terms linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the DbtModelColumn
     * @param qualifiedName for the DbtModelColumn
     * @param terms the list of terms to append to the DbtModelColumn
     * @return the DbtModelColumn that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtModelColumn) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtModelColumn, without replacing all existing terms linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the DbtModelColumn
     * @param terms the list of terms to remove from the DbtModelColumn, which must be referenced by GUID
     * @return the DbtModelColumn that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a DbtModelColumn, without replacing all existing terms linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the DbtModelColumn
     * @param qualifiedName for the DbtModelColumn
     * @param terms the list of terms to remove from the DbtModelColumn, which must be referenced by GUID
     * @return the DbtModelColumn that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static DbtModelColumn removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (DbtModelColumn) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a DbtModelColumn, without replacing existing Atlan tags linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtModelColumn
     */
    public static DbtModelColumn appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtModelColumn, without replacing existing Atlan tags linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtModelColumn
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated DbtModelColumn
     */
    public static DbtModelColumn appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (DbtModelColumn) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtModelColumn, without replacing existing Atlan tags linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtModelColumn
     */
    public static DbtModelColumn appendAtlanTags(
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
     * Add Atlan tags to a DbtModelColumn, without replacing existing Atlan tags linked to the DbtModelColumn.
     * Note: this operation must make two API calls — one to retrieve the DbtModelColumn's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the DbtModelColumn
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated DbtModelColumn
     */
    public static DbtModelColumn appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (DbtModelColumn) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtModelColumn
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DbtModelColumn
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtModelColumn
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtModelColumn
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
     * Add Atlan tags to a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the DbtModelColumn
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the DbtModelColumn
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
     * Remove an Atlan tag from a DbtModelColumn.
     *
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtModelColumn
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a DbtModelColumn.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a DbtModelColumn
     * @param qualifiedName of the DbtModelColumn
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the DbtModelColumn
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
