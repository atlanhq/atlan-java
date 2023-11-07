/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.net.ApiResource;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@Builder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AtlanImage extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Unique identifier (GUID) of the image. */
    String id;

    /** TBC */
    String version;

    /** Time at which the image was uploaded (epoch), in milliseconds. */
    Long createdAt;

    /** Time at which the image was last modified (epoch), in milliseconds. */
    Long updatedAt;

    /** Generated name of the image that was uploaded. */
    String fileName;

    /** Original name of the image that was uploaded. */
    String rawName;

    /** Generated name of the image that was uploaded. */
    String key;

    /** Filename extension for the image that was uploaded. */
    String extension;

    /** MIME type for the image that was uploaded. */
    String contentType;

    /** Size of the image that was uploaded, in bytes. */
    Long fileSize;

    /** Whether the image is encrypted (true) or not (false). */
    Boolean isEncrypted;

    /** TBC */
    String redirectUrl;

    /** TBC */
    Boolean isUploaded;

    /** TBC */
    String uploadedAt;

    /** Whether the image has been archived (true) or is still actively available (false) */
    Boolean isArchived;
}
