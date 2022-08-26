/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.model.core.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class BusinessMetadataOptions extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Type of logo used for the custom metadata. */
    String logoType;

    /** If the {@code logoType} is emoji, this should hold the emoji character. */
    String emoji;

    /** If the {@code logoType} is image, this should hold a URL to the image. */
    String logoUrl;

    /** Indicates whether the custom metadata can be managed in the UI (false) or not (true). */
    String isLocked;
}
