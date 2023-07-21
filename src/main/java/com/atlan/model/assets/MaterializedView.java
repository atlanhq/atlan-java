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
 * Instance of a materialized view in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class MaterializedView extends Asset implements IMaterializedView, ISQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MaterialisedView";

    /** Fixed typeName for MaterializedViews. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String alias;

    /** TBC */
    @Attribute
    Long columnCount;

    /** TBC */
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

    /** TBC */
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

    /** TBC */
    @Attribute
    Boolean isTemporary;

    /** TBC */
    @Attribute
    Long lastProfiledAt;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

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

    /** TBC */
    @Attribute
    String refreshMethod;

    /** TBC */
    @Attribute
    String refreshMode;

    /** TBC */
    @Attribute
    Long rowCount;

    /** TBC */
    @Attribute
    @JsonProperty("atlanSchema")
    ISchema schema;

    /** TBC */
    @Attribute
    String schemaName;

    /** TBC */
    @Attribute
    String schemaQualifiedName;

    /** TBC */
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
    Long staleSinceDate;

    /** TBC */
    @Attribute
    String staleness;

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
     * Start an asset filter that will return all MaterializedView assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MaterializedView assets will be included.
     *
     * @return an asset filter that includes all MaterializedView assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(false);
    }

    /**
     * Start an asset filter that will return all MaterializedView assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) MaterializedViews will be included
     * @return an asset filter that includes all MaterializedView assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder = AssetFilter.builder().filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a MaterializedView by GUID.
     *
     * @param guid the GUID of the MaterializedView to reference
     * @return reference to a MaterializedView that can be used for defining a relationship to a MaterializedView
     */
    public static MaterializedView refByGuid(String guid) {
        return MaterializedView.builder().guid(guid).build();
    }

    /**
     * Reference to a MaterializedView by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MaterializedView to reference
     * @return reference to a MaterializedView that can be used for defining a relationship to a MaterializedView
     */
    public static MaterializedView refByQualifiedName(String qualifiedName) {
        return MaterializedView.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MaterializedView by its GUID, complete with all of its relationships.
     *
     * @param guid of the MaterializedView to retrieve
     * @return the requested full MaterializedView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MaterializedView does not exist or the provided GUID is not a MaterializedView
     */
    public static MaterializedView retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a MaterializedView by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the MaterializedView to retrieve
     * @return the requested full MaterializedView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MaterializedView does not exist or the provided GUID is not a MaterializedView
     */
    public static MaterializedView retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MaterializedView) {
            return (MaterializedView) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MaterializedView");
        }
    }

    /**
     * Retrieves a MaterializedView by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MaterializedView to retrieve
     * @return the requested full MaterializedView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MaterializedView does not exist
     */
    public static MaterializedView retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a MaterializedView by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the MaterializedView to retrieve
     * @return the requested full MaterializedView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MaterializedView does not exist
     */
    public static MaterializedView retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof MaterializedView) {
            return (MaterializedView) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MaterializedView");
        }
    }

    /**
     * Restore the archived (soft-deleted) MaterializedView to active.
     *
     * @param qualifiedName for the MaterializedView
     * @return true if the MaterializedView is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) MaterializedView to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MaterializedView
     * @return true if the MaterializedView is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a materialized view.
     *
     * @param name of the materialized view
     * @param schemaQualifiedName unique name of the schema in which this materialized view exists
     * @return the minimal request necessary to create the materialized view, as a builder
     */
    public static MaterializedViewBuilder<?, ?> creator(String name, String schemaQualifiedName) {
        String[] tokens = schemaQualifiedName.split("/");
        AtlanConnectorType connectorType = Connection.getConnectorTypeFromQualifiedName(tokens);
        String schemaName = StringUtils.getNameFromQualifiedName(schemaQualifiedName);
        String databaseQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(schemaQualifiedName);
        String databaseName = StringUtils.getNameFromQualifiedName(databaseQualifiedName);
        String connectionQualifiedName = StringUtils.getParentQualifiedNameFromQualifiedName(databaseQualifiedName);
        return MaterializedView.builder()
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
     * Generate a unique materialized view name.
     *
     * @param name of the materialized view
     * @param schemaQualifiedName unique name of the schema in which this materialized view exists
     * @return a unique name for the materialized view
     */
    public static String generateQualifiedName(String name, String schemaQualifiedName) {
        return schemaQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the minimal request necessary to update the MaterializedView, as a builder
     */
    public static MaterializedViewBuilder<?, ?> updater(String qualifiedName, String name) {
        return MaterializedView.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MaterializedView, from a potentially
     * more-complete MaterializedView object.
     *
     * @return the minimal object necessary to update the MaterializedView, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MaterializedView are not found in the initial object
     */
    @Override
    public MaterializedViewBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MaterializedView", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MaterializedView) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MaterializedView) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MaterializedView's owners
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MaterializedView) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MaterializedView, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant on which to update the MaterializedView's certificate
     * @param qualifiedName of the MaterializedView
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MaterializedView, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MaterializedView)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MaterializedView's certificate
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MaterializedView) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant on which to update the MaterializedView's announcement
     * @param qualifiedName of the MaterializedView
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MaterializedView)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a MaterializedView.
     *
     * @param client connectivity to the Atlan client from which to remove the MaterializedView's announcement
     * @param qualifiedName of the MaterializedView
     * @param name of the MaterializedView
     * @return the updated MaterializedView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MaterializedView) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MaterializedView.
     *
     * @param qualifiedName for the MaterializedView
     * @param name human-readable name of the MaterializedView
     * @param terms the list of terms to replace on the MaterializedView, or null to remove all terms from the MaterializedView
     * @return the MaterializedView that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the MaterializedView.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MaterializedView's assigned terms
     * @param qualifiedName for the MaterializedView
     * @param name human-readable name of the MaterializedView
     * @param terms the list of terms to replace on the MaterializedView, or null to remove all terms from the MaterializedView
     * @return the MaterializedView that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MaterializedView) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MaterializedView, without replacing existing terms linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MaterializedView
     * @param terms the list of terms to append to the MaterializedView
     * @return the MaterializedView that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the MaterializedView, without replacing existing terms linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MaterializedView
     * @param qualifiedName for the MaterializedView
     * @param terms the list of terms to append to the MaterializedView
     * @return the MaterializedView that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MaterializedView) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MaterializedView, without replacing all existing terms linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MaterializedView
     * @param terms the list of terms to remove from the MaterializedView, which must be referenced by GUID
     * @return the MaterializedView that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a MaterializedView, without replacing all existing terms linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MaterializedView
     * @param qualifiedName for the MaterializedView
     * @param terms the list of terms to remove from the MaterializedView, which must be referenced by GUID
     * @return the MaterializedView that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MaterializedView removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MaterializedView) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MaterializedView, without replacing existing Atlan tags linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MaterializedView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MaterializedView
     */
    public static MaterializedView appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MaterializedView, without replacing existing Atlan tags linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MaterializedView
     * @param qualifiedName of the MaterializedView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MaterializedView
     */
    public static MaterializedView appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (MaterializedView) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MaterializedView, without replacing existing Atlan tags linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MaterializedView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MaterializedView
     */
    public static MaterializedView appendAtlanTags(
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
     * Add Atlan tags to a MaterializedView, without replacing existing Atlan tags linked to the MaterializedView.
     * Note: this operation must make two API calls — one to retrieve the MaterializedView's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MaterializedView
     * @param qualifiedName of the MaterializedView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MaterializedView
     */
    public static MaterializedView appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MaterializedView) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MaterializedView
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MaterializedView
     * @param qualifiedName of the MaterializedView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MaterializedView
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MaterializedView
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
     * Add Atlan tags to a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MaterializedView
     * @param qualifiedName of the MaterializedView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MaterializedView
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
     * Remove an Atlan tag from a MaterializedView.
     *
     * @param qualifiedName of the MaterializedView
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MaterializedView
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a MaterializedView.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MaterializedView
     * @param qualifiedName of the MaterializedView
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MaterializedView
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
