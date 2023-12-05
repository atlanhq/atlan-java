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
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Qlik sheet in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class QlikSheet extends Asset implements IQlikSheet, IQlik, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QlikSheet";

    /** Fixed typeName for QlikSheets. */
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

    /** Tasks from which this asset is output. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** Processes from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** App in which this sheet exists. */
    @Attribute
    IQlikApp qlikApp;

    /** Identifier of the app in which this asset belongs, from Qlik. */
    @Attribute
    String qlikAppId;

    /** Unique name of the app where this asset belongs. */
    @Attribute
    String qlikAppQualifiedName;

    /** Charts that exist within this sheet. */
    @Attribute
    @Singular
    SortedSet<IQlikChart> qlikCharts;

    /** Identifier of this asset, from Qlik. */
    @Attribute
    String qlikId;

    /** Whether this asset is published in Qlik (true) or not (false). */
    @Attribute
    Boolean qlikIsPublished;

    /** Identifier of the owner of this asset, in Qlik. */
    @Attribute
    String qlikOwnerId;

    /** Unique QRI of this asset, from Qlik. */
    @Attribute
    String qlikQRI;

    /** Whether this is approved (true) or not (false). */
    @Attribute
    Boolean qlikSheetIsApproved;

    /** Identifier of the space in which this asset exists, from Qlik. */
    @Attribute
    String qlikSpaceId;

    /** Unique name of the space in which this asset exists. */
    @Attribute
    String qlikSpaceQualifiedName;

    /**
     * Builds the minimal object necessary to create a relationship to a QlikSheet, from a potentially
     * more-complete QlikSheet object.
     *
     * @return the minimal object necessary to relate to the QlikSheet
     * @throws InvalidRequestException if any of the minimal set of required properties for a QlikSheet relationship are not found in the initial object
     */
    @Override
    public QlikSheet trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all QlikSheet assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QlikSheet assets will be included.
     *
     * @return a fluent search that includes all QlikSheet assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
    }

    /**
     * Start a fluent search that will return all QlikSheet assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QlikSheet assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all QlikSheet assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all QlikSheet assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) QlikSheets will be included
     * @return a fluent search that includes all QlikSheet assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start a fluent search that will return all QlikSheet assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) QlikSheets will be included
     * @return a fluent search that includes all QlikSheet assets
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
     * Start an asset filter that will return all QlikSheet assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QlikSheet assets will be included.
     *
     * @return an asset filter that includes all QlikSheet assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all QlikSheet assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QlikSheet assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all QlikSheet assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all QlikSheet assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) QlikSheets will be included
     * @return an asset filter that includes all QlikSheet assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all QlikSheet assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) QlikSheets will be included
     * @return an asset filter that includes all QlikSheet assets
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
     * Reference to a QlikSheet by GUID.
     *
     * @param guid the GUID of the QlikSheet to reference
     * @return reference to a QlikSheet that can be used for defining a relationship to a QlikSheet
     */
    public static QlikSheet refByGuid(String guid) {
        return QlikSheet._internal().guid(guid).build();
    }

    /**
     * Reference to a QlikSheet by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QlikSheet to reference
     * @return reference to a QlikSheet that can be used for defining a relationship to a QlikSheet
     */
    public static QlikSheet refByQualifiedName(String qualifiedName) {
        return QlikSheet._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QlikSheet by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the QlikSheet to retrieve, either its GUID or its full qualifiedName
     * @return the requested full QlikSheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSheet does not exist or the provided GUID is not a QlikSheet
     */
    @JsonIgnore
    public static QlikSheet get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a QlikSheet by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QlikSheet to retrieve, either its GUID or its full qualifiedName
     * @return the requested full QlikSheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSheet does not exist or the provided GUID is not a QlikSheet
     */
    @JsonIgnore
    public static QlikSheet get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a QlikSheet by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QlikSheet to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full QlikSheet, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSheet does not exist or the provided GUID is not a QlikSheet
     */
    @JsonIgnore
    public static QlikSheet get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof QlikSheet) {
                return (QlikSheet) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof QlikSheet) {
                return (QlikSheet) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a QlikSheet by its GUID, complete with all of its relationships.
     *
     * @param guid of the QlikSheet to retrieve
     * @return the requested full QlikSheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSheet does not exist or the provided GUID is not a QlikSheet
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static QlikSheet retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a QlikSheet by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the QlikSheet to retrieve
     * @return the requested full QlikSheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSheet does not exist or the provided GUID is not a QlikSheet
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static QlikSheet retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a QlikSheet by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QlikSheet to retrieve
     * @return the requested full QlikSheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSheet does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static QlikSheet retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a QlikSheet by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the QlikSheet to retrieve
     * @return the requested full QlikSheet, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QlikSheet does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static QlikSheet retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) QlikSheet to active.
     *
     * @param qualifiedName for the QlikSheet
     * @return true if the QlikSheet is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) QlikSheet to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the QlikSheet
     * @return true if the QlikSheet is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QlikSheet.
     *
     * @param qualifiedName of the QlikSheet
     * @param name of the QlikSheet
     * @return the minimal request necessary to update the QlikSheet, as a builder
     */
    public static QlikSheetBuilder<?, ?> updater(String qualifiedName, String name) {
        return QlikSheet._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QlikSheet, from a potentially
     * more-complete QlikSheet object.
     *
     * @return the minimal object necessary to update the QlikSheet, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QlikSheet are not found in the initial object
     */
    @Override
    public QlikSheetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QlikSheet", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QlikSheet.
     *
     * @param qualifiedName of the QlikSheet
     * @param name of the QlikSheet
     * @return the updated QlikSheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a QlikSheet.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QlikSheet
     * @param name of the QlikSheet
     * @return the updated QlikSheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikSheet) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QlikSheet.
     *
     * @param qualifiedName of the QlikSheet
     * @param name of the QlikSheet
     * @return the updated QlikSheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a QlikSheet.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QlikSheet
     * @param name of the QlikSheet
     * @return the updated QlikSheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikSheet) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QlikSheet.
     *
     * @param qualifiedName of the QlikSheet
     * @param name of the QlikSheet
     * @return the updated QlikSheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a QlikSheet.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QlikSheet's owners
     * @param qualifiedName of the QlikSheet
     * @param name of the QlikSheet
     * @return the updated QlikSheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (QlikSheet) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QlikSheet.
     *
     * @param qualifiedName of the QlikSheet
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikSheet, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a QlikSheet.
     *
     * @param client connectivity to the Atlan tenant on which to update the QlikSheet's certificate
     * @param qualifiedName of the QlikSheet
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QlikSheet, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (QlikSheet) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QlikSheet.
     *
     * @param qualifiedName of the QlikSheet
     * @param name of the QlikSheet
     * @return the updated QlikSheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a QlikSheet.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QlikSheet's certificate
     * @param qualifiedName of the QlikSheet
     * @param name of the QlikSheet
     * @return the updated QlikSheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikSheet) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QlikSheet.
     *
     * @param qualifiedName of the QlikSheet
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a QlikSheet.
     *
     * @param client connectivity to the Atlan tenant on which to update the QlikSheet's announcement
     * @param qualifiedName of the QlikSheet
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (QlikSheet)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QlikSheet.
     *
     * @param qualifiedName of the QlikSheet
     * @param name of the QlikSheet
     * @return the updated QlikSheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a QlikSheet.
     *
     * @param client connectivity to the Atlan client from which to remove the QlikSheet's announcement
     * @param qualifiedName of the QlikSheet
     * @param name of the QlikSheet
     * @return the updated QlikSheet, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QlikSheet removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QlikSheet) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QlikSheet.
     *
     * @param qualifiedName for the QlikSheet
     * @param name human-readable name of the QlikSheet
     * @param terms the list of terms to replace on the QlikSheet, or null to remove all terms from the QlikSheet
     * @return the QlikSheet that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikSheet replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the QlikSheet.
     *
     * @param client connectivity to the Atlan tenant on which to replace the QlikSheet's assigned terms
     * @param qualifiedName for the QlikSheet
     * @param name human-readable name of the QlikSheet
     * @param terms the list of terms to replace on the QlikSheet, or null to remove all terms from the QlikSheet
     * @return the QlikSheet that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QlikSheet replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (QlikSheet) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QlikSheet, without replacing existing terms linked to the QlikSheet.
     * Note: this operation must make two API calls — one to retrieve the QlikSheet's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QlikSheet
     * @param terms the list of terms to append to the QlikSheet
     * @return the QlikSheet that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QlikSheet appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the QlikSheet, without replacing existing terms linked to the QlikSheet.
     * Note: this operation must make two API calls — one to retrieve the QlikSheet's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the QlikSheet
     * @param qualifiedName for the QlikSheet
     * @param terms the list of terms to append to the QlikSheet
     * @return the QlikSheet that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QlikSheet appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QlikSheet) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikSheet, without replacing all existing terms linked to the QlikSheet.
     * Note: this operation must make two API calls — one to retrieve the QlikSheet's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QlikSheet
     * @param terms the list of terms to remove from the QlikSheet, which must be referenced by GUID
     * @return the QlikSheet that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QlikSheet removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a QlikSheet, without replacing all existing terms linked to the QlikSheet.
     * Note: this operation must make two API calls — one to retrieve the QlikSheet's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the QlikSheet
     * @param qualifiedName for the QlikSheet
     * @param terms the list of terms to remove from the QlikSheet, which must be referenced by GUID
     * @return the QlikSheet that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QlikSheet removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (QlikSheet) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QlikSheet, without replacing existing Atlan tags linked to the QlikSheet.
     * Note: this operation must make two API calls — one to retrieve the QlikSheet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QlikSheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QlikSheet
     */
    public static QlikSheet appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikSheet, without replacing existing Atlan tags linked to the QlikSheet.
     * Note: this operation must make two API calls — one to retrieve the QlikSheet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QlikSheet
     * @param qualifiedName of the QlikSheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QlikSheet
     */
    public static QlikSheet appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (QlikSheet) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikSheet, without replacing existing Atlan tags linked to the QlikSheet.
     * Note: this operation must make two API calls — one to retrieve the QlikSheet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QlikSheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QlikSheet
     */
    public static QlikSheet appendAtlanTags(
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
     * Add Atlan tags to a QlikSheet, without replacing existing Atlan tags linked to the QlikSheet.
     * Note: this operation must make two API calls — one to retrieve the QlikSheet's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QlikSheet
     * @param qualifiedName of the QlikSheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QlikSheet
     */
    public static QlikSheet appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QlikSheet) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a QlikSheet.
     *
     * @param qualifiedName of the QlikSheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikSheet
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikSheet.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QlikSheet
     * @param qualifiedName of the QlikSheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikSheet
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QlikSheet.
     *
     * @param qualifiedName of the QlikSheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikSheet
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
     * Add Atlan tags to a QlikSheet.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QlikSheet
     * @param qualifiedName of the QlikSheet
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QlikSheet
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
     * Remove an Atlan tag from a QlikSheet.
     *
     * @param qualifiedName of the QlikSheet
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QlikSheet
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a QlikSheet.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a QlikSheet
     * @param qualifiedName of the QlikSheet
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QlikSheet
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
