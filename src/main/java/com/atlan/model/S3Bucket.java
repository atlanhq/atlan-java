/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.relations.Reference;
import java.util.List;
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
    List<Reference> objects;

    /**
     * Builds the minimal object necessary to create an S3 bucket.
     * To continue adding to the object, call {@link #toBuilder()} on the result and continue calling
     * additional methods to add metadata followed by {@link S3Bucket.S3BucketBuilder#build()}.
     *
     * @param name of the S3 bucket
     * @param connectionQualifiedName unique name of the connection through which the bucket is accessible
     * @param awsArn unique ARN of the bucket
     * @return the minimal object necessary to create the S3 bucket
     */
    public static S3Bucket toCreate(String name, String connectionQualifiedName, String awsArn) {
        return S3Bucket.builder()
                .qualifiedName(generateQualifiedName(connectionQualifiedName, awsArn))
                .name(name)
                .connectionQualifiedName(connectionQualifiedName)
                .connectorName("s3")
                .awsArn(awsArn)
                .build();
    }

    /**
     * Builds the minimal object necessary to update an S3 bucket.
     * To continue adding to the object, call {@link #toBuilder()} on the result and continue calling
     * additional methods to add metadata followed by {@link S3Bucket.S3BucketBuilder#build()}.
     *
     * @param qualifiedName of the S3 bucket
     * @param name of the S3 bucket
     * @return the minimal object necessary to update the S3 bucket
     */
    public static S3Bucket toUpdate(String qualifiedName, String name) {
        return S3Bucket.builder().qualifiedName(qualifiedName).name(name).build();
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
