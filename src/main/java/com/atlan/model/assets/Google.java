/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Detailed information about Google-related assets.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class Google extends Asset {

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
}
