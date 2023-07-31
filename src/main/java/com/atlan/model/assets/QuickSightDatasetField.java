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
import com.atlan.model.enums.QuickSightDatasetFieldType;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.QueryFactory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a QuickSight Dataset field in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class QuickSightDatasetField extends Asset
        implements IQuickSightDatasetField, IQuickSight, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QuickSightDatasetField";

    /** Fixed typeName for QuickSightDatasetFields. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    IQuickSightDataset quickSightDataset;

    /** Datatype of column in the dataset */
    @Attribute
    QuickSightDatasetFieldType quickSightDatasetFieldType;

    /** Qualified name of the parent dataset */
    @Attribute
    String quickSightDatasetQualifiedName;

    /** TBC */
    @Attribute
    String quickSightId;

    /** TBC */
    @Attribute
    String quickSightSheetId;

    /** TBC */
    @Attribute
    String quickSightSheetName;

    /**
     * Start an asset filter that will return all QuickSightDatasetField assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QuickSightDatasetField assets will be included.
     *
     * @return an asset filter that includes all QuickSightDatasetField assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all QuickSightDatasetField assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QuickSightDatasetField assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all QuickSightDatasetField assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all QuickSightDatasetField assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) QuickSightDatasetFields will be included
     * @return an asset filter that includes all QuickSightDatasetField assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all QuickSightDatasetField assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) QuickSightDatasetFields will be included
     * @return an asset filter that includes all QuickSightDatasetField assets
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
     * Reference to a QuickSightDatasetField by GUID.
     *
     * @param guid the GUID of the QuickSightDatasetField to reference
     * @return reference to a QuickSightDatasetField that can be used for defining a relationship to a QuickSightDatasetField
     */
    public static QuickSightDatasetField refByGuid(String guid) {
        return QuickSightDatasetField.builder().guid(guid).build();
    }

    /**
     * Reference to a QuickSightDatasetField by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QuickSightDatasetField to reference
     * @return reference to a QuickSightDatasetField that can be used for defining a relationship to a QuickSightDatasetField
     */
    public static QuickSightDatasetField refByQualifiedName(String qualifiedName) {
        return QuickSightDatasetField.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QuickSightDatasetField by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the QuickSightDatasetField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full QuickSightDatasetField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDatasetField does not exist or the provided GUID is not a QuickSightDatasetField
     */
    @JsonIgnore
    public static QuickSightDatasetField get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a QuickSightDatasetField by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the QuickSightDatasetField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full QuickSightDatasetField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDatasetField does not exist or the provided GUID is not a QuickSightDatasetField
     */
    @JsonIgnore
    public static QuickSightDatasetField get(AtlanClient client, String id) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (id.startsWith("default")) {
            Asset asset = Asset.retrieveFull(client, TYPE_NAME, id);
            if (asset instanceof QuickSightDatasetField) {
                return (QuickSightDatasetField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "QuickSightDatasetField");
            }
        } else {
            Asset asset = Asset.retrieveFull(client, id);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof QuickSightDatasetField) {
                return (QuickSightDatasetField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "QuickSightDatasetField");
            }
        }
    }

    /**
     * Retrieves a QuickSightDatasetField by its GUID, complete with all of its relationships.
     *
     * @param guid of the QuickSightDatasetField to retrieve
     * @return the requested full QuickSightDatasetField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDatasetField does not exist or the provided GUID is not a QuickSightDatasetField
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static QuickSightDatasetField retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a QuickSightDatasetField by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the QuickSightDatasetField to retrieve
     * @return the requested full QuickSightDatasetField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDatasetField does not exist or the provided GUID is not a QuickSightDatasetField
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static QuickSightDatasetField retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QuickSightDatasetField) {
            return (QuickSightDatasetField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QuickSightDatasetField");
        }
    }

    /**
     * Retrieves a QuickSightDatasetField by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QuickSightDatasetField to retrieve
     * @return the requested full QuickSightDatasetField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDatasetField does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static QuickSightDatasetField retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a QuickSightDatasetField by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the QuickSightDatasetField to retrieve
     * @return the requested full QuickSightDatasetField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightDatasetField does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static QuickSightDatasetField retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof QuickSightDatasetField) {
            return (QuickSightDatasetField) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QuickSightDatasetField");
        }
    }

    /**
     * Restore the archived (soft-deleted) QuickSightDatasetField to active.
     *
     * @param qualifiedName for the QuickSightDatasetField
     * @return true if the QuickSightDatasetField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) QuickSightDatasetField to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the QuickSightDatasetField
     * @return true if the QuickSightDatasetField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the minimal request necessary to update the QuickSightDatasetField, as a builder
     */
    public static QuickSightDatasetFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return QuickSightDatasetField.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QuickSightDatasetField, from a potentially
     * more-complete QuickSightDatasetField object.
     *
     * @return the minimal object necessary to update the QuickSightDatasetField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QuickSightDatasetField are not found in the initial object
     */
    @Override
    public QuickSightDatasetFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QuickSightDatasetField", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a QuickSightDatasetField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDatasetField) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a QuickSightDatasetField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDatasetField) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a QuickSightDatasetField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QuickSightDatasetField's owners
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDatasetField) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightDatasetField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a QuickSightDatasetField.
     *
     * @param client connectivity to the Atlan tenant on which to update the QuickSightDatasetField's certificate
     * @param qualifiedName of the QuickSightDatasetField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightDatasetField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (QuickSightDatasetField)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a QuickSightDatasetField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QuickSightDatasetField's certificate
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDatasetField) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a QuickSightDatasetField.
     *
     * @param client connectivity to the Atlan tenant on which to update the QuickSightDatasetField's announcement
     * @param qualifiedName of the QuickSightDatasetField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (QuickSightDatasetField)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a QuickSightDatasetField.
     *
     * @param client connectivity to the Atlan client from which to remove the QuickSightDatasetField's announcement
     * @param qualifiedName of the QuickSightDatasetField
     * @param name of the QuickSightDatasetField
     * @return the updated QuickSightDatasetField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightDatasetField) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QuickSightDatasetField.
     *
     * @param qualifiedName for the QuickSightDatasetField
     * @param name human-readable name of the QuickSightDatasetField
     * @param terms the list of terms to replace on the QuickSightDatasetField, or null to remove all terms from the QuickSightDatasetField
     * @return the QuickSightDatasetField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the QuickSightDatasetField.
     *
     * @param client connectivity to the Atlan tenant on which to replace the QuickSightDatasetField's assigned terms
     * @param qualifiedName for the QuickSightDatasetField
     * @param name human-readable name of the QuickSightDatasetField
     * @param terms the list of terms to replace on the QuickSightDatasetField, or null to remove all terms from the QuickSightDatasetField
     * @return the QuickSightDatasetField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (QuickSightDatasetField) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QuickSightDatasetField, without replacing existing terms linked to the QuickSightDatasetField.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDatasetField's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QuickSightDatasetField
     * @param terms the list of terms to append to the QuickSightDatasetField
     * @return the QuickSightDatasetField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the QuickSightDatasetField, without replacing existing terms linked to the QuickSightDatasetField.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDatasetField's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the QuickSightDatasetField
     * @param qualifiedName for the QuickSightDatasetField
     * @param terms the list of terms to append to the QuickSightDatasetField
     * @return the QuickSightDatasetField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (QuickSightDatasetField) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightDatasetField, without replacing all existing terms linked to the QuickSightDatasetField.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDatasetField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QuickSightDatasetField
     * @param terms the list of terms to remove from the QuickSightDatasetField, which must be referenced by GUID
     * @return the QuickSightDatasetField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightDatasetField, without replacing all existing terms linked to the QuickSightDatasetField.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDatasetField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the QuickSightDatasetField
     * @param qualifiedName for the QuickSightDatasetField
     * @param terms the list of terms to remove from the QuickSightDatasetField, which must be referenced by GUID
     * @return the QuickSightDatasetField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightDatasetField removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (QuickSightDatasetField) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QuickSightDatasetField, without replacing existing Atlan tags linked to the QuickSightDatasetField.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDatasetField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightDatasetField
     */
    public static QuickSightDatasetField appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightDatasetField, without replacing existing Atlan tags linked to the QuickSightDatasetField.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDatasetField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QuickSightDatasetField
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightDatasetField
     */
    public static QuickSightDatasetField appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (QuickSightDatasetField) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightDatasetField, without replacing existing Atlan tags linked to the QuickSightDatasetField.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDatasetField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightDatasetField
     */
    public static QuickSightDatasetField appendAtlanTags(
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
     * Add Atlan tags to a QuickSightDatasetField, without replacing existing Atlan tags linked to the QuickSightDatasetField.
     * Note: this operation must make two API calls — one to retrieve the QuickSightDatasetField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QuickSightDatasetField
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightDatasetField
     */
    public static QuickSightDatasetField appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QuickSightDatasetField) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightDatasetField
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightDatasetField.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QuickSightDatasetField
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightDatasetField
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightDatasetField
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
     * Add Atlan tags to a QuickSightDatasetField.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QuickSightDatasetField
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightDatasetField
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
     * Remove an Atlan tag from a QuickSightDatasetField.
     *
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QuickSightDatasetField
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a QuickSightDatasetField.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a QuickSightDatasetField
     * @param qualifiedName of the QuickSightDatasetField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QuickSightDatasetField
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
