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
import com.atlan.model.structs.GoogleLabel;
import com.atlan.model.structs.GoogleTag;
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
 * Base class for Google assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class Google extends Asset implements IGoogle, IAsset, IReferenceable, ICloud {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Google";

    /** Fixed typeName for Googles. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Uniform resource name (URN) for the asset: AWS ARN, Google Cloud URI, Azure resource ID, Oracle OCID, and so on. */
    @Attribute
    String cloudUniformResourceName;

    /** List of labels that have been applied to the asset in Google. */
    @Attribute
    @Singular
    List<GoogleLabel> googleLabels;

    /** Location of this asset in Google. */
    @Attribute
    String googleLocation;

    /** Type of location of this asset in Google. */
    @Attribute
    String googleLocationType;

    /** ID of the project in which the asset exists. */
    @Attribute
    String googleProjectId;

    /** Name of the project in which the asset exists. */
    @Attribute
    String googleProjectName;

    /** Number of the project in which the asset exists. */
    @Attribute
    Long googleProjectNumber;

    /** Service in Google in which the asset exists. */
    @Attribute
    String googleService;

    /** List of tags that have been applied to the asset in Google. */
    @Attribute
    @Singular
    List<GoogleTag> googleTags;

    /**
     * Builds the minimal object necessary to create a relationship to a Google, from a potentially
     * more-complete Google object.
     *
     * @return the minimal object necessary to relate to the Google
     * @throws InvalidRequestException if any of the minimal set of required properties for a Google relationship are not found in the initial object
     */
    @Override
    public Google trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all Google assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) Google assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all Google assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all Google assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) Googles will be included
     * @return a fluent search that includes all Google assets
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
     * Reference to a Google by GUID. Use this to create a relationship to this Google,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the Google to reference
     * @return reference to a Google that can be used for defining a relationship to a Google
     */
    public static Google refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Google by GUID. Use this to create a relationship to this Google,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the Google to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Google that can be used for defining a relationship to a Google
     */
    public static Google refByGuid(String guid, Reference.SaveSemantic semantic) {
        return Google._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a Google by qualifiedName. Use this to create a relationship to this Google,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the Google to reference
     * @return reference to a Google that can be used for defining a relationship to a Google
     */
    public static Google refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a Google by qualifiedName. Use this to create a relationship to this Google,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the Google to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a Google that can be used for defining a relationship to a Google
     */
    public static Google refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return Google._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a Google by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Google to retrieve, either its GUID or its full qualifiedName
     * @return the requested full Google, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Google does not exist or the provided GUID is not a Google
     */
    @JsonIgnore
    public static Google get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a Google by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Google to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full Google, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Google does not exist or the provided GUID is not a Google
     */
    @JsonIgnore
    public static Google get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof Google) {
                return (Google) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof Google) {
                return (Google) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a Google by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Google to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Google, including any relationships
     * @return the requested Google, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Google does not exist or the provided GUID is not a Google
     */
    @JsonIgnore
    public static Google get(AtlanClient client, String id, Collection<AtlanField> attributes) throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a Google by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the Google to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the Google, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the Google
     * @return the requested Google, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the Google does not exist or the provided GUID is not a Google
     */
    @JsonIgnore
    public static Google get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = Google.select(client)
                    .where(Google.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof Google) {
                return (Google) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = Google.select(client)
                    .where(Google.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof Google) {
                return (Google) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) Google to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the Google
     * @return true if the Google is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a Google.
     *
     * @param qualifiedName of the Google
     * @param name of the Google
     * @return the minimal request necessary to update the Google, as a builder
     */
    public static GoogleBuilder<?, ?> updater(String qualifiedName, String name) {
        return Google._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a Google,
     * from a potentially more-complete Google object.
     *
     * @return the minimal object necessary to update the Google, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a Google are not present in the initial object
     */
    @Override
    public GoogleBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class GoogleBuilder<C extends Google, B extends GoogleBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a Google.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Google
     * @param name of the Google
     * @return the updated Google, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Google removeDescription(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Google) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a Google.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the Google
     * @param name of the Google
     * @return the updated Google, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Google removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Google) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a Google.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Google's owners
     * @param qualifiedName of the Google
     * @param name of the Google
     * @return the updated Google, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Google removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Google) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a Google.
     *
     * @param client connectivity to the Atlan tenant on which to update the Google's certificate
     * @param qualifiedName of the Google
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated Google, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Google updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (Google) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a Google.
     *
     * @param client connectivity to the Atlan tenant from which to remove the Google's certificate
     * @param qualifiedName of the Google
     * @param name of the Google
     * @return the updated Google, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Google removeCertificate(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (Google) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a Google.
     *
     * @param client connectivity to the Atlan tenant on which to update the Google's announcement
     * @param qualifiedName of the Google
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static Google updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (Google) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a Google.
     *
     * @param client connectivity to the Atlan client from which to remove the Google's announcement
     * @param qualifiedName of the Google
     * @param name of the Google
     * @return the updated Google, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static Google removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (Google) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the Google.
     *
     * @param client connectivity to the Atlan tenant on which to replace the Google's assigned terms
     * @param qualifiedName for the Google
     * @param name human-readable name of the Google
     * @param terms the list of terms to replace on the Google, or null to remove all terms from the Google
     * @return the Google that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static Google replaceTerms(AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Google) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the Google, without replacing existing terms linked to the Google.
     * Note: this operation must make two API calls — one to retrieve the Google's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the Google
     * @param qualifiedName for the Google
     * @param terms the list of terms to append to the Google
     * @return the Google that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static Google appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Google) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a Google, without replacing all existing terms linked to the Google.
     * Note: this operation must make two API calls — one to retrieve the Google's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the Google
     * @param qualifiedName for the Google
     * @param terms the list of terms to remove from the Google, which must be referenced by GUID
     * @return the Google that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static Google removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (Google) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a Google, without replacing existing Atlan tags linked to the Google.
     * Note: this operation must make two API calls — one to retrieve the Google's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Google
     * @param qualifiedName of the Google
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated Google
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static Google appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (Google) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a Google, without replacing existing Atlan tags linked to the Google.
     * Note: this operation must make two API calls — one to retrieve the Google's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the Google
     * @param qualifiedName of the Google
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated Google
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static Google appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (Google) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a Google.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a Google
     * @param qualifiedName of the Google
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the Google
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}