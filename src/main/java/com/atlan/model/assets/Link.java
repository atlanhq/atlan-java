/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.Entity;
import com.atlan.model.enums.LinkIconType;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
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

    /** Fixed typeName for Links. */
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

    /** Asset to which the link is attached. */
    @Attribute
    Asset asset;

    /**
     * Reference to a Link by GUID.
     *
     * @param guid the GUID of the Link to reference
     * @return reference to a Link that can be used for defining a relationship to a Link
     */
    public static Link refByGuid(String guid) {
        return Link.builder().guid(guid).build();
    }

    /**
     * Reference to a Link by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Link to reference
     * @return reference to a Link that can be used for defining a relationship to a Link
     */
    public static Link refByQualifiedName(String qualifiedName) {
        return Link.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update a Link.
     *
     * @param reference a reference to the asset to which the Link should be attached
     * @param title for the Link
     * @param url of the Link
     * @return the minimal object necessary to create the Link and attach it to the asset, as a builder
     */
    public static LinkBuilder<?, ?> creator(Asset reference, String title, String url) {
        return Link.builder()
                .qualifiedName(generateQualifiedName())
                .name(title)
                .link(url)
                .asset(reference);
    }

    /**
     * Builds the minimal object necessary to update a Link.
     *
     * @param qualifiedName of the Link
     * @param name of the Link
     * @return the minimal request necessary to update the Link, as a builder
     */
    public static LinkBuilder<?, ?> updater(String qualifiedName, String name) {
        return Link.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Link, from a potentially
     * more-complete Link object.
     *
     * @return the minimal object necessary to update the Link, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Link are not found in the initial object
     */
    @Override
    public LinkBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    "Required field for updating Link is missing.",
                    String.join(",", missing),
                    "ATLAN-JAVA-CLIENT-400-404",
                    400,
                    null);
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Generate a unique link name.
     *
     * @return a unique name for the link
     */
    private static String generateQualifiedName() {
        return UUID.randomUUID().toString();
    }

    /**
     * Retrieves a Link by its GUID, complete with all of its relationships.
     *
     * @param guid of the Link to retrieve
     * @return the requested full Link, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Link does not exist or the provided GUID is not a Link
     */
    public static Link retrieveByGuid(String guid) throws AtlanException {
        Entity entity = Entity.retrieveFull(guid);
        if (entity == null) {
            throw new NotFoundException("No entity found with GUID: " + guid, "ATLAN_JAVA_CLIENT-404-001", 404, null);
        } else if (entity instanceof Link) {
            return (Link) entity;
        } else {
            throw new NotFoundException(
                    "Entity with GUID " + guid + " is not a Link.", "ATLAN_JAVA_CLIENT-404-002", 404, null);
        }
    }

    /**
     * Retrieves a Link by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Link to retrieve
     * @return the requested full Link, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Link does not exist
     */
    public static Link retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Entity entity = Entity.retrieveFull(TYPE_NAME, qualifiedName);
        if (entity instanceof Link) {
            return (Link) entity;
        } else {
            throw new NotFoundException(
                    "No Link found with qualifiedName: " + qualifiedName, "ATLAN_JAVA_CLIENT-404-003", 404, null);
        }
    }

    /**
     * Restore the archived (soft-deleted) Link to active.
     *
     * @param qualifiedName for the Link
     * @return true if the Link is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Remove the system description from a Link.
     *
     * @param qualifiedName of the Link
     * @param name of the Link
     * @return the updated Link, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Link removeDescription(String qualifiedName, String name) throws AtlanException {
        return (Link)
                Asset.removeDescription(builder().qualifiedName(qualifiedName).name(name));
    }

    /**
     * Remove the user's description from a Link.
     *
     * @param qualifiedName of the Link
     * @param name of the Link
     * @return the updated Link, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Link removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (Link) Asset.removeUserDescription(
                builder().qualifiedName(qualifiedName).name(name));
    }
}
