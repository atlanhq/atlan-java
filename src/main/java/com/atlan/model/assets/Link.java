/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.LinkIconType;
import com.atlan.model.relations.UniqueAttributes;
import java.util.UUID;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Instance of a Link in Atlan, with its detailed information.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class Link extends Resource {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Link";

    /** Fixed typeName for links. */
    @Getter(onMethod_ = {@Override})
    @Setter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Icon for the link. */
    @Attribute
    String icon;

    /** Type of icon for the link. */
    @Attribute
    LinkIconType iconType;

    /** Asset to which the link is linked. */
    @Attribute
    Asset asset;

    /**
     * Reference to a link by GUID.
     *
     * @param guid the GUID of the link to reference
     * @return reference to a link that can be used for defining a relationship to a link
     */
    public static Link refByGuid(String guid) {
        return Link.builder().guid(guid).build();
    }

    /**
     * Reference to a link by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the link to reference
     * @return reference to a link that can be used for defining a relationship to a link
     */
    public static Link refByQualifiedName(String qualifiedName) {
        return Link.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to create a link.
     *
     * @param reference a reference to the asset to which the link should be attached
     * @param title for the link
     * @param url of the link
     * @return the minimal object necessary to create the link and attach it to the asset, as a builder
     */
    public static LinkBuilder<?, ?> creator(Asset reference, String title, String url) {
        return Link.builder()
                .qualifiedName(generateQualifiedName())
                .name(title)
                .link(url)
                .asset(reference);
    }

    /**
     * Builds the minimal object necessary to update a link.
     *
     * @param guid the GUID of the link to update
     * @param qualifiedName of the link to update
     * @return the minimal object necessary to update the link, as a builder
     */
    public static LinkBuilder<?, ?> updater(String guid, String qualifiedName) {
        return Link.builder().guid(guid).qualifiedName(qualifiedName);
    }

    /**
     * Builds the minimal object necessary to apply an update to a link, from a potentially
     * more-complete link object.
     *
     * @return the minimal object necessary to update the link, as a builder
     */
    @Override
    protected LinkBuilder<?, ?> trimToRequired() {
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique link name.
     * @return a unique name for the link
     */
    private static String generateQualifiedName() {
        return UUID.randomUUID().toString();
    }
}
