/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Detailed information about S3-related assets in Atlan.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class S3J extends AWSJ {
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
}
