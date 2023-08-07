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
import com.atlan.model.structs.AwsTag;
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
 * Instance of an S3 object in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class S3Object extends Asset
        implements IS3Object, IS3, IObjectStore, IAWS, ICatalog, IAsset, IReferenceable, ICloud {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "S3Object";

    /** Fixed typeName for S3Objects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String awsAccountId;

    /** TBC */
    @Attribute
    String awsArn;

    /** TBC */
    @Attribute
    String awsOwnerId;

    /** TBC */
    @Attribute
    String awsOwnerName;

    /** TBC */
    @Attribute
    String awsPartition;

    /** TBC */
    @Attribute
    String awsRegion;

    /** TBC */
    @Attribute
    String awsResourceId;

    /** TBC */
    @Attribute
    String awsService;

    /** TBC */
    @Attribute
    @Singular
    List<AwsTag> awsTags;

    /** S3 bucket in which the object exists. */
    @Attribute
    IS3Bucket bucket;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** Name of the bucket in which the object exists. */
    @Attribute
    String s3BucketName;

    /** qualifiedName of the bucket in which the object exists. */
    @Attribute
    String s3BucketQualifiedName;

    /** TBC */
    @Attribute
    String s3ETag;

    /** TBC */
    @Attribute
    String s3Encryption;

    /** Information about how the object's content should be presented. */
    @Attribute
    String s3ObjectContentDisposition;

    /** Type of content in the object. */
    @Attribute
    String s3ObjectContentType;

    /** Unique identity of the object in an S3 bucket. This is usually the concatenation of any prefix (folder) in the S3 bucket with the name of the object (file) itself. */
    @Attribute
    String s3ObjectKey;

    /** Time (epoch) at which the object was last updated, in milliseconds, or when it was created if it has never been modified. */
    @Attribute
    Long s3ObjectLastModifiedTime;

    /** Object size in bytes. */
    @Attribute
    Long s3ObjectSize;

    /** Storage class used for storing the object. */
    @Attribute
    String s3ObjectStorageClass;

    /** Version of the object. This is only applicable when versioning is enabled on the bucket in which the object exists. */
    @Attribute
    String s3ObjectVersionId;

    /**
     * Start an asset filter that will return all S3Object assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) S3Object assets will be included.
     *
     * @return an asset filter that includes all S3Object assets
     */
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all S3Object assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) S3Object assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all S3Object assets
     */
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all S3Object assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) S3Objects will be included
     * @return an asset filter that includes all S3Object assets
     */
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all S3Object assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) S3Objects will be included
     * @return an asset filter that includes all S3Object assets
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
     * Reference to a S3Object by GUID.
     *
     * @param guid the GUID of the S3Object to reference
     * @return reference to a S3Object that can be used for defining a relationship to a S3Object
     */
    public static S3Object refByGuid(String guid) {
        return S3Object._internal().guid(guid).build();
    }

    /**
     * Reference to a S3Object by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the S3Object to reference
     * @return reference to a S3Object that can be used for defining a relationship to a S3Object
     */
    public static S3Object refByQualifiedName(String qualifiedName) {
        return S3Object._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a S3Object by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the S3Object to retrieve, either its GUID or its full qualifiedName
     * @return the requested full S3Object, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the S3Object does not exist or the provided GUID is not a S3Object
     */
    @JsonIgnore
    public static S3Object get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
    }

    /**
     * Retrieves a S3Object by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the S3Object to retrieve, either its GUID or its full qualifiedName
     * @return the requested full S3Object, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the S3Object does not exist or the provided GUID is not a S3Object
     */
    @JsonIgnore
    public static S3Object get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, true);
    }

    /**
     * Retrieves a S3Object by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the S3Object to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full S3Object, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the S3Object does not exist or the provided GUID is not a S3Object
     */
    @JsonIgnore
    public static S3Object get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof S3Object) {
                return (S3Object) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof S3Object) {
                return (S3Object) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a S3Object by its GUID, complete with all of its relationships.
     *
     * @param guid of the S3Object to retrieve
     * @return the requested full S3Object, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the S3Object does not exist or the provided GUID is not a S3Object
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static S3Object retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a S3Object by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the S3Object to retrieve
     * @return the requested full S3Object, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the S3Object does not exist or the provided GUID is not a S3Object
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static S3Object retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a S3Object by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the S3Object to retrieve
     * @return the requested full S3Object, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the S3Object does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static S3Object retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a S3Object by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the S3Object to retrieve
     * @return the requested full S3Object, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the S3Object does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static S3Object retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) S3Object to active.
     *
     * @param qualifiedName for the S3Object
     * @return true if the S3Object is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) S3Object to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the S3Object
     * @return true if the S3Object is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create an S3 object.
     *
     * @param name of the S3 object
     * @param bucketQualifiedName unique name of the S3 bucket in which the object exists
     * @param bucketName simple human-readable name of the S3 bucket in which the object exists
     * @param awsArn unique ARN of the object
     * @return the minimal object necessary to create the S3 object, as a builder
     */
    public static S3ObjectBuilder<?, ?> creator(
            String name, String bucketQualifiedName, String bucketName, String awsArn) {
        String connectionQualifiedName = StringUtils.getConnectionQualifiedName(bucketQualifiedName);
        return S3Object._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(IS3.generateQualifiedName(connectionQualifiedName, awsArn))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.S3)
                .awsArn(awsArn)
                .s3BucketQualifiedName(bucketQualifiedName)
                .s3BucketName(bucketName)
                .bucket(S3Bucket.refByQualifiedName(bucketQualifiedName));
    }

    /**
     * Builds the minimal object necessary to update a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param name of the S3Object
     * @return the minimal request necessary to update the S3Object, as a builder
     */
    public static S3ObjectBuilder<?, ?> updater(String qualifiedName, String name) {
        return S3Object._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a S3Object, from a potentially
     * more-complete S3Object object.
     *
     * @return the minimal object necessary to update the S3Object, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for S3Object are not found in the initial object
     */
    @Override
    public S3ObjectBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "S3Object", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param name of the S3Object
     * @return the updated S3Object, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Object removeDescription(String qualifiedName, String name) throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the system description from a S3Object.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the S3Object
     * @param name of the S3Object
     * @return the updated S3Object, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Object removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (S3Object) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param name of the S3Object
     * @return the updated S3Object, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Object removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the user's description from a S3Object.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the S3Object
     * @param name of the S3Object
     * @return the updated S3Object, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Object removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (S3Object) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param name of the S3Object
     * @return the updated S3Object, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Object removeOwners(String qualifiedName, String name) throws AtlanException {
        return removeOwners(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the owners from a S3Object.
     *
     * @param client connectivity to the Atlan tenant from which to remove the S3Object's owners
     * @param qualifiedName of the S3Object
     * @param name of the S3Object
     * @return the updated S3Object, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Object removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (S3Object) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated S3Object, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static S3Object updateCertificate(String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return updateCertificate(Atlan.getDefaultClient(), qualifiedName, certificate, message);
    }

    /**
     * Update the certificate on a S3Object.
     *
     * @param client connectivity to the Atlan tenant on which to update the S3Object's certificate
     * @param qualifiedName of the S3Object
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated S3Object, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static S3Object updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (S3Object) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param name of the S3Object
     * @return the updated S3Object, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Object removeCertificate(String qualifiedName, String name) throws AtlanException {
        return removeCertificate(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the certificate from a S3Object.
     *
     * @param client connectivity to the Atlan tenant from which to remove the S3Object's certificate
     * @param qualifiedName of the S3Object
     * @param name of the S3Object
     * @return the updated S3Object, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Object removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (S3Object) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static S3Object updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return updateAnnouncement(Atlan.getDefaultClient(), qualifiedName, type, title, message);
    }

    /**
     * Update the announcement on a S3Object.
     *
     * @param client connectivity to the Atlan tenant on which to update the S3Object's announcement
     * @param qualifiedName of the S3Object
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static S3Object updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (S3Object) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param name of the S3Object
     * @return the updated S3Object, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Object removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return removeAnnouncement(Atlan.getDefaultClient(), qualifiedName, name);
    }

    /**
     * Remove the announcement from a S3Object.
     *
     * @param client connectivity to the Atlan client from which to remove the S3Object's announcement
     * @param qualifiedName of the S3Object
     * @param name of the S3Object
     * @return the updated S3Object, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Object removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (S3Object) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the S3Object.
     *
     * @param qualifiedName for the S3Object
     * @param name human-readable name of the S3Object
     * @param terms the list of terms to replace on the S3Object, or null to remove all terms from the S3Object
     * @return the S3Object that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static S3Object replaceTerms(String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return replaceTerms(Atlan.getDefaultClient(), qualifiedName, name, terms);
    }

    /**
     * Replace the terms linked to the S3Object.
     *
     * @param client connectivity to the Atlan tenant on which to replace the S3Object's assigned terms
     * @param qualifiedName for the S3Object
     * @param name human-readable name of the S3Object
     * @param terms the list of terms to replace on the S3Object, or null to remove all terms from the S3Object
     * @return the S3Object that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static S3Object replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (S3Object) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the S3Object, without replacing existing terms linked to the S3Object.
     * Note: this operation must make two API calls — one to retrieve the S3Object's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the S3Object
     * @param terms the list of terms to append to the S3Object
     * @return the S3Object that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static S3Object appendTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return appendTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Link additional terms to the S3Object, without replacing existing terms linked to the S3Object.
     * Note: this operation must make two API calls — one to retrieve the S3Object's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the S3Object
     * @param qualifiedName for the S3Object
     * @param terms the list of terms to append to the S3Object
     * @return the S3Object that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static S3Object appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (S3Object) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a S3Object, without replacing all existing terms linked to the S3Object.
     * Note: this operation must make two API calls — one to retrieve the S3Object's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the S3Object
     * @param terms the list of terms to remove from the S3Object, which must be referenced by GUID
     * @return the S3Object that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static S3Object removeTerms(String qualifiedName, List<IGlossaryTerm> terms) throws AtlanException {
        return removeTerms(Atlan.getDefaultClient(), qualifiedName, terms);
    }

    /**
     * Remove terms from a S3Object, without replacing all existing terms linked to the S3Object.
     * Note: this operation must make two API calls — one to retrieve the S3Object's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the S3Object
     * @param qualifiedName for the S3Object
     * @param terms the list of terms to remove from the S3Object, which must be referenced by GUID
     * @return the S3Object that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static S3Object removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (S3Object) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a S3Object, without replacing existing Atlan tags linked to the S3Object.
     * Note: this operation must make two API calls — one to retrieve the S3Object's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the S3Object
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated S3Object
     */
    public static S3Object appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a S3Object, without replacing existing Atlan tags linked to the S3Object.
     * Note: this operation must make two API calls — one to retrieve the S3Object's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the S3Object
     * @param qualifiedName of the S3Object
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated S3Object
     */
    public static S3Object appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (S3Object) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a S3Object, without replacing existing Atlan tags linked to the S3Object.
     * Note: this operation must make two API calls — one to retrieve the S3Object's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the S3Object
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated S3Object
     */
    public static S3Object appendAtlanTags(
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
     * Add Atlan tags to a S3Object, without replacing existing Atlan tags linked to the S3Object.
     * Note: this operation must make two API calls — one to retrieve the S3Object's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the S3Object
     * @param qualifiedName of the S3Object
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated S3Object
     */
    public static S3Object appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (S3Object) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the S3Object
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a S3Object.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the S3Object
     * @param qualifiedName of the S3Object
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the S3Object
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the S3Object
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
     * Add Atlan tags to a S3Object.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the S3Object
     * @param qualifiedName of the S3Object
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the S3Object
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
     * Remove an Atlan tag from a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the S3Object
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a S3Object.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a S3Object
     * @param qualifiedName of the S3Object
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the S3Object
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
