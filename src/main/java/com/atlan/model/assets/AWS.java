/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = S3.class, name = S3.TYPE_NAME),
})
public abstract class AWS extends Catalog {

    public static final String TYPE_NAME = "AWS";

    /**
     * Amazon Resource Name (ARN) for this asset. This uniquely identifies
     * the asset in AWS, and thus must be unique across all AWS asset instances.
     */
    @Attribute
    String awsArn;

    /** Group of AWS region and service objects. */
    @Attribute
    String awsPartition;

    /** Type of service in which the asset exists. */
    @Attribute
    String awsService;

    /** Physical region where the data center in which the asset exists is clustered. */
    @Attribute
    String awsRegion;

    /** 12-digit number that uniquely identifies an AWS account. */
    @Attribute
    String awsAccountId;

    /** Unique resource ID assigned when a new resource is created. */
    @Attribute
    String awsResourceId;

    /** Root user's name. */
    @Attribute
    String awsOwnerName;

    /** Root user's ID. */
    @Attribute
    String awsOwnerId;

    /** List of tags that have been applied to the asset in AWS. */
    @Singular
    @Attribute
    List<AWSTag> awsTags;
}
