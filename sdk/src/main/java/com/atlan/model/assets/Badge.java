/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.BadgeCondition;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a badge in Atlan. Badges visually highlight key information about an asset, surfaced from custom metadata.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class Badge extends Asset implements IBadge, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Badge";

    /** Fixed typeName for Badges. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** List of conditions that determine the colors to diplay for various values. */
    @Attribute
    @Singular
    List<BadgeCondition> badgeConditions;

    /** Custom metadata attribute for which to show the badge. */
    @Attribute
    String badgeMetadataAttribute;

    /**
     * Builds the minimal object necessary to create a relationship to a Badge, from a potentially
     * more-complete Badge object.
     *
     * @return the minimal object necessary to relate to the Badge
     * @throws InvalidRequestException if any of the minimal set of required properties for a Badge relationship are not found in the initial object
     */
    @Override
    public Badge trimToReference() throws InvalidRequestException {
        if (this.getGuid() != null && !this.getGuid().isEmpty()) {
            return refByGuid(this.getGuid());
        }
        if (this.getQualifiedName() != null && !this.getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getQualifiedName());
        }
        if (this.getUniqueAttributes() != null
                && this.getUniqueAttributes().getQualifiedName() != null
                && !this.getUniqueAttributes().getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getUniqueAttributes().getQualifiedName());
        }
        throw new InvalidRequestException(
                ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, TYPE_NAME, "guid, qualifiedName");
    }

    /**
     * Start a fluent search that will return all Badge assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Badge assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all Badge assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all Badge assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Badges will be included
     * @return a fluent search that includes all Badge assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client, boolean includeArchived) {
        FluentSearch.FluentSearchBuilder<?, ?> builder =
                FluentSearch.builder(client).where(Asset.TYPE_NAME.eq(TYPE_NAME));
        if (!includeArchived) {
            builder.active();
        }
        return builder;
    }

    /**
     * Reference to a Badge by GUID. Use this to create a relationship to this Badge,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the Badge to reference
     * @return reference to a Badge that can be used for defining a relationship to a Badge
     */
    public static Badge refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Badge by GUID. Use this to create a relationship to this Badge,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the Badge to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Badge that can be used for defining a relationship to a Badge
     */
    public static Badge refByGuid(String guid, Reference.SaveSemantic semantic) {
        return Badge._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a Badge by qualifiedName. Use this to create a relationship to this Badge,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the Badge to reference
     * @return reference to a Badge that can be used for defining a relationship to a Badge
     */
    public static Badge refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Badge by qualifiedName. Use this to create a relationship to this Badge,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the Badge to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Badge that can be used for defining a relationship to a Badge
     */
    public static Badge refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return Badge._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a Badge by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Badge to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Badge, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Badge does not exist or the provided GUID is not a Badge
     */
    @JsonIgnore
    public static Badge get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a Badge by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Badge to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Badge, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Badge does not exist or the provided GUID is not a Badge
     */
    @JsonIgnore
    public static Badge get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Badge) {
                return (Badge) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof Badge) {
                return (Badge) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a Badge by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Badge to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Badge, including any relationships
     * @return the requested Badge, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Badge does not exist or the provided GUID is not a Badge
     */
    @JsonIgnore
    public static Badge get(AtlanClient client, String id, Collection<AtlanField> attributes) throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a Badge by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Badge to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Badge, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the Badge
     * @return the requested Badge, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Badge does not exist or the provided GUID is not a Badge
     */
    @JsonIgnore
    public static Badge get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = Badge.select(client)
                    .where(Badge.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof Badge) {
                return (Badge) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = Badge.select(client)
                    .where(Badge.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof Badge) {
                return (Badge) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) Badge to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the Badge
     * @return true if the Badge is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Badge.
     *
     * @param client connectivity to the Atlan tenant on which the Badge is intended to be created
     * @param name of the Badge
     * @param cmName human-readable name of the custom metadata for which to create the badge
     * @param cmAttribute human-readable name of the custom metadata attribute for which to create the badge
     * @return the minimal request necessary to create the Badge, as a builder
     * @throws AtlanException if the specified custom metadata for the badge cannot be found
     */
    public static BadgeBuilder<?, ?> creator(AtlanClient client, String name, String cmName, String cmAttribute)
            throws AtlanException {
        String cmId = client.getCustomMetadataCache().getIdForName(cmName);
        String cmAttrId = client.getCustomMetadataCache().getAttrIdForName(cmName, cmAttribute);
        return Badge._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(generateQualifiedName(client, cmName, cmAttribute))
                .name(name)
                .badgeMetadataAttribute(cmId + "." + cmAttrId);
    }

    /**
     * Generate a unique name for this badge.
     *
     * @param client connectivity to the Atlan tenant through which to generate the unique name of the badge
     * @param cmName human-readable name of the custom metadata for which to create the badge
     * @param cmAttribute human-readable name of the custom metadata attribute for which to create the badge
     * @return the unique qualifiedName of the badge
     * @throws AtlanException if the specified custom metadata cannot be found
     */
    public static String generateQualifiedName(AtlanClient client, String cmName, String cmAttribute)
            throws AtlanException {
        String cmId = client.getCustomMetadataCache().getIdForName(cmName);
        String cmAttrId = client.getCustomMetadataCache().getAttrIdForName(cmName, cmAttribute);
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
        return Badge._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
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
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class BadgeBuilder<C extends Badge, B extends BadgeBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a Badge.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Badge
     * @param name of the Badge
     * @return the updated Badge, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Badge removeDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Badge) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Badge.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Badge
     * @param name of the Badge
     * @return the updated Badge, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Badge removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Badge) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }
}
