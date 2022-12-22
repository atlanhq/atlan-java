/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of an S3 bucket in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class S3Bucket extends S3 {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "S3Bucket";

    /** Fixed typeName for S3Buckets. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Number of objects within the bucket. */
    @Attribute
    Long s3ObjectCount;

    /** Whether versioning is enabled for the bucket. */
    @Attribute
    Boolean s3BucketVersioningEnabled;

    /** S3 objects within this bucket. */
    @Attribute
    @Singular
    SortedSet<S3Object> objects;

    /**
     * Reference to a S3Bucket by GUID.
     *
     * @param guid the GUID of the S3Bucket to reference
     * @return reference to a S3Bucket that can be used for defining a relationship to a S3Bucket
     */
    public static S3Bucket refByGuid(String guid) {
        return S3Bucket.builder().guid(guid).build();
    }

    /**
     * Reference to a S3Bucket by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the S3Bucket to reference
     * @return reference to a S3Bucket that can be used for defining a relationship to a S3Bucket
     */
    public static S3Bucket refByQualifiedName(String qualifiedName) {
        return S3Bucket.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create an S3 bucket.
     *
     * @param name of the S3 bucket
     * @param connectionQualifiedName unique name of the connection through which the bucket is accessible
     * @param awsArn unique ARN of the bucket
     * @return the minimal object necessary to create the S3 bucket, as a builder
     */
    public static S3BucketBuilder<?, ?> creator(String name, String connectionQualifiedName, String awsArn) {
        return S3Bucket.builder()
                .qualifiedName(generateQualifiedName(connectionQualifiedName, awsArn))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorType(AtlanConnectorType.S3)
                .awsArn(awsArn);
    }

    /**
     * Builds the minimal object necessary to update a S3Bucket.
     *
     * @param qualifiedName of the S3Bucket
     * @param name of the S3Bucket
     * @return the minimal request necessary to update the S3Bucket, as a builder
     */
    public static S3BucketBuilder<?, ?> updater(String qualifiedName, String name) {
        return S3Bucket.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a S3Bucket, from a potentially
     * more-complete S3Bucket object.
     *
     * @return the minimal object necessary to update the S3Bucket, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for S3Bucket are not found in the initial object
     */
    @Override
    public S3BucketBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "S3Bucket", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Retrieves a S3Bucket by its GUID, complete with all of its relationships.
     *
     * @param guid of the S3Bucket to retrieve
     * @return the requested full S3Bucket, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the S3Bucket does not exist or the provided GUID is not a S3Bucket
     */
    public static S3Bucket retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof S3Bucket) {
            return (S3Bucket) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "S3Bucket");
        }
    }

    /**
     * Retrieves a S3Bucket by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the S3Bucket to retrieve
     * @return the requested full S3Bucket, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the S3Bucket does not exist
     */
    public static S3Bucket retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof S3Bucket) {
            return (S3Bucket) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "S3Bucket");
        }
    }

    /**
     * Restore the archived (soft-deleted) S3Bucket to active.
     *
     * @param qualifiedName for the S3Bucket
     * @return true if the S3Bucket is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a S3Bucket.
     *
     * @param qualifiedName of the S3Bucket
     * @param name of the S3Bucket
     * @return the updated S3Bucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Bucket removeDescription(String qualifiedName, String name) throws AtlanException {
        return (S3Bucket)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a S3Bucket.
     *
     * @param qualifiedName of the S3Bucket
     * @param name of the S3Bucket
     * @return the updated S3Bucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Bucket removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (S3Bucket) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the owners from a S3Bucket.
     *
     * @param qualifiedName of the S3Bucket
     * @param name of the S3Bucket
     * @return the updated S3Bucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Bucket removeOwners(String qualifiedName, String name) throws AtlanException {
        return (S3Bucket)
                Asset.removeOwners(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the certificate on a S3Bucket.
     *
     * @param qualifiedName of the S3Bucket
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated S3Bucket, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static S3Bucket updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (S3Bucket) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a S3Bucket.
     *
     * @param qualifiedName of the S3Bucket
     * @param name of the S3Bucket
     * @return the updated S3Bucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Bucket removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (S3Bucket)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on a S3Bucket.
     *
     * @param qualifiedName of the S3Bucket
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static S3Bucket updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (S3Bucket) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a S3Bucket.
     *
     * @param qualifiedName of the S3Bucket
     * @param name of the S3Bucket
     * @return the updated S3Bucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Bucket removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (S3Bucket)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to a S3Bucket.
     *
     * @param qualifiedName of the S3Bucket
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the S3Bucket
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a S3Bucket.
     *
     * @param qualifiedName of the S3Bucket
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the S3Bucket
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the S3Bucket.
     *
     * @param qualifiedName for the S3Bucket
     * @param name human-readable name of the S3Bucket
     * @param terms the list of terms to replace on the S3Bucket, or null to remove all terms from the S3Bucket
     * @return the S3Bucket that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static S3Bucket replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (S3Bucket) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the S3Bucket, without replacing existing terms linked to the S3Bucket.
     * Note: this operation must make two API calls — one to retrieve the S3Bucket's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the S3Bucket
     * @param terms the list of terms to append to the S3Bucket
     * @return the S3Bucket that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static S3Bucket appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (S3Bucket) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a S3Bucket, without replacing all existing terms linked to the S3Bucket.
     * Note: this operation must make two API calls — one to retrieve the S3Bucket's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the S3Bucket
     * @param terms the list of terms to remove from the S3Bucket, which must be referenced by GUID
     * @return the S3Bucket that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static S3Bucket removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (S3Bucket) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
