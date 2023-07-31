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
import com.atlan.model.structs.GoogleLabel;
import com.atlan.model.structs.GoogleTag;
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
 * Instance of a Google Cloud Storage bucket in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class GCSBucket extends Asset
        implements IGCSBucket, IGCS, IGoogle, IObjectStore, ICloud, IAsset, IReferenceable, ICatalog {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "GCSBucket";

    /** Fixed typeName for GCSBuckets. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String gcsAccessControl;

    /** TBC */
    @Attribute
    String gcsBucketLifecycleRules;

    /** TBC */
    @Attribute
    Long gcsBucketRetentionEffectiveTime;

    /** TBC */
    @Attribute
    Boolean gcsBucketRetentionLocked;

    /** TBC */
    @Attribute
    Long gcsBucketRetentionPeriod;

    /** TBC */
    @Attribute
    String gcsBucketRetentionPolicy;

    /** TBC */
    @Attribute
    Boolean gcsBucketVersioningEnabled;

    /** TBC */
    @Attribute
    String gcsETag;

    /** TBC */
    @Attribute
    String gcsEncryptionType;

    /** TBC */
    @Attribute
    Long gcsMetaGenerationId;

    /** TBC */
    @Attribute
    Long gcsObjectCount;

    /** GCS objects within this bucket. */
    @Attribute
    @Singular
    SortedSet<IGCSObject> gcsObjects;

    /** TBC */
    @Attribute
    Boolean gcsRequesterPays;

    /** TBC */
    @Attribute
    String gcsStorageClass;

    /** TBC */
    @Attribute
    @Singular
    List<GoogleLabel> googleLabels;

    /** TBC */
    @Attribute
    String googleLocation;

    /** TBC */
    @Attribute
    String googleLocationType;

    /** TBC */
    @Attribute
    String googleProjectId;

    /** TBC */
    @Attribute
    String googleProjectName;

    /** TBC */
    @Attribute
    Long googleProjectNumber;

    /** TBC */
    @Attribute
    String googleService;

    /** TBC */
    @Attribute
    @Singular
    List<GoogleTag> googleTags;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /**
     * Start an asset filter that will return all GCSBucket assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) GCSBucket assets will be included.
     *
     * @return an asset filter that includes all GCSBucket assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all GCSBucket assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) GCSBucket assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all GCSBucket assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all GCSBucket assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) GCSBuckets will be included
     * @return an asset filter that includes all GCSBucket assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all GCSBucket assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) GCSBuckets will be included
     * @return an asset filter that includes all GCSBucket assets
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
     * Reference to a GCSBucket by GUID.
     *
     * @param guid the GUID of the GCSBucket to reference
     * @return reference to a GCSBucket that can be used for defining a relationship to a GCSBucket
     */
    public static GCSBucket refByGuid(String guid) {
        return GCSBucket._internal().guid(guid).build();
    }

    /**
     * Reference to a GCSBucket by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the GCSBucket to reference
     * @return reference to a GCSBucket that can be used for defining a relationship to a GCSBucket
     */
    public static GCSBucket refByQualifiedName(String qualifiedName) {
        return GCSBucket._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a GCSBucket by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the GCSBucket to retrieve, either its GUID or its full qualifiedName
     * @return the requested full GCSBucket, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSBucket does not exist or the provided GUID is not a GCSBucket
     */
    @JsonIgnore
    public static GCSBucket get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a GCSBucket by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the GCSBucket to retrieve, either its GUID or its full qualifiedName
     * @return the requested full GCSBucket, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSBucket does not exist or the provided GUID is not a GCSBucket
     */
    @JsonIgnore
    public static GCSBucket get(AtlanClient client, String id) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.retrieveFull(client, id);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof GCSBucket) {
                return (GCSBucket) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, "GCSBucket");
            }
        } else {
            Asset asset = Asset.retrieveFull(client, TYPE_NAME, id);
            if (asset instanceof GCSBucket) {
                return (GCSBucket) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, "GCSBucket");
            }
        }
    }

    /**
     * Retrieves a GCSBucket by its GUID, complete with all of its relationships.
     *
     * @param guid of the GCSBucket to retrieve
     * @return the requested full GCSBucket, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSBucket does not exist or the provided GUID is not a GCSBucket
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static GCSBucket retrieveByGuid(String guid) throws AtlanException {
        return retrieveByGuid(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a GCSBucket by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the GCSBucket to retrieve
     * @return the requested full GCSBucket, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSBucket does not exist or the provided GUID is not a GCSBucket
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static GCSBucket retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof GCSBucket) {
            return (GCSBucket) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "GCSBucket");
        }
    }

    /**
     * Retrieves a GCSBucket by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the GCSBucket to retrieve
     * @return the requested full GCSBucket, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSBucket does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static GCSBucket retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return retrieveByQualifiedName(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a GCSBucket by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the GCSBucket to retrieve
     * @return the requested full GCSBucket, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the GCSBucket does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static GCSBucket retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(client, TYPE_NAME, qualifiedName);
        if (asset instanceof GCSBucket) {
            return (GCSBucket) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "GCSBucket");
        }
    }

    /**
     * Restore the archived (soft-deleted) GCSBucket to active.
     *
     * @param qualifiedName for the GCSBucket
     * @return true if the GCSBucket is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) GCSBucket to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the GCSBucket
     * @return true if the GCSBucket is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a GCSBucket.
     *
     * @param name of the GCSBucket
     * @param connectionQualifiedName unique name of the connection through which the GCSBucket is accessible
     * @return the minimal object necessary to create the GCSBucket, as a builder
     */
    public static GCSBucketBuilder<?, ?> creator(String name, String connectionQualifiedName) {
        return GCSBucket._internal()
                .qualifiedName(generateQualifiedName(name, connectionQualifiedName))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.GCS);
    }

    /**
     * Generate a unique GCSBucket name.
     *
     * @param name of the GCSBucket
     * @param connectionQualifiedName unique name of the connection through which the GCSBucket is accessible
     * @return a unique name for the GCSBucket
     */
    public static String generateQualifiedName(String name, String connectionQualifiedName) {
        return connectionQualifiedName + "/" + name;
    }

    /**
     * Builds the minimal object necessary to update a GCSBucket.
     *
     * @param qualifiedName of the GCSBucket
     * @param name of the GCSBucket
     * @return the minimal request necessary to update the GCSBucket, as a builder
     */
    public static GCSBucketBuilder<?, ?> updater(String qualifiedName, String name) {
        return GCSBucket._internal().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a GCSBucket, from a potentially
     * more-complete GCSBucket object.
     *
     * @return the minimal object necessary to update the GCSBucket, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for GCSBucket are not found in the initial object
     */
    @Override
    public GCSBucketBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "GCSBucket", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a GCSBucket.
     *
     * @param qualifiedName of the GCSBucket
     * @param name of the GCSBucket
     * @return the updated GCSBucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a GCSBucket.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the GCSBucket
     * @param name of the GCSBucket
     * @return the updated GCSBucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GCSBucket) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a GCSBucket.
     *
     * @param qualifiedName of the GCSBucket
     * @param name of the GCSBucket
     * @return the updated GCSBucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a GCSBucket.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the GCSBucket
     * @param name of the GCSBucket
     * @return the updated GCSBucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GCSBucket) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a GCSBucket.
     *
     * @param qualifiedName of the GCSBucket
     * @param name of the GCSBucket
     * @return the updated GCSBucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a GCSBucket.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GCSBucket's owners
     * @param qualifiedName of the GCSBucket
     * @param name of the GCSBucket
     * @return the updated GCSBucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (GCSBucket) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a GCSBucket.
     *
     * @param qualifiedName of the GCSBucket
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GCSBucket, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a GCSBucket.
     *
     * @param client connectivity to the Atlan tenant on which to update the GCSBucket's certificate
     * @param qualifiedName of the GCSBucket
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated GCSBucket, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (GCSBucket) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a GCSBucket.
     *
     * @param qualifiedName of the GCSBucket
     * @param name of the GCSBucket
     * @return the updated GCSBucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a GCSBucket.
     *
     * @param client connectivity to the Atlan tenant from which to remove the GCSBucket's certificate
     * @param qualifiedName of the GCSBucket
     * @param name of the GCSBucket
     * @return the updated GCSBucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GCSBucket) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a GCSBucket.
     *
     * @param qualifiedName of the GCSBucket
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a GCSBucket.
     *
     * @param client connectivity to the Atlan tenant on which to update the GCSBucket's announcement
     * @param qualifiedName of the GCSBucket
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (GCSBucket)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a GCSBucket.
     *
     * @param qualifiedName of the GCSBucket
     * @param name of the GCSBucket
     * @return the updated GCSBucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a GCSBucket.
     *
     * @param client connectivity to the Atlan client from which to remove the GCSBucket's announcement
     * @param qualifiedName of the GCSBucket
     * @param name of the GCSBucket
     * @return the updated GCSBucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static GCSBucket removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (GCSBucket) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the GCSBucket.
     *
     * @param qualifiedName for the GCSBucket
     * @param name human-readable name of the GCSBucket
     * @param terms the list of terms to replace on the GCSBucket, or null to remove all terms from the GCSBucket
     * @return the GCSBucket that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static GCSBucket replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the GCSBucket.
     *
     * @param client connectivity to the Atlan tenant on which to replace the GCSBucket's assigned terms
     * @param qualifiedName for the GCSBucket
     * @param name human-readable name of the GCSBucket
     * @param terms the list of terms to replace on the GCSBucket, or null to remove all terms from the GCSBucket
     * @return the GCSBucket that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static GCSBucket replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (GCSBucket) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the GCSBucket, without replacing existing terms linked to the GCSBucket.
     * Note: this operation must make two API calls — one to retrieve the GCSBucket's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the GCSBucket
     * @param terms the list of terms to append to the GCSBucket
     * @return the GCSBucket that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static GCSBucket appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the GCSBucket, without replacing existing terms linked to the GCSBucket.
     * Note: this operation must make two API calls — one to retrieve the GCSBucket's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the GCSBucket
     * @param qualifiedName for the GCSBucket
     * @param terms the list of terms to append to the GCSBucket
     * @return the GCSBucket that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static GCSBucket appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (GCSBucket) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a GCSBucket, without replacing all existing terms linked to the GCSBucket.
     * Note: this operation must make two API calls — one to retrieve the GCSBucket's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the GCSBucket
     * @param terms the list of terms to remove from the GCSBucket, which must be referenced by GUID
     * @return the GCSBucket that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static GCSBucket removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a GCSBucket, without replacing all existing terms linked to the GCSBucket.
     * Note: this operation must make two API calls — one to retrieve the GCSBucket's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the GCSBucket
     * @param qualifiedName for the GCSBucket
     * @param terms the list of terms to remove from the GCSBucket, which must be referenced by GUID
     * @return the GCSBucket that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static GCSBucket removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (GCSBucket) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a GCSBucket, without replacing existing Atlan tags linked to the GCSBucket.
     * Note: this operation must make two API calls — one to retrieve the GCSBucket's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the GCSBucket
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated GCSBucket
     */
    public static GCSBucket appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a GCSBucket, without replacing existing Atlan tags linked to the GCSBucket.
     * Note: this operation must make two API calls — one to retrieve the GCSBucket's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the GCSBucket
     * @param qualifiedName of the GCSBucket
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated GCSBucket
     */
    public static GCSBucket appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (GCSBucket) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a GCSBucket, without replacing existing Atlan tags linked to the GCSBucket.
     * Note: this operation must make two API calls — one to retrieve the GCSBucket's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the GCSBucket
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated GCSBucket
     */
    public static GCSBucket appendAtlanTags(
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
     * Add Atlan tags to a GCSBucket, without replacing existing Atlan tags linked to the GCSBucket.
     * Note: this operation must make two API calls — one to retrieve the GCSBucket's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the GCSBucket
     * @param qualifiedName of the GCSBucket
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated GCSBucket
     */
    public static GCSBucket appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (GCSBucket) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a GCSBucket.
     *
     * @param qualifiedName of the GCSBucket
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the GCSBucket
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a GCSBucket.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the GCSBucket
     * @param qualifiedName of the GCSBucket
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the GCSBucket
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a GCSBucket.
     *
     * @param qualifiedName of the GCSBucket
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the GCSBucket
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
     * Add Atlan tags to a GCSBucket.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the GCSBucket
     * @param qualifiedName of the GCSBucket
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the GCSBucket
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
     * Remove an Atlan tag from a GCSBucket.
     *
     * @param qualifiedName of the GCSBucket
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the GCSBucket
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a GCSBucket.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a GCSBucket
     * @param qualifiedName of the GCSBucket
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the GCSBucket
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
