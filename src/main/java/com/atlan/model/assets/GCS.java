/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.structs.GoogleLabel;
import com.atlan.model.structs.GoogleTag;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for Google Cloud Storage assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = GCSObject.class, name = GCSObject.TYPE_NAME),
    @JsonSubTypes.Type(value = GCSBucket.class, name = GCSBucket.TYPE_NAME),
})
@Slf4j
public abstract class GCS extends Asset
        implements IGCS, IGoogle, IObjectStore, ICloud, IAsset, IReferenceable, ICatalog {

    public static final String TYPE_NAME = "GCS";

    /** TBC */
    @Attribute
    String gcsAccessControl;

    /** Entity tag for the asset. An entity tag is a hash of the object and represents changes to the contents of an object only, not its metadata. */
    @Attribute
    String gcsETag;

    /** TBC */
    @Attribute
    String gcsEncryptionType;

    /** TBC */
    @Attribute
    Long gcsMetaGenerationId;

    /** TBC */
    @Attribute
    Boolean gcsRequesterPays;

    /** TBC */
    @Attribute
    String gcsStorageClass;

    /** TBC */
    @Attribute
    @Singular
    List<GoogleLabel> googleLabels;

    /** TBC */
    @Attribute
    String googleLocation;

    /** TBC */
    @Attribute
    String googleLocationType;

    /** TBC */
    @Attribute
    String googleProjectId;

    /** TBC */
    @Attribute
    String googleProjectName;

    /** TBC */
    @Attribute
    Long googleProjectNumber;

    /** TBC */
    @Attribute
    String googleService;

    /** TBC */
    @Attribute
    @Singular
    List<GoogleTag> googleTags;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
