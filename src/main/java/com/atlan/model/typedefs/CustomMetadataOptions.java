/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanIcon;
import com.atlan.model.enums.AtlanTagColor;
import com.atlan.model.enums.LogoType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class CustomMetadataOptions extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * Provide custom metadata options that set the logo for the custom metadata from the image at
     * the provided URL. The custom metadata will be editable through the UI.
     *
     * @param url to an image (for example a .png)
     * @return the necessary options for setting this image as the logo for the custom metadata
     * @deprecated see {@link #withImage(String)}
     */
    @Deprecated
    public static CustomMetadataOptions withLogoFromURL(String url) {
        return withLogoFromURL(url, false);
    }

    /**
     * Provide custom metadata options that set the logo for the custom metadata to the provided
     * emoji. The custom metadata will be editable through the UI.
     *
     * @param emoji the emoji character to use for the logo
     * @return the necessary options for setting this emoji character as the logo for the custom metadata
     * @deprecated see {@link #withEmoji(String)}
     */
    @Deprecated
    public static CustomMetadataOptions withLogoAsEmoji(String emoji) {
        return withLogoAsEmoji(emoji, false);
    }

    /**
     * Provide custom metadata options that set the logo for the custom metadata from the image at
     * the provided URL.
     *
     * @param url to an image (for example a .png)
     * @param locked if true, the custom metadata cannot be modified through the Atlan UI
     * @return the necessary options for setting this image as the logo for the custom metadata
     * @deprecated see {@link #withImage(String, boolean)}
     */
    @Deprecated
    public static CustomMetadataOptions withLogoFromURL(String url, boolean locked) {
        return CustomMetadataOptions.builder()
                .logoType(LogoType.IMAGE)
                .logoUrl(url)
                .isLocked(Boolean.toString(locked))
                .build();
    }

    /**
     * Provide custom metadata options that set the logo for the custom metadata to the provided
     * emoji.
     *
     * @param emoji the emoji character to use for the logo
     * @param locked if true, the custom metadata cannot be modified through the Atlan UI
     * @return the necessary options for setting this emoji character as the logo for the custom metadata
     * @deprecated see {@link #withEmoji(String, boolean)}
     */
    @Deprecated
    public static CustomMetadataOptions withLogoAsEmoji(String emoji, boolean locked) {
        return withEmoji(emoji, locked);
    }

    /**
     * Provide custom metadata options that set the logo for the custom metadata from the image at
     * the provided URL. The custom metadata will be editable through the UI.
     *
     * @param url to an image (for example a .png)
     * @return the necessary options for setting this image as the logo for the custom metadata
     */
    public static CustomMetadataOptions withImage(String url) {
        return withImage(url, false);
    }

    /**
     * Provide custom metadata options that set the logo for the custom metadata from the image at
     * the provided URL.
     *
     * @param url to an image (for example a .png)
     * @param locked if true, the custom metadata cannot be modified through the Atlan UI
     * @return the necessary options for setting this image as the logo for the custom metadata
     */
    public static CustomMetadataOptions withImage(String url, boolean locked) {
        return CustomMetadataOptions.builder()
                .logoType(LogoType.IMAGE)
                .logoUrl(url)
                .isLocked(Boolean.toString(locked))
                .build();
    }

    /**
     * Provide custom metadata options that set the logo for the custom metadata to the provided
     * emoji. The custom metadata will be editable through the UI.
     *
     * @param emoji the emoji character to use for the logo
     * @return the necessary options for setting this emoji character as the logo for the custom metadata
     */
    public static CustomMetadataOptions withEmoji(String emoji) {
        return withEmoji(emoji, false);
    }

    /**
     * Provide custom metadata options that set the logo for the custom metadata to the provided
     * emoji.
     *
     * @param emoji the emoji character to use for the logo
     * @param locked if true, the custom metadata cannot be modified through the Atlan UI
     * @return the necessary options for setting this emoji character as the logo for the custom metadata
     */
    public static CustomMetadataOptions withEmoji(String emoji, boolean locked) {
        return CustomMetadataOptions.builder()
                .logoType(LogoType.EMOJI)
                .emoji(emoji)
                .isLocked(Boolean.toString(locked))
                .build();
    }

    /**
     * Provide custom metadata options that set the logo for the custom metadata to the provided
     * icon and color. The custom metadata will be editable through the UI.
     *
     * @param icon the icon to use for the logo
     * @param color the color to use for the icon
     * @return the necessary options for setting this icon (and color) as the logo for the custom metadata
     */
    public static CustomMetadataOptions withIcon(AtlanIcon icon, AtlanTagColor color) {
        return withIcon(icon, color, false);
    }

    /**
     * Provide custom metadata options that set the logo for the custom metadata to the provided
     * icon.
     *
     * @param icon the icon to use for the logo
     * @param color the color to use for the icon
     * @param locked if true, the custom metadata cannot be modified through the Atlan UI
     * @return the necessary options for setting this icon (and color) as the logo for the custom metadata
     */
    public static CustomMetadataOptions withIcon(AtlanIcon icon, AtlanTagColor color, boolean locked) {
        return CustomMetadataOptions.builder()
                .logoType(LogoType.ICON)
                .iconColor(color)
                .iconName(icon)
                .isLocked(Boolean.toString(locked))
                .build();
    }

    /** Type of logo used for the custom metadata. */
    LogoType logoType;

    /** Color to use for the icon. */
    AtlanTagColor iconColor;

    /** Icon to use to represent the custom metadata. */
    AtlanIcon iconName;

    /** If the {@code logoType} is emoji, this should hold the emoji character. */
    String emoji;

    /** If the {@code logoType} is image, this should hold a URL to the image. */
    String logoUrl;

    /** Indicates whether the custom metadata can be managed in the UI (false) or not (true). */
    String isLocked;

    /** Unique identifier (GUID) of the image used for the logo, for logos that are uploaded directly to Atlan. */
    String imageId;
}
