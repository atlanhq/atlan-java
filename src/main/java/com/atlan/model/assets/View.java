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
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a database view in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class View extends Asset implements IView, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "View";

    /** Fixed typeName for Views. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String alias;

    /** Number of columns in this view. */
    @Attribute
    Long columnCount;

    /** Columns that exist within this view. */
    @Attribute
    @Singular
    SortedSet<IColumn> columns;

    /** TBC */
    @Attribute
    String databaseName;

    /** TBC */
    @Attribute
    String databaseQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> dbtModels;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> dbtSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtTest> dbtTests;

    /** Definition of the view (DDL). */
    @Attribute
    String definition;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    Boolean isProfiled;

    /** TBC */
    @Attribute
    Boolean isQueryPreview;

    /** Whether this view is temporary (true) or not (false). */
    @Attribute
    Boolean isTemporary;

    /** TBC */
    @Attribute
    Long lastProfiledAt;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** Queries that involve this view. */
    @Attribute
    @Singular
    SortedSet<IAtlanQuery> queries;

    /** TBC */
    @Attribute
    Long queryCount;

    /** TBC */
    @Attribute
    Long queryCountUpdatedAt;

    /** TBC */
    @Attribute
    @Singular("putQueryPreviewConfig")
    Map<String, String> queryPreviewConfig;

    /** TBC */
    @Attribute
    Long queryUserCount;

    /** TBC */
    @Attribute
    @Singular("putQueryUserMap")
    Map<String, Long> queryUserMap;

    /** Number of rows in this view. */
    @Attribute
    Long rowCount;

    /** Schema in which this view exists. */
    @Attribute
    @JsonProperty("atlanSchema")
    ISchema schema;

    /** TBC */
    @Attribute
    String schemaName;

    /** TBC */
    @Attribute
    String schemaQualifiedName;

    /** Size of the view in bytes. */
    @Attribute
    Long sizeBytes;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtSource> sqlDBTSources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IDbtModel> sqlDbtModels;

    /** TBC */
    @Attribute
    String tableName;

    /** TBC */
    @Attribute
    String tableQualifiedName;

    /** TBC */
    @Attribute
    String viewName;

    /** TBC */
    @Attribute
    String viewQualifiedName;

    /**
     * Start an asset filter that will return all View assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) View assets will be included.
     *
     * @return an asset filter that includes all View assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all View assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) View assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all View assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all View assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) Views will be included
     * @return an asset filter that includes all View assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all View assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Views will be included
     * @return an asset filter that includes all View assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a View by GUID.
     *
     * @param guid the GUID of the View to reference
     * @return reference to a View that can be used for defining a relationship to a View
     */
    public static View refByGuid(String guid) {
        return View.builder().guid(guid).build();
    }

    /**
     * Reference to a View by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the View to reference
     * @return reference to a View that can be used for defining a relationship to a View
     */
    public static View refByQualifiedName(String qualifiedName) {
        return View.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a View by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the View to retrieve, either its GUID or its full qualifiedName
     * @return the requested full View, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the View does not exist or the provided GUID is not a View
     */
    @JsonIgnore
    public static View get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a View by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the View to retrieve, either its GUID or its full qualifiedName
     * @return the requested full View, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the View does not exist or the provided GUID is not a View
     */
    @JsonIgnore
    public static View get(AtlanClient client, String id) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (id.startsWith("default")) {
            Asset asset = Asset.retrieveFull(client, TYPE_NAME, id);
            if (asset instanceof View) {
                return (View) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "View");
            }
        } else {
            Asset asset = Asset.retrieveFull(client, id);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof View) {
                return (View) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "View");
            }
        }
    }

    /**
     * Retrieves a View by its GUID, complete with all of its relationships.
     *
     * @param guid of the View to retrieve
     * @return the requested full View, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the View does not exist or the provided GUID is not a View
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static View retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a View by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the View to retrieve
     * @return the requested full View, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the View does not exist or the provided GUID is not a View
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static View retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof View) {
            return (View) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "View");
        }
    }

    /**
     * Retrieves a View by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the View to retrieve
     * @return the requested full View, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the View does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static View retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a View by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the View to retrieve
     * @return the requested full View, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the View does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static View retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof View) {
            return (View) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "View");
        }
    }

    /**
     * Restore the archived (soft-deleted) View to active.
     *
     * @param qualifiedName for the View
     * @return true if the View is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) View to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the View
     * @return true if the View is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a view.
     *
     * @param name of the view
     * @param schemaQualifiedName unique name of the schema in which this view exists
     * @return the minimal request necessary to create the view, as a builder
     */
    public static ViewBuilder<?, ?> creator(String name, String schemaQualifiedName) {
        String[] tokens = schemaQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return View.builder()
                .name(name)
                .qualifiedName(generateQualifiedName(name, schemaQualifiedName))
                .connectorType(connectorType)
                .schemaName(schemaName)
                .schemaQualifiedName(schemaQualifiedName)
                .schema(Schema.refByQualifiedName(schemaQualifiedName))
                .databaseName(databaseName)
                .databaseQualifiedName(databaseQualifiedName)
                .connectionQualifiedName(connectionQualifiedName);
    }

    /**
     * Generate a unique view name.
     *
     * @param name of the view
     * @param schemaQualifiedName unique name of the schema in which this view exists
     * @return a unique name for the view
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the minimal request necessary to update the View, as a builder
     */
    public static ViewBuilder<?, ?> updater(String qualifiedName, String name) {
        return View.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a View, from a potentially
     * more-complete View object.
     *
     * @return the minimal object necessary to update the View, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for View are not found in the initial object
     */
    @Override
    public ViewBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "View", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a View.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (View) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a View.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (View) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a View.
     *
     * @param client connectivity to the Atlan tenant from which to remove the View's owners
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (View) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a View.
     *
     * @param qualifiedName of the View
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated View, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static View updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a View.
     *
     * @param client connectivity to the Atlan tenant on which to update the View's certificate
     * @param qualifiedName of the View
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated View, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static View updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (View) Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a View.
     *
     * @param client connectivity to the Atlan tenant from which to remove the View's certificate
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeCertificate(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (View) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a View.
     *
     * @param qualifiedName of the View
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static View updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a View.
     *
     * @param client connectivity to the Atlan tenant on which to update the View's announcement
     * @param qualifiedName of the View
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static View updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (View) Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a View.
     *
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a View.
     *
     * @param client connectivity to the Atlan client from which to remove the View's announcement
     * @param qualifiedName of the View
     * @param name of the View
     * @return the updated View, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static View removeAnnouncement(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (View) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the View.
     *
     * @param qualifiedName for the View
     * @param name human-readable name of the View
     * @param terms the list of terms to replace on the View, or null to remove all terms from the View
     * @return the View that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static View replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the View.
     *
     * @param client connectivity to the Atlan tenant on which to replace the View's assigned terms
     * @param qualifiedName for the View
     * @param name human-readable name of the View
     * @param terms the list of terms to replace on the View, or null to remove all terms from the View
     * @return the View that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static View replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (View) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the View, without replacing existing terms linked to the View.
     * Note: this operation must make two API calls — one to retrieve the View's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the View
     * @param terms the list of terms to append to the View
     * @return the View that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static View appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the View, without replacing existing terms linked to the View.
     * Note: this operation must make two API calls — one to retrieve the View's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the View
     * @param qualifiedName for the View
     * @param terms the list of terms to append to the View
     * @return the View that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static View appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (View) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a View, without replacing all existing terms linked to the View.
     * Note: this operation must make two API calls — one to retrieve the View's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the View
     * @param terms the list of terms to remove from the View, which must be referenced by GUID
     * @return the View that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static View removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a View, without replacing all existing terms linked to the View.
     * Note: this operation must make two API calls — one to retrieve the View's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the View
     * @param qualifiedName for the View
     * @param terms the list of terms to remove from the View, which must be referenced by GUID
     * @return the View that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static View removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (View) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a View, without replacing existing Atlan tags linked to the View.
     * Note: this operation must make two API calls — one to retrieve the View's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the View
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated View
     */
    public static View appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a View, without replacing existing Atlan tags linked to the View.
     * Note: this operation must make two API calls — one to retrieve the View's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the View
     * @param qualifiedName of the View
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated View
     */
    public static View appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (View) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a View, without replacing existing Atlan tags linked to the View.
     * Note: this operation must make two API calls — one to retrieve the View's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the View
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated View
     */
    public static View appendAtlanTags(
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
     * Add Atlan tags to a View, without replacing existing Atlan tags linked to the View.
     * Note: this operation must make two API calls — one to retrieve the View's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the View
     * @param qualifiedName of the View
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated View
     */
    public static View appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (View) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a View.
     *
     * @param qualifiedName of the View
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the View
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a View.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the View
     * @param qualifiedName of the View
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the View
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a View.
     *
     * @param qualifiedName of the View
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the View
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
     * Add Atlan tags to a View.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the View
     * @param qualifiedName of the View
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the View
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
     * Remove an Atlan tag from a View.
     *
     * @param qualifiedName of the View
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the View
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a View.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a View
     * @param qualifiedName of the View
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the View
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
