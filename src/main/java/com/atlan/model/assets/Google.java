/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.structs.GoogleLabel;
import com.atlan.model.structs.GoogleTag;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for Google assets.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({})
@Slf4j
public abstract class Google extends Catalog {

    public static final String TYPE_NAME = "Google";

    /** Service in Google in which the asset exists. */
    @Attribute
    String googleService;

    /** Name of the project in which the asset exists. */
    @Attribute
    String googleProjectName;

    /** ID of the project in which the asset exists. */
    @Attribute
    String googleProjectId;

    /** TBC */
    @Attribute
    Long googleProjectNumber;

    /** TBC */
    @Attribute
    String googleLocation;

    /** TBC */
    @Attribute
    String googleLocationType;

    /** List of labels that have been applied to the asset in Google. */
    @Attribute
    @Singular
    List<GoogleLabel> googleLabels;

    /** List of tags that have been applied to the asset in Google. */
    @Attribute
    @Singular
    List<GoogleTag> googleTags;
}
