/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.enums.APIQueryParamTypeEnum;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.CertificateStatus;
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
 * Instances of APIField in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class APIField extends Asset implements IAPIField, IAPI, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "APIField";

    /** Fixed typeName for APIFields. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** External documentation of the API. */
    @Attribute
    @Singular
    Map<String, String> apiExternalDocs;

    /** Type of APIField. E.g. STRING, NUMBER etc. It is free text. */
    @Attribute
    String apiFieldType;

    /** Secondary Type of APIField. E.g. LIST/STRING, then LIST would be the secondary type. */
    @Attribute
    String apiFieldTypeSecondary;

    /** Whether authentication is optional (true) or required (false). */
    @Attribute
    Boolean apiIsAuthOptional;

    /** If this asset refers to an APIObject */
    @Attribute
    Boolean apiIsObjectReference;

    /** APIObject asset containing this APIField. */
    @Attribute
    IAPIObject apiObject;

    /** Qualified name of the APIObject that is referred to by this asset. When apiIsObjectReference is true. */
    @Attribute
    String apiObjectQualifiedName;

    /** APIQuery asset containing this APIField. */
    @Attribute
    IAPIQuery apiQuery;

    /** If parent relationship type is APIQuery, then this attribute denotes if this is input or output parameter. */
    @Attribute
    APIQueryParamTypeEnum apiQueryParamType;

    /** Simple name of the API spec, if this asset is contained in an API spec. */
    @Attribute
    String apiSpecName;

    /** Unique name of the API spec, if this asset is contained in an API spec. */
    @Attribute
    String apiSpecQualifiedName;

    /** Type of API, for example: OpenAPI, GraphQL, etc. */
    @Attribute
    String apiSpecType;

    /** Version of the API specification. */
    @Attribute
    String apiSpecVersion;

    /** Tasks to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> inputToAirflowTasks;

    /** Processes to which this asset provides input. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> inputToSparkJobs;

    /** Attributes implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelAttribute> modelImplementedAttributes;

    /** Entities implemented by this asset. */
    @Attribute
    @Singular
    SortedSet<IModelEntity> modelImplementedEntities;

    /** Tasks from which this asset is output. */
    @Attribute
    @Singular
    SortedSet<IAirflowTask> outputFromAirflowTasks;

    /** Processes from which this asset is produced as output. */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ISparkJob> outputFromSparkJobs;

    /**
     * Builds the minimal object necessary to create a relationship to a APIField, from a potentially
     * more-complete APIField object.
     *
     * @return the minimal object necessary to relate to the APIField
     * @throws InvalidRequestException if any of the minimal set of required properties for a APIField relationship are not found in the initial object
     */
    @Override
    public APIField trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all APIField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) APIField assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all APIField assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all APIField assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) APIFields will be included
     * @return a fluent search that includes all APIField assets
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
     * Reference to a APIField by GUID. Use this to create a relationship to this APIField,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the APIField to reference
     * @return reference to a APIField that can be used for defining a relationship to a APIField
     */
    public static APIField refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a APIField by GUID. Use this to create a relationship to this APIField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the APIField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a APIField that can be used for defining a relationship to a APIField
     */
    public static APIField refByGuid(String guid, Reference.SaveSemantic semantic) {
        return APIField._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a APIField by qualifiedName. Use this to create a relationship to this APIField,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the APIField to reference
     * @return reference to a APIField that can be used for defining a relationship to a APIField
     */
    public static APIField refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a APIField by qualifiedName. Use this to create a relationship to this APIField,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the APIField to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a APIField that can be used for defining a relationship to a APIField
     */
    public static APIField refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return APIField._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a APIField by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the APIField to retrieve, either its GUID or its full qualifiedName
     * @return the requested full APIField, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIField does not exist or the provided GUID is not a APIField
     */
    @JsonIgnore
    public static APIField get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a APIField by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the APIField to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full APIField, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIField does not exist or the provided GUID is not a APIField
     */
    @JsonIgnore
    public static APIField get(AtlanClient client, String id, boolean includeAllRelationships) throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof APIField) {
                return (APIField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof APIField) {
                return (APIField) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a APIField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the APIField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the APIField, including any relationships
     * @return the requested APIField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIField does not exist or the provided GUID is not a APIField
     */
    @JsonIgnore
    public static APIField get(AtlanClient client, String id, Collection<AtlanField> attributes) throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a APIField by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the APIField to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the APIField, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the APIField
     * @return the requested APIField, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the APIField does not exist or the provided GUID is not a APIField
     */
    @JsonIgnore
    public static APIField get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = APIField.select(client)
                    .where(APIField.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof APIField) {
                return (APIField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = APIField.select(client)
                    .where(APIField.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof APIField) {
                return (APIField) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) APIField to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the APIField
     * @return true if the APIField is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a APIField.
     *
     * @param qualifiedName of the APIField
     * @param name of the APIField
     * @return the minimal request necessary to update the APIField, as a builder
     */
    public static APIFieldBuilder<?, ?> updater(String qualifiedName, String name) {
        return APIField._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a APIField, from a potentially
     * more-complete APIField object.
     *
     * @return the minimal object necessary to update the APIField, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for APIField are not found in the initial object
     */
    @Override
    public APIFieldBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Remove the system description from a APIField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the APIField
     * @param name of the APIField
     * @return the updated APIField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIField removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIField) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a APIField.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the APIField
     * @param name of the APIField
     * @return the updated APIField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIField removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIField) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a APIField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the APIField's owners
     * @param qualifiedName of the APIField
     * @param name of the APIField
     * @return the updated APIField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIField removeOwners(AtlanClient client, String qualifiedName, String name) throws AtlanException {
        return (APIField) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a APIField.
     *
     * @param client connectivity to the Atlan tenant on which to update the APIField's certificate
     * @param qualifiedName of the APIField
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated APIField, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APIField updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (APIField) Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a APIField.
     *
     * @param client connectivity to the Atlan tenant from which to remove the APIField's certificate
     * @param qualifiedName of the APIField
     * @param name of the APIField
     * @return the updated APIField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIField removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIField) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a APIField.
     *
     * @param client connectivity to the Atlan tenant on which to update the APIField's announcement
     * @param qualifiedName of the APIField
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static APIField updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (APIField) Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a APIField.
     *
     * @param client connectivity to the Atlan client from which to remove the APIField's announcement
     * @param qualifiedName of the APIField
     * @param name of the APIField
     * @return the updated APIField, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static APIField removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (APIField) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the APIField.
     *
     * @param client connectivity to the Atlan tenant on which to replace the APIField's assigned terms
     * @param qualifiedName for the APIField
     * @param name human-readable name of the APIField
     * @param terms the list of terms to replace on the APIField, or null to remove all terms from the APIField
     * @return the APIField that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static APIField replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (APIField) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the APIField, without replacing existing terms linked to the APIField.
     * Note: this operation must make two API calls — one to retrieve the APIField's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the APIField
     * @param qualifiedName for the APIField
     * @param terms the list of terms to append to the APIField
     * @return the APIField that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static APIField appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (APIField) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a APIField, without replacing all existing terms linked to the APIField.
     * Note: this operation must make two API calls — one to retrieve the APIField's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the APIField
     * @param qualifiedName for the APIField
     * @param terms the list of terms to remove from the APIField, which must be referenced by GUID
     * @return the APIField that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static APIField removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (APIField) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a APIField, without replacing existing Atlan tags linked to the APIField.
     * Note: this operation must make two API calls — one to retrieve the APIField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the APIField
     * @param qualifiedName of the APIField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated APIField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static APIField appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (APIField) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a APIField, without replacing existing Atlan tags linked to the APIField.
     * Note: this operation must make two API calls — one to retrieve the APIField's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the APIField
     * @param qualifiedName of the APIField
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated APIField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static APIField appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (APIField) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a APIField.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a APIField
     * @param qualifiedName of the APIField
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the APIField
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
