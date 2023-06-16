/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.structs.GoogleLabel;
import com.atlan.model.structs.GoogleTag;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.List;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for Google assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DataStudio.class, name = DataStudio.TYPE_NAME),
    @JsonSubTypes.Type(value = GCS.class, name = GCS.TYPE_NAME),
    @JsonSubTypes.Type(value = DataStudioAsset.class, name = DataStudioAsset.TYPE_NAME),
})
@Slf4j
public abstract class Google extends Asset implements IGoogle, ICloud, IAsset, IReferenceable {

    public static final String TYPE_NAME = "Google";

    /** List of labels that have been applied to the asset in Google. */
    @Attribute
    @Singular
    List<GoogleLabel> googleLabels;

    /** TBC */
    @Attribute
    String googleLocation;

    /** TBC */
    @Attribute
    String googleLocationType;

    /** ID of the project in which the asset exists. */
    @Attribute
    String googleProjectId;

    /** Name of the project in which the asset exists. */
    @Attribute
    String googleProjectName;

    /** TBC */
    @Attribute
    Long googleProjectNumber;

    /** Service in Google in which the asset exists. */
    @Attribute
    String googleService;

    /** List of tags that have been applied to the asset in Google. */
    @Attribute
    @Singular
    List<GoogleTag> googleTags;
}
