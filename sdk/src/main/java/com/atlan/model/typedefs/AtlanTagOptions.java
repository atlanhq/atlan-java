/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

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
import lombok.Builder;
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
        return of(color, false);
    }

    /**
     * Provide Atlan tag options that set the color for the tag, using the default tag icon.
     *
     * @param color to use to represent the Atlan tag
     * @param sourceSynced if true, configure this tag as a source-synced tag
     * @return the necessary options for setting this color for the Atlan tag
     */
    public static AtlanTagOptions of(AtlanTagColor color, boolean sourceSynced) {
        return withIcon(AtlanIcon.ATLAN_TAG, color, sourceSynced);
    }

    /**
     * Provide Atlan tag options that set the icon and color for the tag, using a built-in icon.
     *
     * @param icon to use to represent the Atlan tag
     * @param color to use to represent the Atlan tag
     * @return the necessary options for setting this icon and color for the Atlan tag
     */
    public static AtlanTagOptions withIcon(AtlanIcon icon, AtlanTagColor color) {
        return withIcon(icon, color, false);
    }

    /**
     * Provide Atlan tag options that set the icon and color for the tag, using a built-in icon.
     *
     * @param icon to use to represent the Atlan tag
     * @param color to use to represent the Atlan tag
     * @param sourceSynced if true, configure this tag as a source-synced tag
     * @return the necessary options for setting this icon and color for the Atlan tag
     */
    public static AtlanTagOptions withIcon(AtlanIcon icon, AtlanTagColor color, boolean sourceSynced) {
        return AtlanTagOptions.builder()
                .color(color.getValue())
                .iconName(icon)
                .iconType(TagIconType.ICON)
                .imageID("")
                .hasTag(sourceSynced)
                .build();
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
        return withImage(client, url, false);
    }

    /**
     * Provide Atlan tag options that set the image for the tag, using an uploaded image.
     *
     * @param client connectivity to the Atlan tenant in which the tag is intended to be created
     * @param url URL to the image to use for the Atlan tag
     * @param sourceSynced if true, configure this tag as a source-synced tag
     * @return the necessary options for setting this image for the Atlan tag
     * @throws AtlanException on any API communication issues trying to upload the image
     */
    public static AtlanTagOptions withImage(AtlanClient client, String url, boolean sourceSynced)
            throws AtlanException {
        try {
            AtlanImage result = client.images.upload(url);
            return AtlanTagOptions.builder()
                    .iconType(TagIconType.IMAGE)
                    .imageID(result.getId())
                    .color(AtlanTagColor.GRAY.getValue())
                    .iconName(AtlanIcon.ATLAN_TAG)
                    .hasTag(sourceSynced)
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
        return withEmoji(emoji, false);
    }

    /**
     * Provide Atlan tag options that set the logo for the tag to the provided emoji.
     *
     * @param emoji the emoji character to use for the logo
     * @param sourceSynced if true, configure this tag as a source-synced tag
     * @return the necessary options for setting this emoji character as the logo for the Atlan tag
     */
    public static AtlanTagOptions withEmoji(String emoji, boolean sourceSynced) {
        return AtlanTagOptions.builder()
                .iconType(TagIconType.EMOJI)
                .emoji(emoji)
                .color(AtlanTagColor.GRAY.getValue())
                .iconName(AtlanIcon.ATLAN_TAG)
                .hasTag(sourceSynced)
                .build();
    }

    /** Color to use for the Atlan tag. */
    String color;

    /** Name of icon to use to represent the tag visually. */
    AtlanIcon iconName;

    /** Type of icon to use for representing the tag visually. */
    TagIconType iconType;

    /** If the {@code iconType} is emoji, this should hold the emoji character. */
    String emoji;

    /** Unique identifier (GUID) of an image to use for the tag. */
    String imageID;

    /** Whether this tag was created within the UI (true) or not (false). */
    @Builder.Default
    Boolean createdFromAtlan = false;

    /** Whether this tag has an associated source-synced tag (true) or not (false). */
    Boolean hasTag;
}
