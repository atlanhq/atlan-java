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
import com.atlan.model.enums.*;
import java.io.IOException;
import java.net.MalformedURLException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Options that can be defined for an Atlan tag.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
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
     * @deprecated see {@link #withImage(String)}
     */
    @Deprecated
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
     * @deprecated see {@link #withImage(AtlanClient, String)}
     */
    @Deprecated
    public static AtlanTagOptions withImage(AtlanClient client, String url, AtlanTagColor color) throws AtlanException {
        return withImage(client, url);
    }

    /**
     * Provide Atlan tag options that set the image for the tag, using an uploaded image.
     *
     * @param url URL to the image to use for the Atlan tag
     * @return the necessary options for setting this image for the Atlan tag
     * @throws AtlanException on any API communication issues trying to upload the image
     */
    public static AtlanTagOptions withImage(String url) throws AtlanException {
        return withImage(Atlan.getDefaultClient(), url);
    }

    /**
     * Provide Atlan tag options that set the image for the tag, using an uploaded image.
     *
     * @param client connectivity to the Atlan tenant in which the tag is intended to be created
     * @param url URL to the image to use for the Atlan tag
     * @return the necessary options for setting this image for the Atlan tag
     * @throws AtlanException on any API communication issues trying to upload the image
     */
    public static AtlanTagOptions withImage(AtlanClient client, String url) throws AtlanException {
        try {
            AtlanImage result = client.images.upload(url);
            return AtlanTagOptions.builder()
                    .iconType(TagIconType.IMAGE)
                    .imageID(result.getId())
                    .color(AtlanTagColor.GRAY)
                    .iconName(AtlanIcon.ATLAN_TAG)
                    .build();
        } catch (MalformedURLException e) {
            throw new InvalidRequestException(ErrorCode.INVALID_URL, e);
        } catch (IOException e) {
            throw new ApiException(ErrorCode.INACCESSIBLE_URL, e);
        }
    }

    /**
     * Provide Atlan tag options that set the logo for the tag to the provided emoji.
     *
     * @param emoji the emoji character to use for the logo
     * @return the necessary options for setting this emoji character as the logo for the Atlan tag
     */
    public static AtlanTagOptions withEmoji(String emoji) {
        return AtlanTagOptions.builder()
                .iconType(TagIconType.EMOJI)
                .emoji(emoji)
                .color(AtlanTagColor.GRAY)
                .iconName(AtlanIcon.ATLAN_TAG)
                .build();
    }

    /** Color to use for the Atlan tag. */
    AtlanTagColor color;

    /** Name of icon to use to represent the tag visually. */
    AtlanIcon iconName;

    /** Type of icon to use for representing the tag visually. */
    TagIconType iconType;

    /** If the {@code iconType} is emoji, this should hold the emoji character. */
    String emoji;

    /** Unique identifier (GUID) of an image to use for the tag. */
    String imageID;
}
