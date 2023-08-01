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
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Sigma data element field in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class SigmaDataElementField extends Asset
        implements ISigmaDataElementField, ISigma, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SigmaDataElementField";

    /** Fixed typeName for SigmaDataElementFields. */
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

    /** Data element that contains this data element field. */
    @Attribute
    ISigmaDataElement sigmaDataElement;

    /** TBC */
    @Attribute
    String sigmaDataElementFieldFormula;

    /** TBC */
    @Attribute
    Boolean sigmaDataElementFieldIsHidden;

    /** TBC */
    @Attribute
    String sigmaDataElementName;

    /** TBC */
    @Attribute
    String sigmaDataElementQualifiedName;

    /** TBC */
    @Attribute
    String sigmaPageName;

    /** TBC */
    @Attribute
    String sigmaPageQualifiedName;

    /** TBC */
    @Attribute
    String sigmaWorkbookName;

    /** TBC */
    @Attribute
    String sigmaWorkbookQualifiedName;

    /**
     * Start an asset filter that will return all SigmaDataElementField assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SigmaDataElementField assets will be included.
     *
     * @return an asset filter that includes all SigmaDataElementField assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all SigmaDataElementField assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SigmaDataElementField assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all SigmaDataElementField assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all SigmaDataElementField assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) SigmaDataElementFields will be included
     * @return an asset filter that includes all SigmaDataElementField assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all SigmaDataElementField assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SigmaDataElementFields will be included
     * @return an asset filter that includes all SigmaDataElementField assets
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
     * Reference to a SigmaDataElementField by GUID.
     *
     * @param guid the GUID of the SigmaDataElementField to reference
     * @return reference to a SigmaDataElementField that can be used for defining a relationship to a SigmaDataElementField
     */
    public static SigmaDataElementField refByGuid(String guid) {
        return SigmaDataElementField._internal().guid(guid).build();
    }

    /**
     * Reference to a SigmaDataElementField by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the SigmaDataElementField to reference
     * @return reference to a SigmaDataElementField that can be used for defining a relationship to a SigmaDataElementField
     */
    public static SigmaDataElementField refByQualifiedName(String qualifiedName) {
        return SigmaDataElementField._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a SigmaDataElementField by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the SigmaDataElementField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SigmaDataElementField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElementField does not exist or the provided GUID is not a SigmaDataElementField
     */
    @JsonIgnore
    public static SigmaDataElementField get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a SigmaDataElementField by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SigmaDataElementField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SigmaDataElementField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElementField does not exist or the provided GUID is not a SigmaDataElementField
     */
    @JsonIgnore
    public static SigmaDataElementField get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a SigmaDataElementField by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SigmaDataElementField to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SigmaDataElementField, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElementField does not exist or the provided GUID is not a SigmaDataElementField
     */
    @JsonIgnore
    public static SigmaDataElementField get(AtlanClient client, String id, boolean includeRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SigmaDataElementField) {
                return (SigmaDataElementField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "SigmaDataElementField");
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof SigmaDataElementField) {
                return (SigmaDataElementField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "SigmaDataElementField");
            }
        }
    }

    /**
     * Retrieves a SigmaDataElementField by its GUID, complete with all of its relationships.
     *
     * @param guid of the SigmaDataElementField to retrieve
     * @return the requested full SigmaDataElementField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElementField does not exist or the provided GUID is not a SigmaDataElementField
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static SigmaDataElementField retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a SigmaDataElementField by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the SigmaDataElementField to retrieve
     * @return the requested full SigmaDataElementField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElementField does not exist or the provided GUID is not a SigmaDataElementField
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static SigmaDataElementField retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a SigmaDataElementField by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the SigmaDataElementField to retrieve
     * @return the requested full SigmaDataElementField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElementField does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static SigmaDataElementField retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a SigmaDataElementField by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the SigmaDataElementField to retrieve
     * @return the requested full SigmaDataElementField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SigmaDataElementField does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static SigmaDataElementField retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) SigmaDataElementField to active.
     *
     * @param qualifiedName for the SigmaDataElementField
     * @return true if the SigmaDataElementField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) SigmaDataElementField to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SigmaDataElementField
     * @return true if the SigmaDataElementField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the minimal request necessary to update the SigmaDataElementField, as a builder
     */
    public static SigmaDataElementFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return SigmaDataElementField._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SigmaDataElementField, from a potentially
     * more-complete SigmaDataElementField object.
     *
     * @return the minimal object necessary to update the SigmaDataElementField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for SigmaDataElementField are not found in the initial object
     */
    @Override
    public SigmaDataElementFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "SigmaDataElementField", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a SigmaDataElementField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataElementField) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a SigmaDataElementField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataElementField) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a SigmaDataElementField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SigmaDataElementField's owners
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataElementField) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SigmaDataElementField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a SigmaDataElementField.
     *
     * @param client connectivity to the Atlan tenant on which to update the SigmaDataElementField's certificate
     * @param qualifiedName of the SigmaDataElementField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SigmaDataElementField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SigmaDataElementField)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a SigmaDataElementField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SigmaDataElementField's certificate
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataElementField) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a SigmaDataElementField.
     *
     * @param client connectivity to the Atlan tenant on which to update the SigmaDataElementField's announcement
     * @param qualifiedName of the SigmaDataElementField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SigmaDataElementField)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a SigmaDataElementField.
     *
     * @param client connectivity to the Atlan client from which to remove the SigmaDataElementField's announcement
     * @param qualifiedName of the SigmaDataElementField
     * @param name of the SigmaDataElementField
     * @return the updated SigmaDataElementField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SigmaDataElementField) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SigmaDataElementField.
     *
     * @param qualifiedName for the SigmaDataElementField
     * @param name human-readable name of the SigmaDataElementField
     * @param terms the list of terms to replace on the SigmaDataElementField, or null to remove all terms from the SigmaDataElementField
     * @return the SigmaDataElementField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the SigmaDataElementField.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SigmaDataElementField's assigned terms
     * @param qualifiedName for the SigmaDataElementField
     * @param name human-readable name of the SigmaDataElementField
     * @param terms the list of terms to replace on the SigmaDataElementField, or null to remove all terms from the SigmaDataElementField
     * @return the SigmaDataElementField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SigmaDataElementField) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SigmaDataElementField, without replacing existing terms linked to the SigmaDataElementField.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElementField's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the SigmaDataElementField
     * @param terms the list of terms to append to the SigmaDataElementField
     * @return the SigmaDataElementField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the SigmaDataElementField, without replacing existing terms linked to the SigmaDataElementField.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElementField's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SigmaDataElementField
     * @param qualifiedName for the SigmaDataElementField
     * @param terms the list of terms to append to the SigmaDataElementField
     * @return the SigmaDataElementField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SigmaDataElementField) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SigmaDataElementField, without replacing all existing terms linked to the SigmaDataElementField.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElementField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the SigmaDataElementField
     * @param terms the list of terms to remove from the SigmaDataElementField, which must be referenced by GUID
     * @return the SigmaDataElementField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a SigmaDataElementField, without replacing all existing terms linked to the SigmaDataElementField.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElementField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SigmaDataElementField
     * @param qualifiedName for the SigmaDataElementField
     * @param terms the list of terms to remove from the SigmaDataElementField, which must be referenced by GUID
     * @return the SigmaDataElementField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static SigmaDataElementField removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SigmaDataElementField) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SigmaDataElementField, without replacing existing Atlan tags linked to the SigmaDataElementField.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElementField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataElementField
     */
    public static SigmaDataElementField appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataElementField, without replacing existing Atlan tags linked to the SigmaDataElementField.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElementField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SigmaDataElementField
     * @param qualifiedName of the SigmaDataElementField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataElementField
     */
    public static SigmaDataElementField appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (SigmaDataElementField) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataElementField, without replacing existing Atlan tags linked to the SigmaDataElementField.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElementField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataElementField
     */
    public static SigmaDataElementField appendAtlanTags(
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
     * Add Atlan tags to a SigmaDataElementField, without replacing existing Atlan tags linked to the SigmaDataElementField.
     * Note: this operation must make two API calls — one to retrieve the SigmaDataElementField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SigmaDataElementField
     * @param qualifiedName of the SigmaDataElementField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SigmaDataElementField
     */
    public static SigmaDataElementField appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SigmaDataElementField) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDataElementField
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataElementField.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the SigmaDataElementField
     * @param qualifiedName of the SigmaDataElementField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDataElementField
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDataElementField
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
     * Add Atlan tags to a SigmaDataElementField.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the SigmaDataElementField
     * @param qualifiedName of the SigmaDataElementField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the SigmaDataElementField
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
     * Remove an Atlan tag from a SigmaDataElementField.
     *
     * @param qualifiedName of the SigmaDataElementField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SigmaDataElementField
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a SigmaDataElementField.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SigmaDataElementField
     * @param qualifiedName of the SigmaDataElementField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SigmaDataElementField
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
