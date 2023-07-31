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
import java.util.Map;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a MicroStrategy document in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class MicroStrategyDocument extends Asset
        implements IMicroStrategyDocument, IMicroStrategy, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "MicroStrategyDocument";

    /** Fixed typeName for MicroStrategyDocuments. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    Long microStrategyCertifiedAt;

    /** TBC */
    @Attribute
    String microStrategyCertifiedBy;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> microStrategyCubeNames;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> microStrategyCubeQualifiedNames;

    /** TBC */
    @Attribute
    Boolean microStrategyIsCertified;

    /** TBC */
    @Attribute
    @Singular("putMicroStrategyLocation")
    List<Map<String, String>> microStrategyLocation;

    /** Project containing the document. */
    @Attribute
    IMicroStrategyProject microStrategyProject;

    /** TBC */
    @Attribute
    String microStrategyProjectName;

    /** TBC */
    @Attribute
    String microStrategyProjectQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> microStrategyReportNames;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> microStrategyReportQualifiedNames;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Start an asset filter that will return all MicroStrategyDocument assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MicroStrategyDocument assets will be included.
     *
     * @return an asset filter that includes all MicroStrategyDocument assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all MicroStrategyDocument assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) MicroStrategyDocument assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all MicroStrategyDocument assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all MicroStrategyDocument assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) MicroStrategyDocuments will be included
     * @return an asset filter that includes all MicroStrategyDocument assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all MicroStrategyDocument assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) MicroStrategyDocuments will be included
     * @return an asset filter that includes all MicroStrategyDocument assets
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
     * Reference to a MicroStrategyDocument by GUID.
     *
     * @param guid the GUID of the MicroStrategyDocument to reference
     * @return reference to a MicroStrategyDocument that can be used for defining a relationship to a MicroStrategyDocument
     */
    public static MicroStrategyDocument refByGuid(String guid) {
        return MicroStrategyDocument._internal().guid(guid).build();
    }

    /**
     * Reference to a MicroStrategyDocument by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the MicroStrategyDocument to reference
     * @return reference to a MicroStrategyDocument that can be used for defining a relationship to a MicroStrategyDocument
     */
    public static MicroStrategyDocument refByQualifiedName(String qualifiedName) {
        return MicroStrategyDocument._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a MicroStrategyDocument by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the MicroStrategyDocument to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MicroStrategyDocument, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyDocument does not exist or the provided GUID is not a MicroStrategyDocument
     */
    @JsonIgnore
    public static MicroStrategyDocument get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a MicroStrategyDocument by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the MicroStrategyDocument to retrieve, either its GUID or its full qualifiedName
     * @return the requested full MicroStrategyDocument, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyDocument does not exist or the provided GUID is not a MicroStrategyDocument
     */
    @JsonIgnore
    public static MicroStrategyDocument get(AtlanClient client, String id) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.retrieveFull(client, id);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof MicroStrategyDocument) {
                return (MicroStrategyDocument) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "MicroStrategyDocument");
            }
        } else {
            Asset asset = Asset.retrieveFull(client, TYPE_NAME, id);
            if (asset instanceof MicroStrategyDocument) {
                return (MicroStrategyDocument) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "MicroStrategyDocument");
            }
        }
    }

    /**
     * Retrieves a MicroStrategyDocument by its GUID, complete with all of its relationships.
     *
     * @param guid of the MicroStrategyDocument to retrieve
     * @return the requested full MicroStrategyDocument, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyDocument does not exist or the provided GUID is not a MicroStrategyDocument
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static MicroStrategyDocument retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a MicroStrategyDocument by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the MicroStrategyDocument to retrieve
     * @return the requested full MicroStrategyDocument, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyDocument does not exist or the provided GUID is not a MicroStrategyDocument
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static MicroStrategyDocument retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof MicroStrategyDocument) {
            return (MicroStrategyDocument) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "MicroStrategyDocument");
        }
    }

    /**
     * Retrieves a MicroStrategyDocument by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the MicroStrategyDocument to retrieve
     * @return the requested full MicroStrategyDocument, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyDocument does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static MicroStrategyDocument retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a MicroStrategyDocument by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the MicroStrategyDocument to retrieve
     * @return the requested full MicroStrategyDocument, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the MicroStrategyDocument does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static MicroStrategyDocument retrieveByQualifiedName(AtlanClient client, String qualifiedName)
            throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof MicroStrategyDocument) {
            return (MicroStrategyDocument) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "MicroStrategyDocument");
        }
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyDocument to active.
     *
     * @param qualifiedName for the MicroStrategyDocument
     * @return true if the MicroStrategyDocument is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) MicroStrategyDocument to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the MicroStrategyDocument
     * @return true if the MicroStrategyDocument is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a MicroStrategyDocument.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param name of the MicroStrategyDocument
     * @return the minimal request necessary to update the MicroStrategyDocument, as a builder
     */
    public static MicroStrategyDocumentBuilder<?, ?> updater(String qualifiedName, String name) {
        return MicroStrategyDocument._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a MicroStrategyDocument, from a potentially
     * more-complete MicroStrategyDocument object.
     *
     * @return the minimal object necessary to update the MicroStrategyDocument, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for MicroStrategyDocument are not found in the initial object
     */
    @Override
    public MicroStrategyDocumentBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "MicroStrategyDocument", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a MicroStrategyDocument.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param name of the MicroStrategyDocument
     * @return the updated MicroStrategyDocument, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a MicroStrategyDocument.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MicroStrategyDocument
     * @param name of the MicroStrategyDocument
     * @return the updated MicroStrategyDocument, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyDocument) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a MicroStrategyDocument.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param name of the MicroStrategyDocument
     * @return the updated MicroStrategyDocument, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a MicroStrategyDocument.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the MicroStrategyDocument
     * @param name of the MicroStrategyDocument
     * @return the updated MicroStrategyDocument, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyDocument) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a MicroStrategyDocument.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param name of the MicroStrategyDocument
     * @return the updated MicroStrategyDocument, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a MicroStrategyDocument.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MicroStrategyDocument's owners
     * @param qualifiedName of the MicroStrategyDocument
     * @param name of the MicroStrategyDocument
     * @return the updated MicroStrategyDocument, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyDocument) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a MicroStrategyDocument.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyDocument, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument updateCertificate(
            String qualifiedName, CertificateStatus certificate, String message) throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a MicroStrategyDocument.
     *
     * @param client connectivity to the Atlan tenant on which to update the MicroStrategyDocument's certificate
     * @param qualifiedName of the MicroStrategyDocument
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated MicroStrategyDocument, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (MicroStrategyDocument)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a MicroStrategyDocument.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param name of the MicroStrategyDocument
     * @return the updated MicroStrategyDocument, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a MicroStrategyDocument.
     *
     * @param client connectivity to the Atlan tenant from which to remove the MicroStrategyDocument's certificate
     * @param qualifiedName of the MicroStrategyDocument
     * @param name of the MicroStrategyDocument
     * @return the updated MicroStrategyDocument, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyDocument) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a MicroStrategyDocument.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a MicroStrategyDocument.
     *
     * @param client connectivity to the Atlan tenant on which to update the MicroStrategyDocument's announcement
     * @param qualifiedName of the MicroStrategyDocument
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (MicroStrategyDocument)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a MicroStrategyDocument.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param name of the MicroStrategyDocument
     * @return the updated MicroStrategyDocument, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a MicroStrategyDocument.
     *
     * @param client connectivity to the Atlan client from which to remove the MicroStrategyDocument's announcement
     * @param qualifiedName of the MicroStrategyDocument
     * @param name of the MicroStrategyDocument
     * @return the updated MicroStrategyDocument, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (MicroStrategyDocument) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the MicroStrategyDocument.
     *
     * @param qualifiedName for the MicroStrategyDocument
     * @param name human-readable name of the MicroStrategyDocument
     * @param terms the list of terms to replace on the MicroStrategyDocument, or null to remove all terms from the MicroStrategyDocument
     * @return the MicroStrategyDocument that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the MicroStrategyDocument.
     *
     * @param client connectivity to the Atlan tenant on which to replace the MicroStrategyDocument's assigned terms
     * @param qualifiedName for the MicroStrategyDocument
     * @param name human-readable name of the MicroStrategyDocument
     * @param terms the list of terms to replace on the MicroStrategyDocument, or null to remove all terms from the MicroStrategyDocument
     * @return the MicroStrategyDocument that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (MicroStrategyDocument) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the MicroStrategyDocument, without replacing existing terms linked to the MicroStrategyDocument.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDocument's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the MicroStrategyDocument
     * @param terms the list of terms to append to the MicroStrategyDocument
     * @return the MicroStrategyDocument that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument appendTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the MicroStrategyDocument, without replacing existing terms linked to the MicroStrategyDocument.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDocument's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the MicroStrategyDocument
     * @param qualifiedName for the MicroStrategyDocument
     * @param terms the list of terms to append to the MicroStrategyDocument
     * @return the MicroStrategyDocument that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyDocument) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyDocument, without replacing all existing terms linked to the MicroStrategyDocument.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDocument's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the MicroStrategyDocument
     * @param terms the list of terms to remove from the MicroStrategyDocument, which must be referenced by GUID
     * @return the MicroStrategyDocument that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument removeTerms(String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a MicroStrategyDocument, without replacing all existing terms linked to the MicroStrategyDocument.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDocument's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the MicroStrategyDocument
     * @param qualifiedName for the MicroStrategyDocument
     * @param terms the list of terms to remove from the MicroStrategyDocument, which must be referenced by GUID
     * @return the MicroStrategyDocument that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static MicroStrategyDocument removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (MicroStrategyDocument) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a MicroStrategyDocument, without replacing existing Atlan tags linked to the MicroStrategyDocument.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDocument's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyDocument
     */
    public static MicroStrategyDocument appendAtlanTags(String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyDocument, without replacing existing Atlan tags linked to the MicroStrategyDocument.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDocument's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MicroStrategyDocument
     * @param qualifiedName of the MicroStrategyDocument
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyDocument
     */
    public static MicroStrategyDocument appendAtlanTags(
            AtlanClient client, String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return (MicroStrategyDocument) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyDocument, without replacing existing Atlan tags linked to the MicroStrategyDocument.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDocument's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyDocument
     */
    public static MicroStrategyDocument appendAtlanTags(
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
     * Add Atlan tags to a MicroStrategyDocument, without replacing existing Atlan tags linked to the MicroStrategyDocument.
     * Note: this operation must make two API calls — one to retrieve the MicroStrategyDocument's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the MicroStrategyDocument
     * @param qualifiedName of the MicroStrategyDocument
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated MicroStrategyDocument
     */
    public static MicroStrategyDocument appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (MicroStrategyDocument) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a MicroStrategyDocument.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyDocument
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyDocument.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MicroStrategyDocument
     * @param qualifiedName of the MicroStrategyDocument
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyDocument
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a MicroStrategyDocument.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyDocument
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
     * Add Atlan tags to a MicroStrategyDocument.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the MicroStrategyDocument
     * @param qualifiedName of the MicroStrategyDocument
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the MicroStrategyDocument
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
     * Remove an Atlan tag from a MicroStrategyDocument.
     *
     * @param qualifiedName of the MicroStrategyDocument
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyDocument
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a MicroStrategyDocument.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a MicroStrategyDocument
     * @param qualifiedName of the MicroStrategyDocument
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the MicroStrategyDocument
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
