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
 * Instances of an Module in Atlan.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class CassandraView extends Asset
        implements ICassandraView, ICassandra, INoSQL, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "CassandraView";

    /** Fixed typeName for CassandraViews. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Individual columns contained in the view. */
    @Attribute
    @Singular
    SortedSet<ICassandraColumn> cassandraColumns;

    /** Keyspace containing the view. */
    @Attribute
    ICassandraKeyspace cassandraKeyspace;

    /** Name of the keyspace for the Cassandra asset. */
    @Attribute
    String cassandraKeyspaceName;

    /** Name of the table for the Cassandra asset. */
    @Attribute
    String cassandraTableName;

    /** Unique name of table for Cassandra asset */
    @Attribute
    String cassandraTableQualifiedName;

    /** False positive chance for the Bloom filter in the CassandraView. */
    @Attribute
    Double cassandraViewBloomFilterFPChance;

    /** CRC check chance for the CassandraView. */
    @Attribute
    Double cassandraViewCRCCheckChance;

    /** Caching configuration in the CassandraView. */
    @Attribute
    @Singular("putCassandraViewCaching")
    Map<String, String> cassandraViewCaching;

    /** Comment describing the CassandraView. */
    @Attribute
    String cassandraViewComment;

    /** Compaction for the CassandraView. */
    @Attribute
    @Singular("putCassandraViewCompaction")
    Map<String, String> cassandraViewCompaction;

    /** DC-local read repair chance for the CassandraView. */
    @Attribute
    Double cassandraViewDCLocalReadRepairChance;

    /** Default time-to-live (TTL) for the CassandraView. */
    @Attribute
    Long cassandraViewDefaultTTL;

    /** Grace period for garbage collection in the CassandraView. */
    @Attribute
    Long cassandraViewGCGraceSeconds;

    /** Whether to include all columns in the CassandraView. */
    @Attribute
    Boolean cassandraViewIncludeAllColumns;

    /** Maximum index interval for the CassandraView. */
    @Attribute
    Long cassandraViewMaxIndexInterval;

    /** Memtable flush period (in milliseconds) for the CassandraView. */
    @Attribute
    Long cassandraViewMembtableFlushPeriodInMS;

    /** Minimum index interval for the CassandraView. */
    @Attribute
    Long cassandraViewMinIndexInterval;

    /** Name of view for Cassandra asset */
    @Attribute
    String cassandraViewName;

    /** Unique name of view for Cassandra asset */
    @Attribute
    String cassandraViewQualifiedName;

    /** Query used in the CassandraView. */
    @Attribute
    String cassandraViewQuery;

    /** Read repair interval for the CassandraView. */
    @Attribute
    Long cassandraViewReadRepairInterval;

    /** SpeculativeRetry setting for the CassandraView. */
    @Attribute
    String cassandraViewSpeculativeRetry;

    /** ID of the base table in the CassandraView. */
    @Attribute
    String cassandraViewTableId;

    /** Where clause used for the CassandraView query. */
    @Attribute
    String cassandraViewWhereClause;

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

    /** Represents attributes for describing the key schema for the table and indexes. */
    @Attribute
    String noSQLSchemaDefinition;

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
     * Builds the minimal object necessary to create a relationship to a CassandraView, from a potentially
     * more-complete CassandraView object.
     *
     * @return the minimal object necessary to relate to the CassandraView
     * @throws InvalidRequestException if any of the minimal set of required properties for a CassandraView relationship are not found in the initial object
     */
    @Override
    public CassandraView trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all CassandraView assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) CassandraView assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all CassandraView assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all CassandraView assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) CassandraViews will be included
     * @return a fluent search that includes all CassandraView assets
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
     * Reference to a CassandraView by GUID. Use this to create a relationship to this CassandraView,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the CassandraView to reference
     * @return reference to a CassandraView that can be used for defining a relationship to a CassandraView
     */
    public static CassandraView refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CassandraView by GUID. Use this to create a relationship to this CassandraView,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the CassandraView to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CassandraView that can be used for defining a relationship to a CassandraView
     */
    public static CassandraView refByGuid(String guid, Reference.SaveSemantic semantic) {
        return CassandraView._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a CassandraView by qualifiedName. Use this to create a relationship to this CassandraView,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the CassandraView to reference
     * @return reference to a CassandraView that can be used for defining a relationship to a CassandraView
     */
    public static CassandraView refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a CassandraView by qualifiedName. Use this to create a relationship to this CassandraView,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the CassandraView to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a CassandraView that can be used for defining a relationship to a CassandraView
     */
    public static CassandraView refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return CassandraView._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a CassandraView by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CassandraView to retrieve, either its GUID or its full qualifiedName
     * @return the requested full CassandraView, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CassandraView does not exist or the provided GUID is not a CassandraView
     */
    @JsonIgnore
    public static CassandraView get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a CassandraView by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CassandraView to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full CassandraView, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CassandraView does not exist or the provided GUID is not a CassandraView
     */
    @JsonIgnore
    public static CassandraView get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof CassandraView) {
                return (CassandraView) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof CassandraView) {
                return (CassandraView) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a CassandraView by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CassandraView to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the CassandraView, including any relationships
     * @return the requested CassandraView, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CassandraView does not exist or the provided GUID is not a CassandraView
     */
    @JsonIgnore
    public static CassandraView get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a CassandraView by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the CassandraView to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the CassandraView, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the CassandraView
     * @return the requested CassandraView, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the CassandraView does not exist or the provided GUID is not a CassandraView
     */
    @JsonIgnore
    public static CassandraView get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = CassandraView.select(client)
                    .where(CassandraView.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof CassandraView) {
                return (CassandraView) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = CassandraView.select(client)
                    .where(CassandraView.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof CassandraView) {
                return (CassandraView) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) CassandraView to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the CassandraView
     * @return true if the CassandraView is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a CassandraView.
     *
     * @param qualifiedName of the CassandraView
     * @param name of the CassandraView
     * @return the minimal request necessary to update the CassandraView, as a builder
     */
    public static CassandraViewBuilder<?, ?> updater(String qualifiedName, String name) {
        return CassandraView._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a CassandraView, from a potentially
     * more-complete CassandraView object.
     *
     * @return the minimal object necessary to update the CassandraView, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties for CassandraView are not found in the initial object
     */
    @Override
    public CassandraViewBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class CassandraViewBuilder<C extends CassandraView, B extends CassandraViewBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a CassandraView.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CassandraView
     * @param name of the CassandraView
     * @return the updated CassandraView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CassandraView removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CassandraView) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a CassandraView.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the CassandraView
     * @param name of the CassandraView
     * @return the updated CassandraView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CassandraView removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CassandraView) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a CassandraView.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CassandraView's owners
     * @param qualifiedName of the CassandraView
     * @param name of the CassandraView
     * @return the updated CassandraView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CassandraView removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CassandraView) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a CassandraView.
     *
     * @param client connectivity to the Atlan tenant on which to update the CassandraView's certificate
     * @param qualifiedName of the CassandraView
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated CassandraView, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CassandraView updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (CassandraView)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a CassandraView.
     *
     * @param client connectivity to the Atlan tenant from which to remove the CassandraView's certificate
     * @param qualifiedName of the CassandraView
     * @param name of the CassandraView
     * @return the updated CassandraView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CassandraView removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CassandraView) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a CassandraView.
     *
     * @param client connectivity to the Atlan tenant on which to update the CassandraView's announcement
     * @param qualifiedName of the CassandraView
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static CassandraView updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (CassandraView)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a CassandraView.
     *
     * @param client connectivity to the Atlan client from which to remove the CassandraView's announcement
     * @param qualifiedName of the CassandraView
     * @param name of the CassandraView
     * @return the updated CassandraView, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static CassandraView removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (CassandraView) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the CassandraView.
     *
     * @param client connectivity to the Atlan tenant on which to replace the CassandraView's assigned terms
     * @param qualifiedName for the CassandraView
     * @param name human-readable name of the CassandraView
     * @param terms the list of terms to replace on the CassandraView, or null to remove all terms from the CassandraView
     * @return the CassandraView that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static CassandraView replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (CassandraView) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the CassandraView, without replacing existing terms linked to the CassandraView.
     * Note: this operation must make two API calls — one to retrieve the CassandraView's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the CassandraView
     * @param qualifiedName for the CassandraView
     * @param terms the list of terms to append to the CassandraView
     * @return the CassandraView that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static CassandraView appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CassandraView) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a CassandraView, without replacing all existing terms linked to the CassandraView.
     * Note: this operation must make two API calls — one to retrieve the CassandraView's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the CassandraView
     * @param qualifiedName for the CassandraView
     * @param terms the list of terms to remove from the CassandraView, which must be referenced by GUID
     * @return the CassandraView that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static CassandraView removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (CassandraView) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a CassandraView, without replacing existing Atlan tags linked to the CassandraView.
     * Note: this operation must make two API calls — one to retrieve the CassandraView's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CassandraView
     * @param qualifiedName of the CassandraView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated CassandraView
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static CassandraView appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (CassandraView) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a CassandraView, without replacing existing Atlan tags linked to the CassandraView.
     * Note: this operation must make two API calls — one to retrieve the CassandraView's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the CassandraView
     * @param qualifiedName of the CassandraView
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated CassandraView
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static CassandraView appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (CassandraView) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a CassandraView.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a CassandraView
     * @param qualifiedName of the CassandraView
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the CassandraView
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
