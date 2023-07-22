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
import com.atlan.util.QueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a QuickSight Analysis sheet visual in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class QuickSightAnalysisVisual extends Asset
        implements IQuickSightAnalysisVisual, IQuickSight, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "QuickSightAnalysisVisual";

    /** Fixed typeName for QuickSightAnalysisVisuals. */
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
    IQuickSightAnalysis quickSightAnalysis;

    /** Qualified name of the QuickSight Analysis */
    @Attribute
    String quickSightAnalysisQualifiedName;

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
     * Start an asset filter that will return all QuickSightAnalysisVisual assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QuickSightAnalysisVisual assets will be included.
     *
     * @return an asset filter that includes all QuickSightAnalysisVisual assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all QuickSightAnalysisVisual assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) QuickSightAnalysisVisual assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all QuickSightAnalysisVisual assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all QuickSightAnalysisVisual assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) QuickSightAnalysisVisuals will be included
     * @return an asset filter that includes all QuickSightAnalysisVisual assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all QuickSightAnalysisVisual assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) QuickSightAnalysisVisuals will be included
     * @return an asset filter that includes all QuickSightAnalysisVisual assets
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
     * Reference to a QuickSightAnalysisVisual by GUID.
     *
     * @param guid the GUID of the QuickSightAnalysisVisual to reference
     * @return reference to a QuickSightAnalysisVisual that can be used for defining a relationship to a QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual refByGuid(String guid) {
        return QuickSightAnalysisVisual.builder().guid(guid).build();
    }

    /**
     * Reference to a QuickSightAnalysisVisual by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the QuickSightAnalysisVisual to reference
     * @return reference to a QuickSightAnalysisVisual that can be used for defining a relationship to a QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual refByQualifiedName(String qualifiedName) {
        return QuickSightAnalysisVisual.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a QuickSightAnalysisVisual by its GUID, complete with all of its relationships.
     *
     * @param guid of the QuickSightAnalysisVisual to retrieve
     * @return the requested full QuickSightAnalysisVisual, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightAnalysisVisual does not exist or the provided GUID is not a QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a QuickSightAnalysisVisual by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the QuickSightAnalysisVisual to retrieve
     * @return the requested full QuickSightAnalysisVisual, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightAnalysisVisual does not exist or the provided GUID is not a QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof QuickSightAnalysisVisual) {
            return (QuickSightAnalysisVisual) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "QuickSightAnalysisVisual");
        }
    }

    /**
     * Retrieves a QuickSightAnalysisVisual by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual to retrieve
     * @return the requested full QuickSightAnalysisVisual, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightAnalysisVisual does not exist
     */
    public static QuickSightAnalysisVisual retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a QuickSightAnalysisVisual by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the QuickSightAnalysisVisual to retrieve
     * @return the requested full QuickSightAnalysisVisual, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the QuickSightAnalysisVisual does not exist
     */
    public static QuickSightAnalysisVisual retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof QuickSightAnalysisVisual) {
            return (QuickSightAnalysisVisual) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "QuickSightAnalysisVisual");
        }
    }

    /**
     * Restore the archived (soft-deleted) QuickSightAnalysisVisual to active.
     *
     * @param qualifiedName for the QuickSightAnalysisVisual
     * @return true if the QuickSightAnalysisVisual is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) QuickSightAnalysisVisual to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the QuickSightAnalysisVisual
     * @return true if the QuickSightAnalysisVisual is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the minimal request necessary to update the QuickSightAnalysisVisual, as a builder
     */
    public static QuickSightAnalysisVisualBuilder<?, ?> updater(String qualifiedName, String name) {
        return QuickSightAnalysisVisual.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a QuickSightAnalysisVisual, from a potentially
     * more-complete QuickSightAnalysisVisual object.
     *
     * @return the minimal object necessary to update the QuickSightAnalysisVisual, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for QuickSightAnalysisVisual are not found in the initial object
     */
    @Override
    public QuickSightAnalysisVisualBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "QuickSightAnalysisVisual", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a QuickSightAnalysisVisual.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeUserDescription(String qualifiedName, String name)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a QuickSightAnalysisVisual.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a QuickSightAnalysisVisual.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QuickSightAnalysisVisual's owners
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightAnalysisVisual, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a QuickSightAnalysisVisual.
     *
     * @param client connectivity to the Atlan tenant on which to update the QuickSightAnalysisVisual's certificate
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated QuickSightAnalysisVisual, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (QuickSightAnalysisVisual)
                Asset.updateCertificate(client, builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a QuickSightAnalysisVisual.
     *
     * @param client connectivity to the Atlan tenant from which to remove the QuickSightAnalysisVisual's certificate
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a QuickSightAnalysisVisual.
     *
     * @param client connectivity to the Atlan tenant on which to update the QuickSightAnalysisVisual's announcement
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (QuickSightAnalysisVisual)
                Asset.updateAnnouncement(client, builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a QuickSightAnalysisVisual.
     *
     * @param client connectivity to the Atlan client from which to remove the QuickSightAnalysisVisual's announcement
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param name of the QuickSightAnalysisVisual
     * @return the updated QuickSightAnalysisVisual, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the QuickSightAnalysisVisual.
     *
     * @param qualifiedName for the QuickSightAnalysisVisual
     * @param name human-readable name of the QuickSightAnalysisVisual
     * @param terms the list of terms to replace on the QuickSightAnalysisVisual, or null to remove all terms from the QuickSightAnalysisVisual
     * @return the QuickSightAnalysisVisual that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the QuickSightAnalysisVisual.
     *
     * @param client connectivity to the Atlan tenant on which to replace the QuickSightAnalysisVisual's assigned terms
     * @param qualifiedName for the QuickSightAnalysisVisual
     * @param name human-readable name of the QuickSightAnalysisVisual
     * @param terms the list of terms to replace on the QuickSightAnalysisVisual, or null to remove all terms from the QuickSightAnalysisVisual
     * @return the QuickSightAnalysisVisual that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the QuickSightAnalysisVisual, without replacing existing terms linked to the QuickSightAnalysisVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysisVisual's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the QuickSightAnalysisVisual
     * @param terms the list of terms to append to the QuickSightAnalysisVisual
     * @return the QuickSightAnalysisVisual that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the QuickSightAnalysisVisual, without replacing existing terms linked to the QuickSightAnalysisVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysisVisual's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the QuickSightAnalysisVisual
     * @param qualifiedName for the QuickSightAnalysisVisual
     * @param terms the list of terms to append to the QuickSightAnalysisVisual
     * @return the QuickSightAnalysisVisual that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual appendTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightAnalysisVisual, without replacing all existing terms linked to the QuickSightAnalysisVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysisVisual's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the QuickSightAnalysisVisual
     * @param terms the list of terms to remove from the QuickSightAnalysisVisual, which must be referenced by GUID
     * @return the QuickSightAnalysisVisual that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a QuickSightAnalysisVisual, without replacing all existing terms linked to the QuickSightAnalysisVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysisVisual's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the QuickSightAnalysisVisual
     * @param qualifiedName for the QuickSightAnalysisVisual
     * @param terms the list of terms to remove from the QuickSightAnalysisVisual, which must be referenced by GUID
     * @return the QuickSightAnalysisVisual that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static QuickSightAnalysisVisual removeTerms(
            AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysisVisual, without replacing existing Atlan tags linked to the QuickSightAnalysisVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysisVisual's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysisVisual, without replacing existing Atlan tags linked to the QuickSightAnalysisVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysisVisual's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QuickSightAnalysisVisual
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysisVisual, without replacing existing Atlan tags linked to the QuickSightAnalysisVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysisVisual's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual appendAtlanTags(
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
     * Add Atlan tags to a QuickSightAnalysisVisual, without replacing existing Atlan tags linked to the QuickSightAnalysisVisual.
     * Note: this operation must make two API calls — one to retrieve the QuickSightAnalysisVisual's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the QuickSightAnalysisVisual
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated QuickSightAnalysisVisual
     */
    public static QuickSightAnalysisVisual appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (QuickSightAnalysisVisual) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightAnalysisVisual
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysisVisual.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QuickSightAnalysisVisual
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightAnalysisVisual
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightAnalysisVisual
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
     * Add Atlan tags to a QuickSightAnalysisVisual.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the QuickSightAnalysisVisual
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the QuickSightAnalysisVisual
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
     * Remove an Atlan tag from a QuickSightAnalysisVisual.
     *
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QuickSightAnalysisVisual
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a QuickSightAnalysisVisual.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a QuickSightAnalysisVisual
     * @param qualifiedName of the QuickSightAnalysisVisual
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the QuickSightAnalysisVisual
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
