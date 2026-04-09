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
import com.atlan.model.enums.SqlInsightJoinCardinality;
import com.atlan.model.enums.SqlInsightJoinType;
import com.atlan.model.fields.AtlanField;
import com.atlan.model.relations.Reference;
import com.atlan.model.relations.UniqueAttributes;
import com.atlan.model.search.FluentSearch;
import com.atlan.model.structs.PopularityInsights;
import com.atlan.model.structs.SqlInsightJoinColumnPair;
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
 * A directed join pattern observed between two SQL datasets from real query traffic.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SqlInsightJoin extends Asset implements ISqlInsightJoin, ISqlInsight, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SqlInsightJoin";

    /** Fixed typeName for SqlInsightJoins. */
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

    /** Observed cardinality of the join relationship. */
    @Attribute
    SqlInsightJoinCardinality sqlInsightJoinCardinality;

    /** Column mappings in this join, pairing source columns to joined columns. */
    @Attribute
    @Singular
    List<SqlInsightJoinColumnPair> sqlInsightJoinColumnPairs;

    /** Example SQL queries that demonstrate this join pattern, with usage details. */
    @Attribute
    @Singular
    List<PopularityInsights> sqlInsightJoinExampleQueries;

    /** Qualified name of the joined dataset in this join pattern. */
    @Attribute
    String sqlInsightJoinJoinedDatasetQualifiedName;

    /** Time (epoch) at which this join pattern was last observed, in milliseconds. */
    @Attribute
    @Date
    Long sqlInsightJoinLastSeenAt;

    /** Number of queries that use this join pattern. */
    @Attribute
    Integer sqlInsightJoinQueryCount;

    /** Qualified name of the source dataset in this join pattern. */
    @Attribute
    String sqlInsightJoinSourceDatasetQualifiedName;

    /** Type of SQL join observed in this pattern. */
    @Attribute
    SqlInsightJoinType sqlInsightJoinType;

    /** Number of unique users who have used this join pattern. */
    @Attribute
    Integer sqlInsightJoinUniqueUsers;

    /** Guidance on when this join pattern should be used. */
    @Attribute
    String sqlInsightJoinWhenToUse;

    /** SQL dataset that is the joined side in this join insight. */
    @Attribute
    ISQL sqlInsightJoinedDataset;

    /** SQL dataset that is the source in this join insight. */
    @Attribute
    ISQL sqlInsightSourceDataset;

    /**
     * Builds the minimal object necessary to create a relationship to a SqlInsightJoin, from a potentially
     * more-complete SqlInsightJoin object.
     *
     * @return the minimal object necessary to relate to the SqlInsightJoin
     * @throws InvalidRequestException if any of the minimal set of required properties for a SqlInsightJoin relationship are not found in the initial object
     */
    @Override
    public SqlInsightJoin trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SqlInsightJoin assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SqlInsightJoin assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SqlInsightJoin assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SqlInsightJoin assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SqlInsightJoins will be included
     * @return a fluent search that includes all SqlInsightJoin assets
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
     * Reference to a SqlInsightJoin by GUID. Use this to create a relationship to this SqlInsightJoin,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SqlInsightJoin to reference
     * @return reference to a SqlInsightJoin that can be used for defining a relationship to a SqlInsightJoin
     */
    public static SqlInsightJoin refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SqlInsightJoin by GUID. Use this to create a relationship to this SqlInsightJoin,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SqlInsightJoin to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SqlInsightJoin that can be used for defining a relationship to a SqlInsightJoin
     */
    public static SqlInsightJoin refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SqlInsightJoin._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SqlInsightJoin by qualifiedName. Use this to create a relationship to this SqlInsightJoin,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SqlInsightJoin to reference
     * @return reference to a SqlInsightJoin that can be used for defining a relationship to a SqlInsightJoin
     */
    public static SqlInsightJoin refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SqlInsightJoin by qualifiedName. Use this to create a relationship to this SqlInsightJoin,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SqlInsightJoin to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SqlInsightJoin that can be used for defining a relationship to a SqlInsightJoin
     */
    public static SqlInsightJoin refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SqlInsightJoin._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SqlInsightJoin by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SqlInsightJoin to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SqlInsightJoin, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SqlInsightJoin does not exist or the provided GUID is not a SqlInsightJoin
     */
    @JsonIgnore
    public static SqlInsightJoin get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SqlInsightJoin by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SqlInsightJoin to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SqlInsightJoin, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SqlInsightJoin does not exist or the provided GUID is not a SqlInsightJoin
     */
    @JsonIgnore
    public static SqlInsightJoin get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SqlInsightJoin) {
                return (SqlInsightJoin) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SqlInsightJoin) {
                return (SqlInsightJoin) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SqlInsightJoin by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SqlInsightJoin to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SqlInsightJoin, including any relationships
     * @return the requested SqlInsightJoin, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SqlInsightJoin does not exist or the provided GUID is not a SqlInsightJoin
     */
    @JsonIgnore
    public static SqlInsightJoin get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SqlInsightJoin by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SqlInsightJoin to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SqlInsightJoin, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SqlInsightJoin
     * @return the requested SqlInsightJoin, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SqlInsightJoin does not exist or the provided GUID is not a SqlInsightJoin
     */
    @JsonIgnore
    public static SqlInsightJoin get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SqlInsightJoin.select(client)
                    .where(SqlInsightJoin.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SqlInsightJoin) {
                return (SqlInsightJoin) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SqlInsightJoin.select(client)
                    .where(SqlInsightJoin.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SqlInsightJoin) {
                return (SqlInsightJoin) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SqlInsightJoin to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SqlInsightJoin
     * @return true if the SqlInsightJoin is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SqlInsightJoin.
     *
     * @param qualifiedName of the SqlInsightJoin
     * @param name of the SqlInsightJoin
     * @return the minimal request necessary to update the SqlInsightJoin, as a builder
     */
    public static SqlInsightJoinBuilder<?, ?> updater(String qualifiedName, String name) {
        return SqlInsightJoin._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SqlInsightJoin,
     * from a potentially more-complete SqlInsightJoin object.
     *
     * @return the minimal object necessary to update the SqlInsightJoin, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SqlInsightJoin are not present in the initial object
     */
    @Override
    public SqlInsightJoinBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SqlInsightJoinBuilder<C extends SqlInsightJoin, B extends SqlInsightJoinBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SqlInsightJoin.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SqlInsightJoin
     * @param name of the SqlInsightJoin
     * @return the updated SqlInsightJoin, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightJoin removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SqlInsightJoin) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SqlInsightJoin.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SqlInsightJoin
     * @param name of the SqlInsightJoin
     * @return the updated SqlInsightJoin, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightJoin removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SqlInsightJoin) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SqlInsightJoin.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SqlInsightJoin's owners
     * @param qualifiedName of the SqlInsightJoin
     * @param name of the SqlInsightJoin
     * @return the updated SqlInsightJoin, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightJoin removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SqlInsightJoin) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SqlInsightJoin.
     *
     * @param client connectivity to the Atlan tenant on which to update the SqlInsightJoin's certificate
     * @param qualifiedName of the SqlInsightJoin
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SqlInsightJoin, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightJoin updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SqlInsightJoin)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SqlInsightJoin.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SqlInsightJoin's certificate
     * @param qualifiedName of the SqlInsightJoin
     * @param name of the SqlInsightJoin
     * @return the updated SqlInsightJoin, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightJoin removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SqlInsightJoin) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SqlInsightJoin.
     *
     * @param client connectivity to the Atlan tenant on which to update the SqlInsightJoin's announcement
     * @param qualifiedName of the SqlInsightJoin
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightJoin updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SqlInsightJoin)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SqlInsightJoin.
     *
     * @param client connectivity to the Atlan client from which to remove the SqlInsightJoin's announcement
     * @param qualifiedName of the SqlInsightJoin
     * @param name of the SqlInsightJoin
     * @return the updated SqlInsightJoin, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightJoin removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SqlInsightJoin) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SqlInsightJoin.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SqlInsightJoin's assigned terms
     * @param qualifiedName for the SqlInsightJoin
     * @param name human-readable name of the SqlInsightJoin
     * @param terms the list of terms to replace on the SqlInsightJoin, or null to remove all terms from the SqlInsightJoin
     * @return the SqlInsightJoin that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SqlInsightJoin replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SqlInsightJoin) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SqlInsightJoin, without replacing existing terms linked to the SqlInsightJoin.
     * Note: this operation must make two API calls — one to retrieve the SqlInsightJoin's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SqlInsightJoin
     * @param qualifiedName for the SqlInsightJoin
     * @param terms the list of terms to append to the SqlInsightJoin
     * @return the SqlInsightJoin that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SqlInsightJoin appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SqlInsightJoin) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SqlInsightJoin, without replacing all existing terms linked to the SqlInsightJoin.
     * Note: this operation must make two API calls — one to retrieve the SqlInsightJoin's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SqlInsightJoin
     * @param qualifiedName for the SqlInsightJoin
     * @param terms the list of terms to remove from the SqlInsightJoin, which must be referenced by GUID
     * @return the SqlInsightJoin that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SqlInsightJoin removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SqlInsightJoin) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SqlInsightJoin, without replacing existing Atlan tags linked to the SqlInsightJoin.
     * Note: this operation must make two API calls — one to retrieve the SqlInsightJoin's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SqlInsightJoin
     * @param qualifiedName of the SqlInsightJoin
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SqlInsightJoin
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SqlInsightJoin appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SqlInsightJoin) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SqlInsightJoin, without replacing existing Atlan tags linked to the SqlInsightJoin.
     * Note: this operation must make two API calls — one to retrieve the SqlInsightJoin's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SqlInsightJoin
     * @param qualifiedName of the SqlInsightJoin
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SqlInsightJoin
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SqlInsightJoin appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SqlInsightJoin) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SqlInsightJoin.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SqlInsightJoin
     * @param qualifiedName of the SqlInsightJoin
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SqlInsightJoin
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
