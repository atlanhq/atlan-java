/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Detailed information about S3-related assets in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = S3Bucket.class, name = S3Bucket.TYPE_NAME),
    @JsonSubTypes.Type(value = S3Object.class, name = S3Object.TYPE_NAME),
})
public abstract class S3 extends AWS {

    public static final String TYPE_NAME = "S3";

    /**
     * Entity tag for the asset. An entity tag is a hash of the object and represents
     * changes to the contents of an object only, not its metadata.
     */
    @Attribute
    String s3ETag;

    /** TBC */
    @Attribute
    String s3Encryption;

    /**
     * Generate a unique S3 name.
     *
     * @param connectionQualifiedName unique name of the connection
     * @param awsArn unique ARN for the S3 artifact
     * @return a unique name for the S3 artifact
     */
    protected static String generateQualifiedName(String connectionQualifiedName, String awsArn) {
        return connectionQualifiedName + "/" + awsArn;
    }
}
