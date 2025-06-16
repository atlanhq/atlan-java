/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanPolicyAction;
import com.atlan.model.enums.AuthPolicyCategory;
import com.atlan.model.enums.AuthPolicyResourceCategory;
import com.atlan.model.enums.AuthPolicyType;
import com.atlan.model.enums.DataMaskingType;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.AuthPolicyCondition;
import com.atlan.model.structs.AuthPolicyValiditySchedule;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Model to store an accesscontrol policy in Atlas
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("serial")
public class AuthPolicy extends Asset implements IAuthPolicy, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AuthPolicy";

    /** Fixed typeName for AuthPolicys. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** TBC */
    @Attribute
    IAccessControl accessControl;

    /** TBC */
    @Attribute
    Boolean isPolicyEnabled;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<AtlanPolicyAction> policyActions;

    /** TBC */
    @Attribute
    AuthPolicyCategory policyCategory;

    /** TBC */
    @Attribute
    @Singular
    List<AuthPolicyCondition> policyConditions;

    /** TBC */
    @Attribute
    Boolean policyDelegateAdmin;

    /** TBC */
    @Attribute
    String policyFilterCriteria;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> policyGroups;

    /** TBC */
    @Attribute
    DataMaskingType policyMaskType;

    /** TBC */
    @Attribute
    Integer policyPriority;

    /** TBC */
    @Attribute
    AuthPolicyResourceCategory policyResourceCategory;

    /** TBC */
    @Attribute
    String policyResourceSignature;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> policyResources;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> policyRoles;

    /** TBC */
    @Attribute
    String policyServiceName;

    /** TBC */
    @Attribute
    String policySubCategory;

    /** TBC */
    @Attribute
    AuthPolicyType policyType;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<String> policyUsers;

    /** TBC */
    @Attribute
    @Singular("addPolicyValiditySchedule")
    List<AuthPolicyValiditySchedule> policyValiditySchedule;

    /**
     * Builds the minimal object necessary to create a relationship to a AuthPolicy, from a potentially
     * more-complete AuthPolicy object.
     *
     * @return the minimal object necessary to relate to the AuthPolicy
     * @throws InvalidRequestException if any of the minimal set of required properties for a AuthPolicy relationship are not found in the initial object
     */
    @Override
    public AuthPolicy trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all AuthPolicy assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) AuthPolicy assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all AuthPolicy assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all AuthPolicy assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) AuthPolicys will be included
     * @return a fluent search that includes all AuthPolicy assets
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
     * Reference to a AuthPolicy by GUID. Use this to create a relationship to this AuthPolicy,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the AuthPolicy to reference
     * @return reference to a AuthPolicy that can be used for defining a relationship to a AuthPolicy
     */
    public static AuthPolicy refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AuthPolicy by GUID. Use this to create a relationship to this AuthPolicy,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the AuthPolicy to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AuthPolicy that can be used for defining a relationship to a AuthPolicy
     */
    public static AuthPolicy refByGuid(String guid, Reference.SaveSemantic semantic) {
        return AuthPolicy._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a AuthPolicy by qualifiedName. Use this to create a relationship to this AuthPolicy,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the AuthPolicy to reference
     * @return reference to a AuthPolicy that can be used for defining a relationship to a AuthPolicy
     */
    public static AuthPolicy refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a AuthPolicy by qualifiedName. Use this to create a relationship to this AuthPolicy,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the AuthPolicy to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a AuthPolicy that can be used for defining a relationship to a AuthPolicy
     */
    public static AuthPolicy refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return AuthPolicy._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a AuthPolicy by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AuthPolicy to retrieve, either its GUID or its full qualifiedName
     * @return the requested full AuthPolicy, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AuthPolicy does not exist or the provided GUID is not a AuthPolicy
     */
    @JsonIgnore
    public static AuthPolicy get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a AuthPolicy by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AuthPolicy to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full AuthPolicy, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AuthPolicy does not exist or the provided GUID is not a AuthPolicy
     */
    @JsonIgnore
    public static AuthPolicy get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof AuthPolicy) {
                return (AuthPolicy) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof AuthPolicy) {
                return (AuthPolicy) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a AuthPolicy by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AuthPolicy to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AuthPolicy, including any relationships
     * @return the requested AuthPolicy, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AuthPolicy does not exist or the provided GUID is not a AuthPolicy
     */
    @JsonIgnore
    public static AuthPolicy get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a AuthPolicy by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the AuthPolicy to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the AuthPolicy, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the AuthPolicy
     * @return the requested AuthPolicy, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the AuthPolicy does not exist or the provided GUID is not a AuthPolicy
     */
    @JsonIgnore
    public static AuthPolicy get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = AuthPolicy.select(client)
                    .where(AuthPolicy.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof AuthPolicy) {
                return (AuthPolicy) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = AuthPolicy.select(client)
                    .where(AuthPolicy.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof AuthPolicy) {
                return (AuthPolicy) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) AuthPolicy to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the AuthPolicy
     * @return true if the AuthPolicy is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to create an AuthPolicy.
     * Note: this method is only for internal use; for creating policies specific to a persona
     * or purpose, use the helper methods from those classes.
     *
     * @param name of the AuthPolicy
     * @return the minimal request necessary to create the AuthPolicy, as a builder
     * @see Persona#createMetadataPolicy(String, String, AuthPolicyType, Collection, String, Collection)
     * @see Persona#createDataPolicy(String, String, AuthPolicyType, String, Collection)
     * @see Persona#createGlossaryPolicy(String, String, AuthPolicyType, Collection, Collection)
     * @see Purpose#createMetadataPolicy(AtlanClient, String, String, AuthPolicyType, Collection, Collection, Collection, boolean)
     * @see Purpose#createDataPolicy(AtlanClient, String, String, AuthPolicyType, Collection, Collection, boolean)
     */
    public static AuthPolicyBuilder<?, ?> creator(String name) {
        return AuthPolicy._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(name)
                .name(name)
                .displayName("");
    }

    /**
     * Builds the minimal object necessary to apply an update to an AuthPolicy, from a potentially
     * more-complete AuthPolicy object.
     *
     * @return the minimal object necessary to update the AuthPolicy, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for AuthPolicy are not found in the initial object
     */
    @Override
    public AuthPolicyBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        throw new InvalidRequestException(ErrorCode.FULL_UPDATE_ONLY, "AuthPolicy");
    }

    public abstract static class AuthPolicyBuilder<C extends AuthPolicy, B extends AuthPolicyBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Add Atlan tags to a AuthPolicy, without replacing existing Atlan tags linked to the AuthPolicy.
     * Note: this operation must make two API calls — one to retrieve the AuthPolicy's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AuthPolicy
     * @param qualifiedName of the AuthPolicy
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated AuthPolicy
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static AuthPolicy appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (AuthPolicy) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a AuthPolicy, without replacing existing Atlan tags linked to the AuthPolicy.
     * Note: this operation must make two API calls — one to retrieve the AuthPolicy's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the AuthPolicy
     * @param qualifiedName of the AuthPolicy
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated AuthPolicy
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static AuthPolicy appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (AuthPolicy) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a AuthPolicy.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a AuthPolicy
     * @param qualifiedName of the AuthPolicy
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the AuthPolicy
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
