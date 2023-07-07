/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.ApiException;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.admin.AtlanImage;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanTagColor;
import com.atlan.model.enums.TagIconType;
import java.io.IOException;
import java.net.MalformedURLException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Options that can be defined for an Atlan tag.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class AtlanTagOptions extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * Provide Atlan tag options that set the color for the tag, using the default tag icon.
     *
     * @param color to use to represent the Atlan tag
     * @return the necessary options for setting this color for the Atlan tag
     */
    public static AtlanTagOptions of(AtlanTagColor color) {
        return withIcon(AtlanIcon.ATLAN_TAG, color);
    }

    /**
     * Provide Atlan tag options that set the icon and color for the tag, using a built-in icon.
     *
     * @param icon to use to represent the Atlan tag
     * @param color to use to represent the Atlan tag
     * @return the necessary options for setting this icon and color for the Atlan tag
     */
    public static AtlanTagOptions withIcon(AtlanIcon icon, AtlanTagColor color) {
        return AtlanTagOptions.builder()
                .color(color)
                .iconName(icon)
                .iconType(TagIconType.ICON)
                .imageID("")
                .build();
    }

    /**
     * Provide Atlan tag options that set the image and color for the tag, using an uploaded image.
     *
     * @param url URL to the image to use for the Atlan tag
     * @param color to use to represent the Atlan tag
     * @return the necessary options for setting this image and color for the Atlan tag
     * @throws AtlanException on any API communication issues trying to upload the image
     */
    public static AtlanTagOptions withImage(String url, AtlanTagColor color) throws AtlanException {
        return withImage(Atlan.getDefaultClient(), url, color);
    }

    /**
     * Provide Atlan tag options that set the image and color for the tag, using an uploaded image.
     *
     * @param client connectivity to the Atlan tenant in which the tag is intended to be created
     * @param url URL to the image to use for the Atlan tag
     * @param color to use to represent the Atlan tag
     * @return the necessary options for setting this image and color for the Atlan tag
     * @throws AtlanException on any API communication issues trying to upload the image
     */
    public static AtlanTagOptions withImage(AtlanClient client, String url, AtlanTagColor color) throws AtlanException {
        try {
            AtlanImage result = client.images().upload(url);
            return AtlanTagOptions.builder()
                    .color(color)
                    .iconType(TagIconType.IMAGE)
                    .imageID(result.getId())
                    .build();
        } catch (MalformedURLException e) {
            throw new InvalidRequestException(ErrorCode.INVALID_URL, e);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.INACCESSIBLE_URL, e);
        }
    }

    /** Color to use for the Atlan tag. */
    AtlanTagColor color;

    /** Name of icon to use to represent the tag visually. */
    AtlanIcon iconName;

    /** Type of icon to use for representing the tag visually. */
    TagIconType iconType;

    /** Unique identifier (GUID) of an image to use for the tag. */
    String imageID;
}
