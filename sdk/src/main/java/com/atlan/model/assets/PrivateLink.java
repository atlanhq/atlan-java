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
import com.atlan.model.enums.PrivateLinkCloud;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
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
 * Instance of a private connection (AWS PrivateLink, Azure Private Link or GCP Private Service Connect) from a customer network to Atlan, set up from the Networking admin page.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class PrivateLink extends Asset implements IPrivateLink, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "PrivateLink";

    /** Fixed typeName for PrivateLinks. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Cloud provider on which this private link is set up. */
    @Attribute
    PrivateLinkCloud privateLinkCloud;

    /** Destination port on the customer's service that traffic over this private link is sent to. */
    @Attribute
    Integer privateLinkDestPort;

    /** DNS names that the cloud provider assigned to the endpoint. */
    @Attribute
    @Singular
    SortedSet<String> privateLinkDnsEntries;

    /** Hostname that resolves to this private link's endpoint inside Atlan. */
    @Attribute
    String privateLinkDnsHostname;

    /** Identifier of the endpoint created on the Atlan side: an AWS VPC endpoint ID, an Azure private endpoint resource ID or a GCP forwarding rule. */
    @Attribute
    String privateLinkEndpointId;

    /** Name of the endpoint for this private link, as shown to users. */
    @Attribute
    String privateLinkEndpointName;

    /** URL of the pull request that applies the infrastructure change for this private link. */
    @Attribute
    String privateLinkGithubPrUrl;

    /** Time (epoch) at which the status of this private link last changed, in milliseconds. */
    @Attribute
    Long privateLinkLastStatusAt;

    /** Identifier of the most recent provisioning operation for this private link. */
    @Attribute
    String privateLinkOperationId;

    /** Raw state of the endpoint as reported by the cloud provider, for example pendingAcceptance on AWS or Pending on Azure. */
    @Attribute
    String privateLinkProviderState;

    /** Cloud region of the customer's service and of the Atlan endpoint, for example us-east-1. */
    @Attribute
    String privateLinkRegion;

    /** Identifier of the customer's service that Atlan connects to: an AWS VPC endpoint service name, an Azure Private Link service alias or a GCP service attachment URI. */
    @Attribute
    String privateLinkServiceName;

    /** Lifecycle status of this private link: DRAFT, EXPIRED, SUBMITTING, REQUESTED, SETTING_UP, WAITING_FOR_ACCEPTANCE, SETTING_UP_DNS, READY or FAILED. */
    @Attribute
    String privateLinkStatus;

    /** Human-readable detail about the current status, such as the reason for a failure. */
    @Attribute
    String privateLinkStatusMessage;

    /** JSON state of the setup wizard (path, version, step and answers), as saved by the frontend. */
    @Attribute
    String privateLinkWizardState;

    /** Availability zone IDs in which the customer's service is available, on clouds that use them (AWS). */
    @Attribute
    @Singular
    SortedSet<String> privateLinkZoneIds;

    /**
     * Builds the minimal object necessary to create a relationship to a PrivateLink, from a potentially
     * more-complete PrivateLink object.
     *
     * @return the minimal object necessary to relate to the PrivateLink
     * @throws InvalidRequestException if any of the minimal set of required properties for a PrivateLink relationship are not found in the initial object
     */
    @Override
    public PrivateLink trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all PrivateLink assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) PrivateLink assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all PrivateLink assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all PrivateLink assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) PrivateLinks will be included
     * @return a fluent search that includes all PrivateLink assets
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
     * Reference to a PrivateLink by GUID. Use this to create a relationship to this PrivateLink,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the PrivateLink to reference
     * @return reference to a PrivateLink that can be used for defining a relationship to a PrivateLink
     */
    public static PrivateLink refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a PrivateLink by GUID. Use this to create a relationship to this PrivateLink,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the PrivateLink to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a PrivateLink that can be used for defining a relationship to a PrivateLink
     */
    public static PrivateLink refByGuid(String guid, Reference.SaveSemantic semantic) {
        return PrivateLink._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a PrivateLink by qualifiedName. Use this to create a relationship to this PrivateLink,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the PrivateLink to reference
     * @return reference to a PrivateLink that can be used for defining a relationship to a PrivateLink
     */
    public static PrivateLink refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a PrivateLink by qualifiedName. Use this to create a relationship to this PrivateLink,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the PrivateLink to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a PrivateLink that can be used for defining a relationship to a PrivateLink
     */
    public static PrivateLink refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return PrivateLink._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a PrivateLink by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the PrivateLink to retrieve, either its GUID or its full qualifiedName
     * @return the requested full PrivateLink, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PrivateLink does not exist or the provided GUID is not a PrivateLink
     */
    @JsonIgnore
    public static PrivateLink get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a PrivateLink by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the PrivateLink to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full PrivateLink, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PrivateLink does not exist or the provided GUID is not a PrivateLink
     */
    @JsonIgnore
    public static PrivateLink get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof PrivateLink) {
                return (PrivateLink) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof PrivateLink) {
                return (PrivateLink) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a PrivateLink by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the PrivateLink to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the PrivateLink, including any relationships
     * @return the requested PrivateLink, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PrivateLink does not exist or the provided GUID is not a PrivateLink
     */
    @JsonIgnore
    public static PrivateLink get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a PrivateLink by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the PrivateLink to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the PrivateLink, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the PrivateLink
     * @return the requested PrivateLink, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the PrivateLink does not exist or the provided GUID is not a PrivateLink
     */
    @JsonIgnore
    public static PrivateLink get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = PrivateLink.select(client)
                    .where(PrivateLink.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof PrivateLink) {
                return (PrivateLink) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = PrivateLink.select(client)
                    .where(PrivateLink.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof PrivateLink) {
                return (PrivateLink) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) PrivateLink to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the PrivateLink
     * @return true if the PrivateLink is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a PrivateLink.
     *
     * @param qualifiedName of the PrivateLink
     * @param name of the PrivateLink
     * @return the minimal request necessary to update the PrivateLink, as a builder
     */
    public static PrivateLinkBuilder<?, ?> updater(String qualifiedName, String name) {
        return PrivateLink._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a PrivateLink,
     * from a potentially more-complete PrivateLink object.
     *
     * @return the minimal object necessary to update the PrivateLink, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a PrivateLink are not present in the initial object
     */
    @Override
    public PrivateLinkBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class PrivateLinkBuilder<C extends PrivateLink, B extends PrivateLinkBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a PrivateLink.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the PrivateLink
     * @param name of the PrivateLink
     * @return the updated PrivateLink, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PrivateLink removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PrivateLink) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a PrivateLink.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the PrivateLink
     * @param name of the PrivateLink
     * @return the updated PrivateLink, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PrivateLink removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PrivateLink) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a PrivateLink.
     *
     * @param client connectivity to the Atlan tenant from which to remove the PrivateLink's owners
     * @param qualifiedName of the PrivateLink
     * @param name of the PrivateLink
     * @return the updated PrivateLink, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PrivateLink removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PrivateLink) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a PrivateLink.
     *
     * @param client connectivity to the Atlan tenant on which to update the PrivateLink's certificate
     * @param qualifiedName of the PrivateLink
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated PrivateLink, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PrivateLink updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (PrivateLink)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a PrivateLink.
     *
     * @param client connectivity to the Atlan tenant from which to remove the PrivateLink's certificate
     * @param qualifiedName of the PrivateLink
     * @param name of the PrivateLink
     * @return the updated PrivateLink, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PrivateLink removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PrivateLink) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a PrivateLink.
     *
     * @param client connectivity to the Atlan tenant on which to update the PrivateLink's announcement
     * @param qualifiedName of the PrivateLink
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static PrivateLink updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (PrivateLink)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a PrivateLink.
     *
     * @param client connectivity to the Atlan client from which to remove the PrivateLink's announcement
     * @param qualifiedName of the PrivateLink
     * @param name of the PrivateLink
     * @return the updated PrivateLink, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static PrivateLink removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (PrivateLink) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the PrivateLink.
     *
     * @param client connectivity to the Atlan tenant on which to replace the PrivateLink's assigned terms
     * @param qualifiedName for the PrivateLink
     * @param name human-readable name of the PrivateLink
     * @param terms the list of terms to replace on the PrivateLink, or null to remove all terms from the PrivateLink
     * @return the PrivateLink that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static PrivateLink replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (PrivateLink) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the PrivateLink, without replacing existing terms linked to the PrivateLink.
     * Note: this operation must make two API calls — one to retrieve the PrivateLink's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the PrivateLink
     * @param qualifiedName for the PrivateLink
     * @param terms the list of terms to append to the PrivateLink
     * @return the PrivateLink that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static PrivateLink appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (PrivateLink) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a PrivateLink, without replacing all existing terms linked to the PrivateLink.
     * Note: this operation must make two API calls — one to retrieve the PrivateLink's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the PrivateLink
     * @param qualifiedName for the PrivateLink
     * @param terms the list of terms to remove from the PrivateLink, which must be referenced by GUID
     * @return the PrivateLink that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static PrivateLink removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (PrivateLink) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a PrivateLink, without replacing existing Atlan tags linked to the PrivateLink.
     * Note: this operation must make two API calls — one to retrieve the PrivateLink's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the PrivateLink
     * @param qualifiedName of the PrivateLink
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated PrivateLink
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static PrivateLink appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (PrivateLink) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a PrivateLink, without replacing existing Atlan tags linked to the PrivateLink.
     * Note: this operation must make two API calls — one to retrieve the PrivateLink's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the PrivateLink
     * @param qualifiedName of the PrivateLink
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated PrivateLink
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static PrivateLink appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (PrivateLink) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a PrivateLink.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a PrivateLink
     * @param qualifiedName of the PrivateLink
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the PrivateLink
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
