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
 * Instance of a Looker look in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class LookerLook extends Asset implements ILookerLook, ILooker, IBI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "LookerLook";

    /** Fixed typeName for LookerLooks. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    ILookerDashboard dashboard;

    /** TBC */
    @Attribute
    ILookerFolder folder;

    /** TBC */
    @Attribute
    String folderName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    ILookerModel model;

    /** TBC */
    @Attribute
    String modelName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    ILookerQuery query;

    /** TBC */
    @Attribute
    Integer sourceContentMetadataId;

    /** TBC */
    @Attribute
    Long sourceLastAccessedAt;

    /** TBC */
    @Attribute
    Long sourceLastViewedAt;

    /** TBC */
    @Attribute
    Integer sourceQueryId;

    /** TBC */
    @Attribute
    Integer sourceUserId;

    /** TBC */
    @Attribute
    Integer sourceViewCount;

    /** TBC */
    @Attribute
    Integer sourcelastUpdaterId;

    /** TBC */
    @Attribute
    ILookerTile tile;

    /**
     * Builds the minimal object necessary to create a relationship to a LookerLook, from a potentially
     * more-complete LookerLook object.
     *
     * @return the minimal object necessary to relate to the LookerLook
     * @throws InvalidRequestException if any of the minimal set of required properties for a LookerLook relationship are not found in the initial object
     */
    @Override
    public LookerLook trimToReference() throws InvalidRequestException {
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
     * Start an asset filter that will return all LookerLook assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) LookerLook assets will be included.
     *
     * @return an asset filter that includes all LookerLook assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all LookerLook assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) LookerLook assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all LookerLook assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all LookerLook assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) LookerLooks will be included
     * @return an asset filter that includes all LookerLook assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all LookerLook assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) LookerLooks will be included
     * @return an asset filter that includes all LookerLook assets
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
     * Reference to a LookerLook by GUID.
     *
     * @param guid the GUID of the LookerLook to reference
     * @return reference to a LookerLook that can be used for defining a relationship to a LookerLook
     */
    public static LookerLook refByGuid(String guid) {
        return LookerLook._internal().guid(guid).build();
    }

    /**
     * Reference to a LookerLook by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the LookerLook to reference
     * @return reference to a LookerLook that can be used for defining a relationship to a LookerLook
     */
    public static LookerLook refByQualifiedName(String qualifiedName) {
        return LookerLook._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a LookerLook by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the LookerLook to retrieve, either its GUID or its full qualifiedName
     * @return the requested full LookerLook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist or the provided GUID is not a LookerLook
     */
    @JsonIgnore
    public static LookerLook get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a LookerLook by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LookerLook to retrieve, either its GUID or its full qualifiedName
     * @return the requested full LookerLook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist or the provided GUID is not a LookerLook
     */
    @JsonIgnore
    public static LookerLook get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a LookerLook by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the LookerLook to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full LookerLook, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist or the provided GUID is not a LookerLook
     */
    @JsonIgnore
    public static LookerLook get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof LookerLook) {
                return (LookerLook) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof LookerLook) {
                return (LookerLook) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a LookerLook by its GUID, complete with all of its relationships.
     *
     * @param guid of the LookerLook to retrieve
     * @return the requested full LookerLook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist or the provided GUID is not a LookerLook
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static LookerLook retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a LookerLook by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the LookerLook to retrieve
     * @return the requested full LookerLook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist or the provided GUID is not a LookerLook
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static LookerLook retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a LookerLook by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the LookerLook to retrieve
     * @return the requested full LookerLook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static LookerLook retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a LookerLook by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the LookerLook to retrieve
     * @return the requested full LookerLook, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the LookerLook does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static LookerLook retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) LookerLook to active.
     *
     * @param qualifiedName for the LookerLook
     * @return true if the LookerLook is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) LookerLook to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the LookerLook
     * @return true if the LookerLook is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the minimal request necessary to update the LookerLook, as a builder
     */
    public static LookerLookBuilder<?, ?> updater(String qualifiedName, String name) {
        return LookerLook._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a LookerLook, from a potentially
     * more-complete LookerLook object.
     *
     * @return the minimal object necessary to update the LookerLook, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for LookerLook are not found in the initial object
     */
    @Override
    public LookerLookBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "LookerLook", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a LookerLook.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerLook) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a LookerLook.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerLook) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a LookerLook.
     *
     * @param client connectivity to the Atlan tenant from which to remove the LookerLook's owners
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (LookerLook) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerLook, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a LookerLook.
     *
     * @param client connectivity to the Atlan tenant on which to update the LookerLook's certificate
     * @param qualifiedName of the LookerLook
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated LookerLook, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (LookerLook)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a LookerLook.
     *
     * @param client connectivity to the Atlan tenant from which to remove the LookerLook's certificate
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerLook) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a LookerLook.
     *
     * @param client connectivity to the Atlan tenant on which to update the LookerLook's announcement
     * @param qualifiedName of the LookerLook
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (LookerLook)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a LookerLook.
     *
     * @param client connectivity to the Atlan client from which to remove the LookerLook's announcement
     * @param qualifiedName of the LookerLook
     * @param name of the LookerLook
     * @return the updated LookerLook, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (LookerLook) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the LookerLook.
     *
     * @param qualifiedName for the LookerLook
     * @param name human-readable name of the LookerLook
     * @param terms the list of terms to replace on the LookerLook, or null to remove all terms from the LookerLook
     * @return the LookerLook that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerLook replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the LookerLook.
     *
     * @param client connectivity to the Atlan tenant on which to replace the LookerLook's assigned terms
     * @param qualifiedName for the LookerLook
     * @param name human-readable name of the LookerLook
     * @param terms the list of terms to replace on the LookerLook, or null to remove all terms from the LookerLook
     * @return the LookerLook that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static LookerLook replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (LookerLook) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the LookerLook, without replacing existing terms linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the LookerLook
     * @param terms the list of terms to append to the LookerLook
     * @return the LookerLook that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerLook appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the LookerLook, without replacing existing terms linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the LookerLook
     * @param qualifiedName for the LookerLook
     * @param terms the list of terms to append to the LookerLook
     * @return the LookerLook that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static LookerLook appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LookerLook) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerLook, without replacing all existing terms linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the LookerLook
     * @param terms the list of terms to remove from the LookerLook, which must be referenced by GUID
     * @return the LookerLook that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a LookerLook, without replacing all existing terms linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the LookerLook
     * @param qualifiedName for the LookerLook
     * @param terms the list of terms to remove from the LookerLook, which must be referenced by GUID
     * @return the LookerLook that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static LookerLook removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (LookerLook) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a LookerLook, without replacing existing Atlan tags linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the LookerLook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated LookerLook
     */
    public static LookerLook appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerLook, without replacing existing Atlan tags linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the LookerLook
     * @param qualifiedName of the LookerLook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated LookerLook
     */
    public static LookerLook appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (LookerLook) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerLook, without replacing existing Atlan tags linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the LookerLook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LookerLook
     */
    public static LookerLook appendAtlanTags(
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
     * Add Atlan tags to a LookerLook, without replacing existing Atlan tags linked to the LookerLook.
     * Note: this operation must make two API calls — one to retrieve the LookerLook's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the LookerLook
     * @param qualifiedName of the LookerLook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated LookerLook
     */
    public static LookerLook appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (LookerLook) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the LookerLook
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerLook.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the LookerLook
     * @param qualifiedName of the LookerLook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the LookerLook
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the LookerLook
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
     * Add Atlan tags to a LookerLook.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the LookerLook
     * @param qualifiedName of the LookerLook
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the LookerLook
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
     * Remove an Atlan tag from a LookerLook.
     *
     * @param qualifiedName of the LookerLook
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the LookerLook
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a LookerLook.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a LookerLook
     * @param qualifiedName of the LookerLook
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the LookerLook
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
