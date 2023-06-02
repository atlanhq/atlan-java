/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.structs.BadgeCondition;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a badge in Atlan.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Badge extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Badge";

    /** Fixed typeName for Badges. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    @Singular
    List<BadgeCondition> badgeConditions;

    /** TBC */
    @Attribute
    String badgeMetadataAttribute;

    /**
     * Reference to a Badge by GUID.
     *
     * @param guid the GUID of the Badge to reference
     * @return reference to a Badge that can be used for defining a relationship to a Badge
     */
    public static Badge refByGuid(String guid) {
        return Badge.builder().guid(guid).build();
    }

    /**
     * Reference to a Badge by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Badge to reference
     * @return reference to a Badge that can be used for defining a relationship to a Badge
     */
    public static Badge refByQualifiedName(String qualifiedName) {
        return Badge.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Badge by its GUID, complete with all of its relationships.
     *
     * @param guid of the Badge to retrieve
     * @return the requested full Badge, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Badge does not exist or the provided GUID is not a Badge
     */
    public static Badge retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof Badge) {
            return (Badge) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "Badge");
        }
    }

    /**
     * Retrieves a Badge by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Badge to retrieve
     * @return the requested full Badge, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Badge does not exist
     */
    public static Badge retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof Badge) {
            return (Badge) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "Badge");
        }
    }

    /**
     * Restore the archived (soft-deleted) Badge to active.
     *
     * @param qualifiedName for the Badge
     * @return true if the Badge is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Badge.
     *
     * @param name of the Badge
     * @param cmName human-readable name of the custom metadata for which to create the badge
     * @param cmAttribute human-readable name of the custom metadata attribute for which to create the badge
     * @return the minimal request necessary to create the Badge, as a builder
     * @throws AtlanException if the specified custom metadata for the badge cannot be found
     */
    public static BadgeBuilder<?, ?> creator(String name, String cmName, String cmAttribute) throws AtlanException {
        String cmId = CustomMetadataCache.getIdForName(cmName);
        String cmAttrId = CustomMetadataCache.getAttrIdForName(cmName, cmAttribute);
        return Badge.builder()
                .qualifiedName(generateQualifiedName(cmName, cmAttribute))
                .name(name)
                .badgeMetadataAttribute(cmId + "." + cmAttrId);
    }

    /**
     * Generate a unique name for this badge.
     *
     * @param cmName human-readable name of the custom metadata for which to create the badge
     * @param cmAttribute human-readable name of the custom metadata attribute for which to create the badge
     * @return the unique qualifiedName of the badge
     * @throws AtlanException if the specified custom metadata cannot be found
     */
    public static String generateQualifiedName(String cmName, String cmAttribute) throws AtlanException {
        String cmId = CustomMetadataCache.getIdForName(cmName);
        String cmAttrId = CustomMetadataCache.getAttrIdForName(cmName, cmAttribute);
        return "badges/global/" + cmId + "." + cmAttrId;
    }

    /**
     * Builds the minimal object necessary to update a Badge.
     *
     * @param qualifiedName of the Badge
     * @param name of the Badge
     * @return the minimal request necessary to update the Badge, as a builder
     */
    public static BadgeBuilder<?, ?> updater(String qualifiedName, String name) {
        return Badge.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Badge, from a potentially
     * more-complete Badge object.
     *
     * @return the minimal object necessary to update the Badge, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Badge are not found in the initial object
     */
    @Override
    public BadgeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Badge", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a Badge.
     *
     * @param qualifiedName of the Badge
     * @param name of the Badge
     * @return the updated Badge, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Badge removeDescription(String qualifiedName, String name) throws AtlanException {
        return (Badge) Asset.removeDescription(updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Badge.
     *
     * @param qualifiedName of the Badge
     * @param name of the Badge
     * @return the updated Badge, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Badge removeUserDescription(String qualifiedName, String name) throws AtlanException {
        return (Badge) Asset.removeUserDescription(updater(qualifiedName, name));
    }
}
