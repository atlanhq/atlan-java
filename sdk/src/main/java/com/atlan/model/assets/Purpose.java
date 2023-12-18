/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.core.AssetFilter;
import com.atlan.model.enums.AssetSidebarTab;
import com.atlan.model.enums.AuthPolicyCategory;
import com.atlan.model.enums.AuthPolicyResourceCategory;
import com.atlan.model.enums.AuthPolicyType;
import com.atlan.model.enums.DataAction;
import com.atlan.model.enums.PurposeMetadataAction;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.CompoundQuery;
import com.atlan.model.search.FluentSearch;
import com.atlan.util.QueryFactory;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
    SortedSet<String> denyAssetFilters;

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
     * @return a fluent search that includes all Purpose assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select() {
        return select(Atlan.getDefaultClient());
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
     * @param includeArchived when true, archived (soft-deleted) Purposes will be included
     * @return a fluent search that includes all Purpose assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(boolean includeArchived) {
        return select(Atlan.getDefaultClient(), includeArchived);
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
                FluentSearch.builder(client).where(CompoundQuery.assetType(TYPE_NAME));
        if (!includeArchived) {
            builder.where(CompoundQuery.ACTIVE);
        }
        return builder;
    }

    /**
     * Start an asset filter that will return all Purpose assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Purpose assets will be included.
     *
     * @return an asset filter that includes all Purpose assets
     * @deprecated replaced by {@link #select()}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all() {
        return all(Atlan.getDefaultClient());
    }

    /**
     * Start an asset filter that will return all Purpose assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Purpose assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return an asset filter that includes all Purpose assets
     * @deprecated replaced by {@link #select(AtlanClient)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client) {
        return all(client, false);
    }

    /**
     * Start an asset filter that will return all Purpose assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param includeArchived when true, archived (soft-deleted) Purposes will be included
     * @return an asset filter that includes all Purpose assets
     * @deprecated replaced by {@link #select(boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(boolean includeArchived) {
        return all(Atlan.getDefaultClient(), includeArchived);
    }

    /**
     * Start an asset filter that will return all Purpose assets.
     * Additional conditions can be chained onto the returned filter before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Purposes will be included
     * @return an asset filter that includes all Purpose assets
     * @deprecated replaced by {@link #select(AtlanClient, boolean)}
     */
    @Deprecated
    public static AssetFilter.AssetFilterBuilder all(AtlanClient client, boolean includeArchived) {
        AssetFilter.AssetFilterBuilder builder =
                AssetFilter.builder().client(client).filter(QueryFactory.type(TYPE_NAME));
        if (!includeArchived) {
            builder.filter(QueryFactory.active());
        }
        return builder;
    }

    /**
     * Reference to a Purpose by GUID.
     *
     * @param guid the GUID of the Purpose to reference
     * @return reference to a Purpose that can be used for defining a relationship to a Purpose
     */
    public static Purpose refByGuid(String guid) {
        return Purpose._internal().guid(guid).build();
    }

    /**
     * Reference to a Purpose by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Purpose to reference
     * @return reference to a Purpose that can be used for defining a relationship to a Purpose
     */
    public static Purpose refByQualifiedName(String qualifiedName) {
        return Purpose._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Purpose by one of its identifiers, complete with all of its relationships.
     *
     * @param id of the Purpose to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Purpose, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist or the provided GUID is not a Purpose
     */
    @JsonIgnore
    public static Purpose get(String id) throws AtlanException {
        return get(Atlan.getDefaultClient(), id);
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
        return get(client, id, true);
    }

    /**
     * Retrieves a Purpose by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Purpose to retrieve, either its GUID or its full qualifiedName
     * @param includeRelationships if true, all of the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Purpose, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist or the provided GUID is not a Purpose
     */
    @JsonIgnore
    public static Purpose get(AtlanClient client, String id, boolean includeRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Purpose) {
                return (Purpose) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeRelationships);
            if (asset instanceof Purpose) {
                return (Purpose) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a Purpose by its GUID, complete with all of its relationships.
     *
     * @param guid of the Purpose to retrieve
     * @return the requested full Purpose, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist or the provided GUID is not a Purpose
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static Purpose retrieveByGuid(String guid) throws AtlanException {
        return get(Atlan.getDefaultClient(), guid);
    }

    /**
     * Retrieves a Purpose by its GUID, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param guid of the Purpose to retrieve
     * @return the requested full Purpose, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist or the provided GUID is not a Purpose
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static Purpose retrieveByGuid(AtlanClient client, String guid) throws AtlanException {
        return get(client, guid);
    }

    /**
     * Retrieves a Purpose by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Purpose to retrieve
     * @return the requested full Purpose, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist
     * @deprecated see {@link #get(String)} instead
     */
    @Deprecated
    public static Purpose retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        return get(Atlan.getDefaultClient(), qualifiedName);
    }

    /**
     * Retrieves a Purpose by its qualifiedName, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param qualifiedName of the Purpose to retrieve
     * @return the requested full Purpose, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist
     * @deprecated see {@link #get(AtlanClient, String)} instead
     */
    @Deprecated
    public static Purpose retrieveByQualifiedName(AtlanClient client, String qualifiedName) throws AtlanException {
        return get(client, qualifiedName);
    }

    /**
     * Restore the archived (soft-deleted) Purpose to active.
     *
     * @param qualifiedName for the Purpose
     * @return true if the Purpose is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return restore(Atlan.getDefaultClient(), qualifiedName);
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
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (this.getIsAccessControlEnabled() == null) {
            missing.add("isAccessControlEnabled");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Purpose", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName(), this.getIsAccessControlEnabled());
    }

    /**
     * Find a Purpose by its human-readable name. Only the bare minimum set of attributes and no
     * relationships will be retrieved for the purpose, if found.
     *
     * @param name of the Purpose
     * @return all Purposes with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Purpose does not exist
     */
    public static List<Purpose> findByName(String name) throws AtlanException {
        return findByName(name, (List<AtlanField>) null);
    }

    /**
     * Find a Purpose by its human-readable name.
     *
     * @param name of the Purpose
     * @param attributes an optional collection of attributes (unchecked) to retrieve for the Purpose
     * @return all Purposes with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Purpose does not exist
     */
    public static List<Purpose> findByName(String name, Collection<String> attributes) throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
    }

    /**
     * Find a Purpose by its human-readable name.
     *
     * @param name of the Purpose
     * @param attributes an optional collection of attributes (checked) to retrieve for the Purpose
     * @return all Purposes with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Purpose does not exist
     */
    public static List<Purpose> findByName(String name, List<AtlanField> attributes) throws AtlanException {
        return findByName(Atlan.getDefaultClient(), name, attributes);
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
            String name,
            String purposeId,
            AuthPolicyType policyType,
            Collection<PurposeMetadataAction> actions,
            Collection<String> policyGroups,
            Collection<String> policyUsers,
            boolean allUsers)
            throws AtlanException {
        return createMetadataPolicy(
                Atlan.getDefaultClient(), name, purposeId, policyType, actions, policyGroups, policyUsers, allUsers);
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
            String name,
            String purposeId,
            AuthPolicyType policyType,
            Collection<String> policyGroups,
            Collection<String> policyUsers,
            boolean allUsers)
            throws AtlanException {
        return createDataPolicy(
                Atlan.getDefaultClient(), name, purposeId, policyType, policyGroups, policyUsers, allUsers);
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
     * @param qualifiedName of the Purpose
     * @param name of the Purpose
     * @param isEnabled whether the Purpose should be activated (true) or deactivated (false)
     * @return the updated Purpose, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Purpose removeDescription(String qualifiedName, String name, boolean isEnabled)
            throws AtlanException {
        return removeDescription(Atlan.getDefaultClient(), qualifiedName, name, isEnabled);
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
     * @param qualifiedName of the Purpose
     * @param name of the Purpose
     * @param isEnabled whether the Purpose should be activated (true) or deactivated (false)
     * @return the updated Purpose, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Purpose removeUserDescription(String qualifiedName, String name, boolean isEnabled)
            throws AtlanException {
        return removeUserDescription(Atlan.getDefaultClient(), qualifiedName, name, isEnabled);
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
     * @param qualifiedName of the Purpose
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Purpose
     */
    public static Purpose appendAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        return appendAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
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
     */
    public static Purpose appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Purpose) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Purpose, without replacing existing Atlan tags linked to the Purpose.
     * Note: this operation must make two API calls — one to retrieve the Purpose's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param qualifiedName of the Purpose
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Purpose
     */
    public static Purpose appendAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return appendAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
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
     */
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
     * Add Atlan tags to a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Purpose
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(String qualifiedName, List<String> atlanTagNames) throws AtlanException {
        addAtlanTags(Atlan.getDefaultClient(), qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Purpose.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the Purpose
     * @param qualifiedName of the Purpose
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Purpose
     * @deprecated see {@link #appendAtlanTags(String, List)} instead
     */
    @Deprecated
    public static void addAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        Asset.addAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Purpose.
     *
     * @param qualifiedName of the Purpose
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Purpose
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        addAtlanTags(
                Atlan.getDefaultClient(),
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Add Atlan tags to a Purpose.
     *
     * @param client connectivity to the Atlan tenant on which to add Atlan tags to the Purpose
     * @param qualifiedName of the Purpose
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems, or if any of the Atlan tags already exist on the Purpose
     * @deprecated see {@link #appendAtlanTags(String, List, boolean, boolean, boolean)} instead
     */
    @Deprecated
    public static void addAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        Asset.addAtlanTags(
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
     * @param qualifiedName of the Purpose
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Purpose
     */
    public static void removeAtlanTag(String qualifiedName, String atlanTagName) throws AtlanException {
        removeAtlanTag(Atlan.getDefaultClient(), qualifiedName, atlanTagName);
    }

    /**
     * Remove an Atlan tag from a Purpose.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Purpose
     * @param qualifiedName of the Purpose
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Purpose
     */
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
