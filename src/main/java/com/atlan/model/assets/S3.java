/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.structs.AwsTag;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for S3 assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = S3Bucket.class, name = S3Bucket.TYPE_NAME),
    @JsonSubTypes.Type(value = S3Object.class, name = S3Object.TYPE_NAME),
})
@Slf4j
public abstract class S3 extends Asset implements IS3, IObjectStore, IAWS, ICatalog, IAsset, IReferenceable, ICloud {

    public static final String TYPE_NAME = "S3";

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

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
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
    public static String generateQualifiedName(String connectionQualifiedName, String awsArn) {
        return connectionQualifiedName + "/" + awsArn;
    }
}
