/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.*;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of an S3 object in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class S3Object extends S3 {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "S3Object";

    /** Fixed typeName for S3Objects. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Time (epoch) at which the object was last updated, in milliseconds, or when it was created if it has never been modified. */
    @Attribute
    Long s3ObjectLastModifiedTime;

    /** Name of the bucket in which the object exists. */
    @Attribute
    String s3BucketName;

    /** qualifiedName of the bucket in which the object exists. */
    @Attribute
    String s3BucketQualifiedName;

    /** Object size in bytes. */
    @Attribute
    Long s3ObjectSize;

    /** Storage class used for storing the object. */
    @Attribute
    String s3ObjectStorageClass;

    /** Unique identity of the object in an S3 bucket. This is usually the concatenation of any prefix (folder) in the S3 bucket with the name of the object (file) itself. */
    @Attribute
    String s3ObjectKey;

    /** Type of content in the object. */
    @Attribute
    String s3ObjectContentType;

    /** Information about how the object's content should be presented. */
    @Attribute
    String s3ObjectContentDisposition;

    /** Version of the object. This is only applicable when versioning is enabled on the bucket in which the object exists. */
    @Attribute
    String s3ObjectVersionId;

    /** S3 bucket in which the object exists. */
    @Attribute
    S3Bucket bucket;

    /**
     * Reference to a S3Object by GUID.
     *
     * @param guid the GUID of the S3Object to reference
     * @return reference to a S3Object that can be used for defining a relationship to a S3Object
     */
    public static S3Object refByGuid(String guid) {
        return S3Object.builder().guid(guid).build();
    }

    /**
     * Reference to a S3Object by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the S3Object to reference
     * @return reference to a S3Object that can be used for defining a relationship to a S3Object
     */
    public static S3Object refByQualifiedName(String qualifiedName) {
        return S3Object.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
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
        return S3Object.builder()
                .qualifiedName(generateQualifiedName(connectionQualifiedName, awsArn))
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
        return S3Object.builder().qualifiedName(qualifiedName).name(name);
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
     * Retrieves a S3Object by its GUID, complete with all of its relationships.
     *
     * @param guid of the S3Object to retrieve
     * @return the requested full S3Object, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the S3Object does not exist or the provided GUID is not a S3Object
     */
    public static S3Object retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof S3Object) {
            return (S3Object) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "S3Object");
        }
    }

    /**
     * Retrieves a S3Object by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the S3Object to retrieve
     * @return the requested full S3Object, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the S3Object does not exist
     */
    public static S3Object retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof S3Object) {
            return (S3Object) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "S3Object");
        }
    }

    /**
     * Restore the archived (soft-deleted) S3Object to active.
     *
     * @param qualifiedName for the S3Object
     * @return true if the S3Object is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
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
        return (S3Object) Asset.removeDescription(updater(qualifiedName, name));
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
        return (S3Object) Asset.removeUserDescription(updater(qualifiedName, name));
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
        return (S3Object) Asset.removeOwners(updater(qualifiedName, name));
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
    public static S3Object updateCertificate(String qualifiedName, AtlanCertificateStatus certificate, String message)
            throws AtlanException {
        return (S3Object) Asset.updateCertificate(builder(), TYPE_NAME, qualifiedName, certificate, message);
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
        return (S3Object) Asset.removeCertificate(updater(qualifiedName, name));
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
        return (S3Object) Asset.updateAnnouncement(builder(), TYPE_NAME, qualifiedName, type, title, message);
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
        return (S3Object) Asset.removeAnnouncement(updater(qualifiedName, name));
    }

    /**
     * Add classifications to a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param classificationNames human-readable names of the classifications to add
     * @throws AtlanException on any API problems, or if any of the classifications already exist on the S3Object
     */
    public static void addClassifications(String qualifiedName, List<String> classificationNames)
            throws AtlanException {
        Asset.addClassifications(TYPE_NAME, qualifiedName, classificationNames);
    }

    /**
     * Remove a classification from a S3Object.
     *
     * @param qualifiedName of the S3Object
     * @param classificationName human-readable name of the classification to remove
     * @throws AtlanException on any API problems, or if the classification does not exist on the S3Object
     */
    public static void removeClassification(String qualifiedName, String classificationName) throws AtlanException {
        Asset.removeClassification(TYPE_NAME, qualifiedName, classificationName);
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
    public static S3Object replaceTerms(String qualifiedName, String name, List<GlossaryTerm> terms)
            throws AtlanException {
        return (S3Object) Asset.replaceTerms(updater(qualifiedName, name), terms);
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
    public static S3Object appendTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (S3Object) Asset.appendTerms(TYPE_NAME, qualifiedName, terms);
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
    public static S3Object removeTerms(String qualifiedName, List<GlossaryTerm> terms) throws AtlanException {
        return (S3Object) Asset.removeTerms(TYPE_NAME, qualifiedName, terms);
    }
}
