/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of an S3 object in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class S3Object extends S3 {
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
    Reference bucket;

    /**
     * Builds the minimal object necessary to create an S3 object.
     * To continue adding to the object, call {@link #toBuilder()} on the result and continue calling
     * additional methods to add metadata followed by {@link S3Object.S3ObjectBuilder#build()}.
     *
     * @param name of the S3 object
     * @param connectionQualifiedName unique name of the connection through which the object is accessible
     * @param awsArn unique ARN of the object
     * @return the minimal object necessary to create the S3 object
     */
    public static S3Object toCreate(String name, String connectionQualifiedName, String awsArn) {
        return S3Object.builder()
                .qualifiedName(generateQualifiedName(connectionQualifiedName, awsArn))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorName("s3")
                .awsArn(awsArn)
                .build();
    }

    /**
     * Builds the minimal object necessary to update an S3 object.
     * To continue adding to the object, call {@link #toBuilder()} on the result and continue calling
     * additional methods to add metadata followed by {@link S3Object.S3ObjectBuilder#build()}.
     *
     * @param qualifiedName unique name of the S3 object
     * @param name of the S3 object
     * @return the minimal object necessary to update the S3 object
     */
    public static S3Object toUpdate(String qualifiedName, String name) {
        return S3Object.builder().qualifiedName(qualifiedName).name(name).build();
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
}
