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
 * Base class for semantic measures across different sources.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SemanticMeasure extends Asset implements ISemanticMeasure, ISemantic, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SemanticMeasure";

    /** Fixed typeName for SemanticMeasures. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Unique identifier of the dataset this asset belongs to. */
    @Attribute
    String catalogDatasetGuid;

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

    /** Partial fields contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialField> partialChildFields;

    /** Partial objects contained in the asset. */
    @Attribute
    @Singular
    SortedSet<IPartialObject> partialChildObjects;

    /** Access level for the semantic field (e.g., public_access/private_access). */
    @Attribute
    String semanticAccessModifier;

    /** Data type of the semantic field. */
    @Attribute
    String semanticDataType;

    /** Column name or SQL expression for the semantic field. */
    @Attribute
    String semanticExpression;

    /** Labels associated with the semantic field. */
    @Attribute
    @Singular
    SortedSet<String> semanticLabels;

    /** Semantic model in which this measure exists. */
    @Attribute
    ISemanticModel semanticModel;

    /** Sample values for the semantic field. */
    @Attribute
    @Singular
    SortedSet<String> semanticSampleValues;

    /** Alternative names or terms for the semantic field. */
    @Attribute
    @Singular
    SortedSet<String> semanticSynonyms;

    /** Detailed type of the semantic field (e.g., type of measure, type of dimension, or type of entity). */
    @Attribute
    String semanticType;

    /**
     * Builds the minimal object necessary to create a relationship to a SemanticMeasure, from a potentially
     * more-complete SemanticMeasure object.
     *
     * @return the minimal object necessary to relate to the SemanticMeasure
     * @throws InvalidRequestException if any of the minimal set of required properties for a SemanticMeasure relationship are not found in the initial object
     */
    @Override
    public SemanticMeasure trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SemanticMeasure assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SemanticMeasure assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SemanticMeasure assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SemanticMeasure assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SemanticMeasures will be included
     * @return a fluent search that includes all SemanticMeasure assets
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
     * Reference to a SemanticMeasure by GUID. Use this to create a relationship to this SemanticMeasure,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SemanticMeasure to reference
     * @return reference to a SemanticMeasure that can be used for defining a relationship to a SemanticMeasure
     */
    public static SemanticMeasure refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SemanticMeasure by GUID. Use this to create a relationship to this SemanticMeasure,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SemanticMeasure to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SemanticMeasure that can be used for defining a relationship to a SemanticMeasure
     */
    public static SemanticMeasure refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SemanticMeasure._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SemanticMeasure by qualifiedName. Use this to create a relationship to this SemanticMeasure,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SemanticMeasure to reference
     * @return reference to a SemanticMeasure that can be used for defining a relationship to a SemanticMeasure
     */
    public static SemanticMeasure refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SemanticMeasure by qualifiedName. Use this to create a relationship to this SemanticMeasure,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SemanticMeasure to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SemanticMeasure that can be used for defining a relationship to a SemanticMeasure
     */
    public static SemanticMeasure refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SemanticMeasure._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SemanticMeasure by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SemanticMeasure to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SemanticMeasure, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SemanticMeasure does not exist or the provided GUID is not a SemanticMeasure
     */
    @JsonIgnore
    public static SemanticMeasure get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SemanticMeasure by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SemanticMeasure to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SemanticMeasure, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SemanticMeasure does not exist or the provided GUID is not a SemanticMeasure
     */
    @JsonIgnore
    public static SemanticMeasure get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SemanticMeasure) {
                return (SemanticMeasure) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SemanticMeasure) {
                return (SemanticMeasure) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SemanticMeasure by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SemanticMeasure to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SemanticMeasure, including any relationships
     * @return the requested SemanticMeasure, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SemanticMeasure does not exist or the provided GUID is not a SemanticMeasure
     */
    @JsonIgnore
    public static SemanticMeasure get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SemanticMeasure by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SemanticMeasure to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SemanticMeasure, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SemanticMeasure
     * @return the requested SemanticMeasure, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SemanticMeasure does not exist or the provided GUID is not a SemanticMeasure
     */
    @JsonIgnore
    public static SemanticMeasure get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SemanticMeasure.select(client)
                    .where(SemanticMeasure.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SemanticMeasure) {
                return (SemanticMeasure) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SemanticMeasure.select(client)
                    .where(SemanticMeasure.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SemanticMeasure) {
                return (SemanticMeasure) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SemanticMeasure to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SemanticMeasure
     * @return true if the SemanticMeasure is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SemanticMeasure.
     *
     * @param qualifiedName of the SemanticMeasure
     * @param name of the SemanticMeasure
     * @return the minimal request necessary to update the SemanticMeasure, as a builder
     */
    public static SemanticMeasureBuilder<?, ?> updater(String qualifiedName, String name) {
        return SemanticMeasure._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SemanticMeasure,
     * from a potentially more-complete SemanticMeasure object.
     *
     * @return the minimal object necessary to update the SemanticMeasure, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SemanticMeasure are not present in the initial object
     */
    @Override
    public SemanticMeasureBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SemanticMeasureBuilder<
                    C extends SemanticMeasure, B extends SemanticMeasureBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SemanticMeasure.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SemanticMeasure
     * @param name of the SemanticMeasure
     * @return the updated SemanticMeasure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SemanticMeasure removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SemanticMeasure) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SemanticMeasure.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SemanticMeasure
     * @param name of the SemanticMeasure
     * @return the updated SemanticMeasure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SemanticMeasure removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SemanticMeasure) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SemanticMeasure.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SemanticMeasure's owners
     * @param qualifiedName of the SemanticMeasure
     * @param name of the SemanticMeasure
     * @return the updated SemanticMeasure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SemanticMeasure removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SemanticMeasure) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SemanticMeasure.
     *
     * @param client connectivity to the Atlan tenant on which to update the SemanticMeasure's certificate
     * @param qualifiedName of the SemanticMeasure
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SemanticMeasure, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SemanticMeasure updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SemanticMeasure)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SemanticMeasure.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SemanticMeasure's certificate
     * @param qualifiedName of the SemanticMeasure
     * @param name of the SemanticMeasure
     * @return the updated SemanticMeasure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SemanticMeasure removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SemanticMeasure) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SemanticMeasure.
     *
     * @param client connectivity to the Atlan tenant on which to update the SemanticMeasure's announcement
     * @param qualifiedName of the SemanticMeasure
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SemanticMeasure updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SemanticMeasure)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SemanticMeasure.
     *
     * @param client connectivity to the Atlan client from which to remove the SemanticMeasure's announcement
     * @param qualifiedName of the SemanticMeasure
     * @param name of the SemanticMeasure
     * @return the updated SemanticMeasure, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SemanticMeasure removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SemanticMeasure) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SemanticMeasure.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SemanticMeasure's assigned terms
     * @param qualifiedName for the SemanticMeasure
     * @param name human-readable name of the SemanticMeasure
     * @param terms the list of terms to replace on the SemanticMeasure, or null to remove all terms from the SemanticMeasure
     * @return the SemanticMeasure that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SemanticMeasure replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SemanticMeasure) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SemanticMeasure, without replacing existing terms linked to the SemanticMeasure.
     * Note: this operation must make two API calls — one to retrieve the SemanticMeasure's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SemanticMeasure
     * @param qualifiedName for the SemanticMeasure
     * @param terms the list of terms to append to the SemanticMeasure
     * @return the SemanticMeasure that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SemanticMeasure appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SemanticMeasure) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SemanticMeasure, without replacing all existing terms linked to the SemanticMeasure.
     * Note: this operation must make two API calls — one to retrieve the SemanticMeasure's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SemanticMeasure
     * @param qualifiedName for the SemanticMeasure
     * @param terms the list of terms to remove from the SemanticMeasure, which must be referenced by GUID
     * @return the SemanticMeasure that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SemanticMeasure removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SemanticMeasure) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SemanticMeasure, without replacing existing Atlan tags linked to the SemanticMeasure.
     * Note: this operation must make two API calls — one to retrieve the SemanticMeasure's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SemanticMeasure
     * @param qualifiedName of the SemanticMeasure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SemanticMeasure
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SemanticMeasure appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SemanticMeasure) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SemanticMeasure, without replacing existing Atlan tags linked to the SemanticMeasure.
     * Note: this operation must make two API calls — one to retrieve the SemanticMeasure's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SemanticMeasure
     * @param qualifiedName of the SemanticMeasure
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SemanticMeasure
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SemanticMeasure appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SemanticMeasure) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SemanticMeasure.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SemanticMeasure
     * @param qualifiedName of the SemanticMeasure
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SemanticMeasure
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
