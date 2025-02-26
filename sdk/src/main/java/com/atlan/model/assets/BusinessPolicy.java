/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.BusinessPolicyRule;
import com.atlan.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Instance of a business policy template in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
public class BusinessPolicy extends Asset implements IBusinessPolicy, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "BusinessPolicy";

    /** Fixed typeName for BusinessPolicys. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Base parent Guid for policy used in version */
    @Attribute
    String businessPolicyBaseParentGuid;

    /** Business Policy Filter ES DSL to denote the associate asset/s involved. */
    @Attribute
    String businessPolicyFilterDSL;

    /** Body of the business policy, a long readme like document */
    @Attribute
    String businessPolicyLongDescription;

    /** Duration for the business policy to complete review. */
    @Attribute
    String businessPolicyReviewPeriod;

    /** List of rules applied to this business policy. */
    @Attribute
    @Singular
    List<BusinessPolicyRule> businessPolicyRules;

    /** Selected approval workflow id for business policy */
    @Attribute
    String businessPolicySelectedApprovalWF;

    /** Type of business policy */
    @Attribute
    String businessPolicyType;

    /** Validity start date of the policy */
    @Attribute
    @Date
    Long businessPolicyValidFrom;

    /** Validity end date of the policy */
    @Attribute
    @Date
    Long businessPolicyValidTill;

    /** Version of the policy */
    @Attribute
    Integer businessPolicyVersion;

    /** Exception assigned to business polices */
    @Attribute
    @Singular("exceptionForBusinessPolicy")
    SortedSet<IBusinessPolicyException> exceptionsForBusinessPolicy;

    /** BusinessPolicy that have the same (or relatable) compliance */
    @Attribute
    @Singular
    SortedSet<IBusinessPolicy> relatedBusinessPolicies;

    /**
     * Builds the minimal object necessary to create a relationship to a BusinessPolicy, from a potentially
     * more-complete BusinessPolicy object.
     *
     * @return the minimal object necessary to relate to the BusinessPolicy
     * @throws InvalidRequestException if any of the minimal set of required properties for a BusinessPolicy relationship are not found in the initial object
     */
    @Override
    public BusinessPolicy trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all BusinessPolicy assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) BusinessPolicy assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all BusinessPolicy assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all BusinessPolicy assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) BusinessPolicys will be included
     * @return a fluent search that includes all BusinessPolicy assets
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
     * Reference to a BusinessPolicy by GUID. Use this to create a relationship to this BusinessPolicy,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the BusinessPolicy to reference
     * @return reference to a BusinessPolicy that can be used for defining a relationship to a BusinessPolicy
     */
    public static BusinessPolicy refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a BusinessPolicy by GUID. Use this to create a relationship to this BusinessPolicy,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the BusinessPolicy to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a BusinessPolicy that can be used for defining a relationship to a BusinessPolicy
     */
    public static BusinessPolicy refByGuid(String guid, Reference.SaveSemantic semantic) {
        return BusinessPolicy._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a BusinessPolicy by qualifiedName. Use this to create a relationship to this BusinessPolicy,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the BusinessPolicy to reference
     * @return reference to a BusinessPolicy that can be used for defining a relationship to a BusinessPolicy
     */
    public static BusinessPolicy refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a BusinessPolicy by qualifiedName. Use this to create a relationship to this BusinessPolicy,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the BusinessPolicy to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a BusinessPolicy that can be used for defining a relationship to a BusinessPolicy
     */
    public static BusinessPolicy refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return BusinessPolicy._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a BusinessPolicy by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the BusinessPolicy to retrieve, either its GUID or its full qualifiedName
     * @return the requested full BusinessPolicy, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BusinessPolicy does not exist or the provided GUID is not a BusinessPolicy
     */
    @JsonIgnore
    public static BusinessPolicy get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a BusinessPolicy by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the BusinessPolicy to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full BusinessPolicy, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BusinessPolicy does not exist or the provided GUID is not a BusinessPolicy
     */
    @JsonIgnore
    public static BusinessPolicy get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof BusinessPolicy) {
                return (BusinessPolicy) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof BusinessPolicy) {
                return (BusinessPolicy) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a BusinessPolicy by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the BusinessPolicy to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the BusinessPolicy, including any relationships
     * @return the requested BusinessPolicy, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BusinessPolicy does not exist or the provided GUID is not a BusinessPolicy
     */
    @JsonIgnore
    public static BusinessPolicy get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a BusinessPolicy by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the BusinessPolicy to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the BusinessPolicy, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the BusinessPolicy
     * @return the requested BusinessPolicy, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the BusinessPolicy does not exist or the provided GUID is not a BusinessPolicy
     */
    @JsonIgnore
    public static BusinessPolicy get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = BusinessPolicy.select(client)
                    .where(BusinessPolicy.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof BusinessPolicy) {
                return (BusinessPolicy) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = BusinessPolicy.select(client)
                    .where(BusinessPolicy.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof BusinessPolicy) {
                return (BusinessPolicy) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) BusinessPolicy to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the BusinessPolicy
     * @return true if the BusinessPolicy is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a BusinessPolicy.
     *
     * @param qualifiedName of the BusinessPolicy
     * @param name of the BusinessPolicy
     * @return the minimal request necessary to update the BusinessPolicy, as a builder
     */
    public static BusinessPolicyBuilder<?, ?> updater(String qualifiedName, String name) {
        return BusinessPolicy._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a BusinessPolicy, from a potentially
     * more-complete BusinessPolicy object.
     *
     * @return the minimal object necessary to update the BusinessPolicy, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for BusinessPolicy are not found in the initial object
     */
    @Override
    public BusinessPolicyBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a BusinessPolicy.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the BusinessPolicy
     * @param name of the BusinessPolicy
     * @return the updated BusinessPolicy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicy removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BusinessPolicy) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a BusinessPolicy.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the BusinessPolicy
     * @param name of the BusinessPolicy
     * @return the updated BusinessPolicy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicy removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BusinessPolicy) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a BusinessPolicy.
     *
     * @param client connectivity to the Atlan tenant from which to remove the BusinessPolicy's owners
     * @param qualifiedName of the BusinessPolicy
     * @param name of the BusinessPolicy
     * @return the updated BusinessPolicy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicy removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BusinessPolicy) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a BusinessPolicy.
     *
     * @param client connectivity to the Atlan tenant on which to update the BusinessPolicy's certificate
     * @param qualifiedName of the BusinessPolicy
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated BusinessPolicy, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicy updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (BusinessPolicy)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a BusinessPolicy.
     *
     * @param client connectivity to the Atlan tenant from which to remove the BusinessPolicy's certificate
     * @param qualifiedName of the BusinessPolicy
     * @param name of the BusinessPolicy
     * @return the updated BusinessPolicy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicy removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BusinessPolicy) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a BusinessPolicy.
     *
     * @param client connectivity to the Atlan tenant on which to update the BusinessPolicy's announcement
     * @param qualifiedName of the BusinessPolicy
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicy updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (BusinessPolicy)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a BusinessPolicy.
     *
     * @param client connectivity to the Atlan client from which to remove the BusinessPolicy's announcement
     * @param qualifiedName of the BusinessPolicy
     * @param name of the BusinessPolicy
     * @return the updated BusinessPolicy, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicy removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (BusinessPolicy) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the BusinessPolicy.
     *
     * @param client connectivity to the Atlan tenant on which to replace the BusinessPolicy's assigned terms
     * @param qualifiedName for the BusinessPolicy
     * @param name human-readable name of the BusinessPolicy
     * @param terms the list of terms to replace on the BusinessPolicy, or null to remove all terms from the BusinessPolicy
     * @return the BusinessPolicy that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static BusinessPolicy replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (BusinessPolicy) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the BusinessPolicy, without replacing existing terms linked to the BusinessPolicy.
     * Note: this operation must make two API calls — one to retrieve the BusinessPolicy's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the BusinessPolicy
     * @param qualifiedName for the BusinessPolicy
     * @param terms the list of terms to append to the BusinessPolicy
     * @return the BusinessPolicy that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static BusinessPolicy appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (BusinessPolicy) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a BusinessPolicy, without replacing all existing terms linked to the BusinessPolicy.
     * Note: this operation must make two API calls — one to retrieve the BusinessPolicy's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the BusinessPolicy
     * @param qualifiedName for the BusinessPolicy
     * @param terms the list of terms to remove from the BusinessPolicy, which must be referenced by GUID
     * @return the BusinessPolicy that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static BusinessPolicy removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (BusinessPolicy) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a BusinessPolicy, without replacing existing Atlan tags linked to the BusinessPolicy.
     * Note: this operation must make two API calls — one to retrieve the BusinessPolicy's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the BusinessPolicy
     * @param qualifiedName of the BusinessPolicy
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated BusinessPolicy
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static BusinessPolicy appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (BusinessPolicy) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a BusinessPolicy, without replacing existing Atlan tags linked to the BusinessPolicy.
     * Note: this operation must make two API calls — one to retrieve the BusinessPolicy's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the BusinessPolicy
     * @param qualifiedName of the BusinessPolicy
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated BusinessPolicy
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static BusinessPolicy appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (BusinessPolicy) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a BusinessPolicy.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a BusinessPolicy
     * @param qualifiedName of the BusinessPolicy
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the BusinessPolicy
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
