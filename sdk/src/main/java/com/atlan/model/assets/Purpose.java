/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AssetFilterGroup;
import com.atlan.model.enums.AssetSidebarTab;
import com.atlan.model.enums.AuthPolicyCategory;
import com.atlan.model.enums.AuthPolicyResourceCategory;
import com.atlan.model.enums.AuthPolicyType;
import com.atlan.model.enums.DataAction;
import com.atlan.model.enums.PurposeMetadataAction;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Atlan Type representing a Purpose model
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class Purpose extends Asset implements IPurpose, IAccessControl, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Purpose";

    /** Fixed typeName for Purposes. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    String channelLink;

    /** TBC */
    @Attribute
    String defaultNavigation;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<AssetFilterGroup> denyAssetFilters;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<AssetSidebarTab> denyAssetTabs;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> denyAssetTypes;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> denyCustomMetadataGuids;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> denyNavigationPages;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> displayPreferences;

    /** TBC */
    @Attribute
    Boolean isAccessControlEnabled;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAuthPolicy> policies;

    /** TBC */
    @Attribute
    @Singular
    @JsonProperty("purposeClassifications")
    SortedSet<String> purposeAtlanTags;

    /**
     * Builds the minimal object necessary to create a relationship to a Purpose, from a potentially
     * more-complete Purpose object.
     *
     * @return the minimal object necessary to relate to the Purpose
     * @throws InvalidRequestException if any of the minimal set of required properties for a Purpose relationship are not found in the initial object
     */
    @Override
    public Purpose trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all Purpose assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Purpose assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all Purpose assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all Purpose assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Purposes will be included
     * @return a fluent search that includes all Purpose assets
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
     * Reference to a Purpose by GUID. Use this to create a relationship to this Purpose,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the Purpose to reference
     * @return reference to a Purpose that can be used for defining a relationship to a Purpose
     */
    public static Purpose refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Purpose by GUID. Use this to create a relationship to this Purpose,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the Purpose to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Purpose that can be used for defining a relationship to a Purpose
     */
    public static Purpose refByGuid(String guid, Reference.SaveSemantic semantic) {
        return Purpose._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a Purpose by qualifiedName. Use this to create a relationship to this Purpose,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the Purpose to reference
     * @return reference to a Purpose that can be used for defining a relationship to a Purpose
     */
    public static Purpose refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Purpose by qualifiedName. Use this to create a relationship to this Purpose,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the Purpose to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Purpose that can be used for defining a relationship to a Purpose
     */
    public static Purpose refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return Purpose._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a Purpose by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Purpose to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Purpose, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist or the provided GUID is not a Purpose
     */
    @JsonIgnore
    public static Purpose get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a Purpose by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Purpose to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Purpose, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist or the provided GUID is not a Purpose
     */
    @JsonIgnore
    public static Purpose get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Purpose) {
                return (Purpose) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof Purpose) {
                return (Purpose) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a Purpose by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Purpose to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Purpose, including any relationships
     * @return the requested Purpose, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist or the provided GUID is not a Purpose
     */
    @JsonIgnore
    public static Purpose get(AtlanClient client, String id, Collection<AtlanField> attributes) throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a Purpose by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Purpose to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Purpose, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the Purpose
     * @return the requested Purpose, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist or the provided GUID is not a Purpose
     */
    @JsonIgnore
    public static Purpose get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = Purpose.select(client)
                    .where(Purpose.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof Purpose) {
                return (Purpose) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = Purpose.select(client)
                    .where(Purpose.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof Purpose) {
                return (Purpose) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) Purpose to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the Purpose
     * @return true if the Purpose is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create a Purpose.
     *
     * @param name of the Purpose
     * @param atlanTags Atlan tags on which this purpose should be applied
     * @return the minimal request necessary to create the Purpose, as a builder
     * @throws InvalidRequestException if at least one Atlan tag is not specified
     */
    public static PurposeBuilder<?, ?> creator(String name, Collection<String> atlanTags)
            throws InvalidRequestException {
        if (atlanTags == null || atlanTags.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.NO_ATLAN_TAG_FOR_PURPOSE);
        }
        return Purpose._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(name)
                .name(name)
                .displayName(name)
                .isAccessControlEnabled(true)
                .description("")
                .purposeAtlanTags(atlanTags);
    }

    /**
     * Builds the minimal object necessary to update a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param name of the Purpose
     * @param isEnabled whether the Purpose should be activated (true) or deactivated (false)
     * @return the minimal request necessary to update the Purpose, as a builder
     */
    public static PurposeBuilder<?, ?> updater(String qualifiedName, String name, boolean isEnabled) {
        return Purpose._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name)
                .isAccessControlEnabled(isEnabled);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Purpose, from a potentially
     * more-complete Purpose object.
     *
     * @return the minimal object necessary to update the Purpose, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for Purpose are not found in the initial object
     */
    @Override
    public PurposeBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        if (this.getIsAccessControlEnabled() == null) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, TYPE_NAME, "isAccessControlEnabled");
        }
        return updater(this.getQualifiedName(), this.getName(), this.getIsAccessControlEnabled());
    }

    /**
     * Find a Purpose by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the purpose, if found.
     *
     * @param client connectivity to the Atlan tenant in which to search for the purpose
     * @param name of the Purpose
     * @return all Purposes with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Purpose does not exist
     */
    public static List<Purpose> findByName(AtlanClient client, String name) throws AtlanException {
        return findByName(client, name, (List<AtlanField>) null);
    }

    /**
     * Find a Purpose by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the purpose
     * @param name of the Purpose
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the Purpose
     * @return all Purposes with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Purpose does not exist
     */
    public static List<Purpose> findByName(AtlanClient client, String name, Collection<String> attributes)
            throws AtlanException {
        List<Purpose> results = new ArrayList<>();
        Purpose.select(client)
                .where(Purpose.NAME.eq(name))
                ._includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof Purpose)
                .forEach(p -> results.add((Purpose) p));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.PURPOSE_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Find a Purpose by its human-readable name.
     *
     * @param client connectivity to the Atlan tenant in which to search for the Purpose
     * @param name of the Purpose
     * @param attributes an optional list of attributes (checked) to retrieve for the Purpose
     * @return all Purposes with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Purpose does not exist
     */
    public static List<Purpose> findByName(AtlanClient client, String name, List<AtlanField> attributes)
            throws AtlanException {
        List<Purpose> results = new ArrayList<>();
        Purpose.select(client)
                .where(Purpose.NAME.eq(name))
                .includesOnResults(attributes == null ? Collections.emptyList() : attributes)
                .stream()
                .filter(a -> a instanceof Purpose)
                .forEach(p -> results.add((Purpose) p));
        if (results.isEmpty()) {
            throw new NotFoundException(ErrorCode.PURPOSE_NOT_FOUND_BY_NAME, name);
        }
        return results;
    }

    /**
     * Builds the minimal object necessary to create a metadata policy for a Purpose.
     *
     * @param client connectivity to the Atlan tenant on which the policy is intended to be created
     * @param name of the policy
     * @param purposeId unique identifier (GUID) of the purpose for which to create this metadata policy
     * @param policyType type of policy (for example allow vs deny)
     * @param actions to include in the policy
     * @param policyGroups groups to whom this policy applies, given as internal group names (at least one of these or policyUsers must be specified)
     * @param policyUsers users to whom this policy applies, given as usernames (at least one of these or policyGroups must be specified)
     * @param allUsers whether to apply this policy to all users (true) or not (false). If true this will override the other users and groups parameters.
     * @return the minimal request necessary to create the metadata policy for the Purpose, as a builder
     * @throws AtlanException on any other error related to the request, such as an inability to find the specified users or groups
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createMetadataPolicy(
            AtlanClient client,
            String name,
            String purposeId,
            AuthPolicyType policyType,
            Collection<PurposeMetadataAction> actions,
            Collection<String> policyGroups,
            Collection<String> policyUsers,
            boolean allUsers)
            throws AtlanException {
        boolean targetFound = false;
        AuthPolicy.AuthPolicyBuilder<?, ?> builder = AuthPolicy.creator(name)
                .policyActions(actions)
                .policyCategory(AuthPolicyCategory.PURPOSE)
                .policyType(policyType)
                .policyResourceCategory(AuthPolicyResourceCategory.TAG)
                .policyServiceName("atlas_tag")
                .policySubCategory("metadata")
                .accessControl(Purpose.refByGuid(purposeId));
        if (allUsers) {
            targetFound = true;
            builder.policyGroup("public");
        } else {
            if (policyGroups != null && !policyGroups.isEmpty()) {
                for (String groupName : policyGroups) {
                    client.getGroupCache().getIdForName(groupName);
                }
                targetFound = true;
                builder.policyGroups(policyGroups);
            } else {
                builder.nullField("policyGroups");
            }
            if (policyUsers != null && !policyUsers.isEmpty()) {
                for (String userName : policyUsers) {
                    client.getUserCache().getIdForName(userName);
                }
                targetFound = true;
                builder.policyUsers(policyUsers);
            } else {
                builder.nullField("policyUsers");
            }
        }
        if (targetFound) {
            return builder;
        } else {
            throw new InvalidRequestException(ErrorCode.NO_USERS_FOR_POLICY);
        }
    }

    /**
     * Builds the minimal object necessary to create a data policy for a Purpose.
     *
     * @param client connectivity to the Atlan tenant on which the policy is intended to be created
     * @param name of the policy
     * @param purposeId unique identifier (GUID) of the purpose for which to create this data policy
     * @param policyType type of policy (for example allow vs deny)
     * @param policyGroups groups to whom this policy applies, given as internal group names (at least one of these or policyUsers must be specified)
     * @param policyUsers users to whom this policy applies, given as usernames (at least one of these or policyGroups must be specified)
     * @param allUsers whether to apply this policy to all users (true) or not (false). If true this will override the other users and groups parameters.
     * @return the minimal request necessary to create the data policy for the Purpose, as a builder
     * @throws AtlanException on any other error related to the request, such as an inability to find the specified users or groups
     */
    public static AuthPolicy.AuthPolicyBuilder<?, ?> createDataPolicy(
            AtlanClient client,
            String name,
            String purposeId,
            AuthPolicyType policyType,
            Collection<String> policyGroups,
            Collection<String> policyUsers,
            boolean allUsers)
            throws AtlanException {
        boolean targetFound = false;
        AuthPolicy.AuthPolicyBuilder<?, ?> builder = AuthPolicy.creator(name)
                .policyAction(DataAction.SELECT)
                .policyCategory(AuthPolicyCategory.PURPOSE)
                .policyType(policyType)
                .policyResourceCategory(AuthPolicyResourceCategory.TAG)
                .policyServiceName("atlas_tag")
                .policySubCategory("data")
                .accessControl(Purpose.refByGuid(purposeId));
        if (allUsers) {
            targetFound = true;
            builder.policyGroup("public");
        } else {
            if (policyGroups != null && !policyGroups.isEmpty()) {
                for (String groupName : policyGroups) {
                    client.getGroupCache().getIdForName(groupName);
                }
                targetFound = true;
                builder.policyGroups(policyGroups);
            } else {
                builder.nullField("policyGroups");
            }
            if (policyUsers != null && !policyUsers.isEmpty()) {
                for (String userName : policyUsers) {
                    client.getUserCache().getIdForName(userName);
                }
                targetFound = true;
                builder.policyUsers(policyUsers);
            } else {
                builder.nullField("policyUsers");
            }
        }
        if (targetFound) {
            return builder;
        } else {
            throw new InvalidRequestException(ErrorCode.NO_USERS_FOR_POLICY);
        }
    }

    /**
     * Remove the system description from a Purpose.
     *
     * @param client connectivity to the Atlan tenant from which to remove this Purpose's description
     * @param qualifiedName of the Purpose
     * @param name of the Purpose
     * @param isEnabled whether the Purpose should be activated (true) or deactivated (false)
     * @return the updated Purpose, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Purpose removeDescription(AtlanClient client, String qualifiedName, String name, boolean isEnabled)
            throws AtlanException {
        return (Purpose) Asset.removeDescription(client, updater(qualifiedName, name, isEnabled));
    }

    /**
     * Remove the user's description from a Purpose.
     *
     * @param client connectivity to the Atlan tenant from which to remove this Purpose's description
     * @param qualifiedName of the Purpose
     * @param name of the Purpose
     * @param isEnabled whether the Purpose should be activated (true) or deactivated (false)
     * @return the updated Purpose, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Purpose removeUserDescription(
            AtlanClient client, String qualifiedName, String name, boolean isEnabled) throws AtlanException {
        return (Purpose) Asset.removeUserDescription(client, updater(qualifiedName, name, isEnabled));
    }

    /**
     * Add Atlan tags to a Purpose, without replacing existing Atlan tags linked to the Purpose.
     * Note: this operation must make two API calls — one to retrieve the Purpose's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Purpose
     * @param qualifiedName of the Purpose
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Purpose
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static Purpose appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Purpose) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Purpose, without replacing existing Atlan tags linked to the Purpose.
     * Note: this operation must make two API calls — one to retrieve the Purpose's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Purpose
     * @param qualifiedName of the Purpose
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Purpose
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static Purpose appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Purpose) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a Purpose.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Purpose
     * @param qualifiedName of the Purpose
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Purpose
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
