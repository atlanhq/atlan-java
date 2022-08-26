/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.exception.AtlanException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.GuidReferenceJ;
import com.atlan.model.relations.ReferenceJ;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of an S3 object in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class S3ObjectJ extends S3J {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "S3Object";

    /** Fixed typeName for S3 objects. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Time (epoch) at which the object was last updated,
     * in milliseconds, or when it was created if it has never
     * been modified.
     */
    @Attribute
    Long s3ObjectLastModifiedTime;

    /** Name of the bucket in which the object exists. */
    @Attribute
    String s3BucketName;

    /** Qualified name of the bucket in which the object exists. */
    @Attribute
    String s3BucketQualifiedName;

    /** Object size in bytes. */
    @Attribute
    Long s3ObjectSize;

    /** Storage class used for storing the object. */
    @Attribute
    String s3ObjectStorageClass;

    /**
     * Unique identity of the object in an S3 bucket. This is usually
     * the concatenation of any prefix (folder) in the S3 bucket with
     * the name of the object (file) itself.
     */
    @Attribute
    String s3ObjectKey;

    /** Type of content in the object. */
    @Attribute
    String s3ObjectContentType;

    /** Information about how the object's content should be presented. */
    @Attribute
    String s3ObjectContentDisposition;

    /**
     * Version of the object. This is only applicable when versioning is
     * enabled on the bucket in which the object exists.
     */
    @Attribute
    String s3ObjectVersionId;

    /** S3 bucket in which the object exists. */
    @Attribute
    ReferenceJ bucket;

    /**
     * Update the certificate on an S3 object.
     * @param qualifiedName of the S3 object
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated S3 object, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static S3ObjectJ updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (S3ObjectJ) AssetJ.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Update the announcement on an S3 object.
     *
     * @param qualifiedName of the S3 object
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static S3ObjectJ updateAnnouncement(
            String qualifiedName, AtlanAnnouncementType type, String title, String message) throws AtlanException {
        return (S3ObjectJ) AssetJ.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Add classifications to an S3 object.
     *
     * @param qualifiedName of the S3 object
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the S3 object
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        AssetJ.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from an S3 object.
     *
     * @param qualifiedName of the S3 object
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the S3 object
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        AssetJ.removeClassification(TYPE_NAME, qualifiedName, classificationName);
    }

    /**
     * Replace the terms linked to the S3 object.
     *
     * @param qualifiedName for the S3 object
     * @param name human-readable name of the S3 object
     * @param terms the list of terms to replace on the S3 object, or null to remove all terms from the S3 object
     * @return the S3 object that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static S3ObjectJ replaceTerms(String qualifiedName, String name, List<ReferenceJ> terms)
            throws AtlanException {
        return (S3ObjectJ) AssetJ.replaceTerms(updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the S3 object, without replacing existing terms linked to the S3 object.
     * Note: this operation must make two API calls — one to retrieve the S3 object's existing terms,
     * and a second to append the new terms.
     *
     * @param qualifiedName for the S3 object
     * @param terms the list of terms to append to the S3 object
     * @return the S3 object that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     */
    public static S3ObjectJ appendTerms(String qualifiedName, List<ReferenceJ> terms) throws AtlanException {
        return (S3ObjectJ) AssetJ.appendTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from an S3 object, without replacing all existing terms linked to the S3 object.
     * Note: this operation must make two API calls — one to retrieve the S3 object's existing terms,
     * and a second to remove the provided terms.
     *
     * @param qualifiedName for the S3 object
     * @param terms the list of terms to remove from the S3 object, which must be referenced by GUID
     * @return the S3 object that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     */
    public static S3ObjectJ removeTerms(String qualifiedName, List<GuidReferenceJ> terms) throws AtlanException {
        return (S3ObjectJ) AssetJ.removeTerms(TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Builds the minimal object necessary to create an S3 object.
     *
     * @param name of the S3 object
     * @param connectionQualifiedName unique name of the connection through which the object is accessible
     * @param awsArn unique ARN of the object
     * @return the minimal object necessary to create the S3 object, as a builder
     */
    public static S3ObjectJBuilder<?, ?> creator(String name, String connectionQualifiedName, String awsArn) {
        return S3ObjectJ.builder()
                .qualifiedName(generateQualifiedName(connectionQualifiedName, awsArn))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorName("s3")
                .awsArn(awsArn);
    }

    /**
     * Builds the minimal object necessary to update an S3 object.
     *
     * @param qualifiedName unique name of the S3 object
     * @param name of the S3 object
     * @return the minimal object necessary to update the S3 object, as a builder
     */
    public static S3ObjectJBuilder<?, ?> updater(String qualifiedName, String name) {
        return S3ObjectJ.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to an S3 object, from a potentially
     * more-complete S3 object.
     *
     * @return the minimal object necessary to update the S3 object, as a builder
     */
    @Override
    protected S3ObjectJBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique S3 object name.
     * @param connectionQualifiedName unique name of the connection
     * @param awsArn unique ARN for the object
     * @return a unique name for the S3 object
     */
    private static String generateQualifiedName(String connectionQualifiedName, String awsArn) {
        return connectionQualifiedName + "/" + awsArn;
    }
}
