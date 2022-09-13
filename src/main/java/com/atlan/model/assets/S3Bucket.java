/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.model.relations.GuidReference;
import com.atlan.model.relations.Reference;
import java.util.List;
import java.util.Set;
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

    /** Fixed typeName for S3 buckets. */
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
    @Singular
    @Attribute
    Set<Reference> objects;

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
     * Builds the minimal object necessary to update an S3 bucket.
     *
     * @param qualifiedName of the S3 bucket
     * @param name of the S3 bucket
     * @return the minimal object necessary to update the S3 bucket, as a builder
     */
    public static S3BucketBuilder<?, ?> updater(String qualifiedName, String name) {
        return S3Bucket.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to an S3 bucket, from a potentially
     * more-complete S3 bucket object.
     *
     * @return the minimal object necessary to update the S3 bucket, as a builder
     */
    @Override
    protected S3BucketBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique S3 bucket name.
     * @param connectionQualifiedName unique name of the connection
     * @param awsArn unique ARN for the bucket
     * @return a unique name for the S3 bucket
     */
    private static String generateQualifiedName(String connectionQualifiedName, String awsArn) {
        return connectionQualifiedName + "/" + awsArn;
    }

    /**
     * Update the certificate on an S3 bucket.
     *
     * @param qualifiedName of the S3 bucket
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated S3 bucket, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static S3Bucket updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (S3Bucket) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from an S3 bucket.
     *
     * @param qualifiedName of the S3 bucket
     * @param name of the S3 bucket
     * @return the updated S3 bucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Bucket removeCertificate(String qualifiedName, String name) throws AtlanException {
        return (S3Bucket)
                Asset.removeCertificate(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Update the announcement on an S3 bucket.
     *
     * @param qualifiedName of the S3 bucket
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
     * Remove the announcement from an S3 bucket.
     *
     * @param qualifiedName of the S3 bucket
     * @param name of the S3 bucket
     * @return the updated S3 bucket, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static S3Bucket removeAnnouncement(String qualifiedName, String name) throws AtlanException {
        return (S3Bucket)
                Asset.removeAnnouncement(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Add classifications to an S3 bucket.
     *
     * @param qualifiedName of the S3 bucket
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the S3 bucket
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from an S3 bucket.
     *
     * @param qualifiedName of the S3 bucket
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the S3 bucket
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the S3 bucket.
     *
     * @param qualifiedName for the S3 bucket
     * @param name human-readable name of the S3 bucket
     * @param terms the list of terms to replace on the S3 bucket, or null to remove all terms from the S3 bucket
     * @return the S3 bucket that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static S3Bucket replaceTerms(String qualifiedName, String name, List<Reference> terms)
            throws AtlanException {
        return (S3Bucket) Asset.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the S3 bucket, without replacing existing terms linked to the S3 bucket.
     * Note: this operation must make two API calls — one to retrieve the S3 bucket's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the S3 bucket
     * @param terms the list of terms to append to the S3 bucket
     * @return the S3 bucket that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static S3Bucket appendTerms(String qualifiedName, List<Reference> terms) throws AtlanException {
        return (S3Bucket) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from an S3 bucket, without replacing all existing terms linked to the S3 bucket.
     * Note: this operation must make two API calls — one to retrieve the S3 bucket's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the S3 bucket
     * @param terms the list of terms to remove from the S3 bucket, which must be referenced by GUID
     * @return the S3 bucket that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static S3Bucket removeTerms(String qualifiedName, List<GuidReference> terms) throws AtlanException {
        return (S3Bucket) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
