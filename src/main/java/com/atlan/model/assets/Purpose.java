/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.cache.GroupCache;
import com.atlan.cache.UserCache;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AssetSidebarTab;
import com.atlan.model.enums.AuthPolicyCategory;
import com.atlan.model.enums.AuthPolicyResourceCategory;
import com.atlan.model.enums.AuthPolicyType;
import com.atlan.model.enums.DataAction;
import com.atlan.model.enums.KeywordFields;
import com.atlan.model.enums.PurposeMetadataAction;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.util.QueryFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Instance of a Purpose access control object in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
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
    @Singular
    SortedSet<AssetSidebarTab> denyAssetTabs;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> denyCustomMetadataGuids;

    /** TBC */
    @Attribute
    Boolean isAccessControlEnabled;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<IAuthPolicy> policies;

    /** Tags on which this purpose is applied. */
    @Attribute
    @Singular
    @JsonProperty("purposeClassifications")
    SortedSet<String> purposeAtlanTags;

    /**
     * Reference to a Purpose by GUID.
     *
     * @param guid the GUID of the Purpose to reference
     * @return reference to a Purpose that can be used for defining a relationship to a Purpose
     */
    public static Purpose refByGuid(String guid) {
        return Purpose.builder().guid(guid).build();
    }

    /**
     * Reference to a Purpose by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the Purpose to reference
     * @return reference to a Purpose that can be used for defining a relationship to a Purpose
     */
    public static Purpose refByQualifiedName(String qualifiedName) {
        return Purpose.builder()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Retrieves a Purpose by its GUID, complete with all of its relationships.
     *
     * @param guid of the Purpose to retrieve
     * @return the requested full Purpose, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist or the provided GUID is not a Purpose
     */
    public static Purpose retrieveByGuid(String guid) throws AtlanException {
        Asset asset = Asset.retrieveFull(guid);
        if (asset == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, guid);
        } else if (asset instanceof Purpose) {
            return (Purpose) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, guid, "Purpose");
        }
    }

    /**
     * Retrieves a Purpose by its qualifiedName, complete with all of its relationships.
     *
     * @param qualifiedName of the Purpose to retrieve
     * @return the requested full Purpose, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Purpose does not exist
     */
    public static Purpose retrieveByQualifiedName(String qualifiedName) throws AtlanException {
        Asset asset = Asset.retrieveFull(TYPE_NAME, qualifiedName);
        if (asset instanceof Purpose) {
            return (Purpose) asset;
        } else {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, qualifiedName, "Purpose");
        }
    }

    /**
     * Restore the archived (soft-deleted) Purpose to active.
     *
     * @param qualifiedName for the Purpose
     * @return true if the Purpose is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(String qualifiedName) throws AtlanException {
        return Asset.restore(TYPE_NAME, qualifiedName);
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
        return Purpose.builder()
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
        return Purpose.builder().qualifiedName(qualifiedName).name(name).isAccessControlEnabled(isEnabled);
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
     * Find a Purpose by its human-readable name.
     *
     * @param name of the Purpose
     * @param attributes an optional collection of attributes to retrieve for the Purpose
     * @return all Purposes with that name, if found
     * @throws AtlanException on any API problems
     * @throws NotFoundException if the Purpose does not exist
     */
    public static List<Purpose> findByName(String name, Collection<String> attributes) throws AtlanException {
        Query filter = QueryFactory.CompoundQuery.builder()
                .must(QueryFactory.beActive())
                .must(QueryFactory.beOfType(TYPE_NAME))
                .must(QueryFactory.have(KeywordFields.NAME).eq(name))
                .build()
                ._toQuery();
        IndexSearchRequest.IndexSearchRequestBuilder<?, ?> builder = IndexSearchRequest.builder(filter);
        if (attributes != null && !attributes.isEmpty()) {
            builder.attributes(attributes);
        }
        IndexSearchRequest request = builder.build();
        IndexSearchResponse response = request.search();
        List<Purpose> purposes = new ArrayList<>();
        if (response != null) {
            List<Asset> results = response.getAssets();
            while (results != null) {
                for (Asset result : results) {
                    if (result instanceof Purpose) {
                        purposes.add((Purpose) result);
                    }
                }
                response = response.getNextPage();
                results = response.getAssets();
            }
        }
        if (purposes.isEmpty()) {
            throw new NotFoundException(ErrorCode.PURPOSE_NOT_FOUND_BY_NAME, name);
        } else {
            return purposes;
        }
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
                for (String groupAlias : policyGroups) {
                    GroupCache.getIdForAlias(groupAlias);
                }
                targetFound = true;
                builder.policyGroups(policyGroups);
            } else {
                builder.nullField("policyGroups");
            }
            if (policyUsers != null && !policyUsers.isEmpty()) {
                for (String userName : policyUsers) {
                    UserCache.getIdForName(userName);
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
                for (String groupAlias : policyGroups) {
                    GroupCache.getIdForAlias(groupAlias);
                }
                targetFound = true;
                builder.policyGroups(policyGroups);
            } else {
                builder.nullField("policyGroups");
            }
            if (policyUsers != null && !policyUsers.isEmpty()) {
                for (String userName : policyUsers) {
                    UserCache.getIdForName(userName);
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
        return (Purpose) Asset.removeDescription(updater(qualifiedName, name, isEnabled));
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
        return (Purpose) Asset.removeUserDescription(updater(qualifiedName, name, isEnabled));
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
        return (Purpose) Asset.appendAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
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
        return (Purpose) Asset.appendAtlanTags(
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
        Asset.addAtlanTags(TYPE_NAME, qualifiedName, atlanTagNames);
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
        Asset.addAtlanTags(
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
        Asset.removeAtlanTag(TYPE_NAME, qualifiedName, atlanTagName);
    }
}
