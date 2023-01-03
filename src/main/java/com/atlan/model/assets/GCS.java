/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Base class for Google Cloud Storage assets.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = GCSObject.class, name = GCSObject.TYPE_NAME),
    @JsonSubTypes.Type(value = GCSBucket.class, name = GCSBucket.TYPE_NAME),
})
public abstract class GCS extends Google {

    public static final String TYPE_NAME = "GCS";

    /** TBC */
    @Attribute
    String gcsStorageClass;

    /** TBC */
    @Attribute
    String gcsEncryptionType;

    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    @Attribute
    String gcsETag;

    /** TBC */
    @Attribute
    Boolean gcsRequesterPays;

    /** TBC */
    @Attribute
    String gcsAccessControl;

    /** TBC */
    @Attribute
    Long gcsMetaGenerationId;
}
