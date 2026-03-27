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
import com.atlan.model.structs.PopularityInsights;
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
 * A column-level filtering observation from real query traffic.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "serial"})
public class SqlInsightFilter extends Asset
        implements ISqlInsightFilter, ISqlInsight, ICatalog, IAsset, IReferenceable {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SqlInsightFilter";

    /** Fixed typeName for SqlInsightFilters. */
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

    /** Column that this filter insight observes. */
    @Attribute
    IColumn sqlInsightColumn;

    /** Qualified name of the filtered column. */
    @Attribute
    String sqlInsightFilterColumnQualifiedName;

    /** Common values observed for this filter. */
    @Attribute
    @Singular
    SortedSet<String> sqlInsightFilterCommonValues;

    /** Qualified name of the dataset containing the filtered column. */
    @Attribute
    String sqlInsightFilterDatasetQualifiedName;

    /** Example SQL queries that demonstrate this filter pattern, with usage details. */
    @Attribute
    @Singular
    List<PopularityInsights> sqlInsightFilterExampleQueries;

    /** Time (epoch) at which this filter pattern was last observed, in milliseconds. */
    @Attribute
    @Date
    Long sqlInsightFilterLastSeenAt;

    /** SQL operator observed on this column, such as =, !=, IN, LIKE. */
    @Attribute
    String sqlInsightFilterOperator;

    /** SQL predicate expression for this filter pattern. */
    @Attribute
    String sqlInsightFilterPredicateSQL;

    /** Number of queries that use this filter pattern. */
    @Attribute
    Integer sqlInsightFilterQueryCount;

    /** Number of unique users who have used this filter pattern. */
    @Attribute
    Integer sqlInsightFilterUniqueUsers;

    /** Guidance on when this filter pattern should be used. */
    @Attribute
    String sqlInsightFilterWhenToUse;

    /**
     * Builds the minimal object necessary to create a relationship to a SqlInsightFilter, from a potentially
     * more-complete SqlInsightFilter object.
     *
     * @return the minimal object necessary to relate to the SqlInsightFilter
     * @throws InvalidRequestException if any of the minimal set of required properties for a SqlInsightFilter relationship are not found in the initial object
     */
    @Override
    public SqlInsightFilter trimToReference() throws InvalidRequestException {
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
     * Start a fluent search that will return all SqlInsightFilter assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval. Only active (non-archived) SqlInsightFilter assets will be included.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @return a fluent search that includes all SqlInsightFilter assets
     */
    public static FluentSearch.FluentSearchBuilder<?, ?> select(AtlanClient client) {
        return select(client, false);
    }

    /**
     * Start a fluent search that will return all SqlInsightFilter assets.
     * Additional conditions can be chained onto the returned search before any
     * asset retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the assets
     * @param includeArchived when true, archived (soft-deleted) SqlInsightFilters will be included
     * @return a fluent search that includes all SqlInsightFilter assets
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
     * Reference to a SqlInsightFilter by GUID. Use this to create a relationship to this SqlInsightFilter,
     * where the relationship should be replaced.
     *
     * @param guid the GUID of the SqlInsightFilter to reference
     * @return reference to a SqlInsightFilter that can be used for defining a relationship to a SqlInsightFilter
     */
    public static SqlInsightFilter refByGuid(String guid) {
        return refByGuid(guid, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SqlInsightFilter by GUID. Use this to create a relationship to this SqlInsightFilter,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param guid the GUID of the SqlInsightFilter to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SqlInsightFilter that can be used for defining a relationship to a SqlInsightFilter
     */
    public static SqlInsightFilter refByGuid(String guid, Reference.SaveSemantic semantic) {
        return SqlInsightFilter._internal().guid(guid).semantic(semantic).build();
    }

    /**
     * Reference to a SqlInsightFilter by qualifiedName. Use this to create a relationship to this SqlInsightFilter,
     * where the relationship should be replaced.
     *
     * @param qualifiedName the qualifiedName of the SqlInsightFilter to reference
     * @return reference to a SqlInsightFilter that can be used for defining a relationship to a SqlInsightFilter
     */
    public static SqlInsightFilter refByQualifiedName(String qualifiedName) {
        return refByQualifiedName(qualifiedName, Reference.SaveSemantic.REPLACE);
    }

    /**
     * Reference to a SqlInsightFilter by qualifiedName. Use this to create a relationship to this SqlInsightFilter,
     * where you want to further control how that relationship should be updated (i.e. replaced,
     * appended, or removed).
     *
     * @param qualifiedName the qualifiedName of the SqlInsightFilter to reference
     * @param semantic how to save this relationship (replace all with this, append it, or remove it)
     * @return reference to a SqlInsightFilter that can be used for defining a relationship to a SqlInsightFilter
     */
    public static SqlInsightFilter refByQualifiedName(String qualifiedName, Reference.SaveSemantic semantic) {
        return SqlInsightFilter._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .semantic(semantic)
                .build();
    }

    /**
     * Retrieves a SqlInsightFilter by one of its identifiers, complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SqlInsightFilter to retrieve, either its GUID or its full qualifiedName
     * @return the requested full SqlInsightFilter, complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SqlInsightFilter does not exist or the provided GUID is not a SqlInsightFilter
     */
    @JsonIgnore
    public static SqlInsightFilter get(AtlanClient client, String id) throws AtlanException {
        return get(client, id, false);
    }

    /**
     * Retrieves a SqlInsightFilter by one of its identifiers, optionally complete with all of its relationships.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SqlInsightFilter to retrieve, either its GUID or its full qualifiedName
     * @param includeAllRelationships if true, all the asset's relationships will also be retrieved; if false, no relationships will be retrieved
     * @return the requested full SqlInsightFilter, optionally complete with all of its relationships
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SqlInsightFilter does not exist or the provided GUID is not a SqlInsightFilter
     */
    @JsonIgnore
    public static SqlInsightFilter get(AtlanClient client, String id, boolean includeAllRelationships)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Asset asset = Asset.get(client, id, includeAllRelationships);
            if (asset == null) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset instanceof SqlInsightFilter) {
                return (SqlInsightFilter) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Asset asset = Asset.get(client, TYPE_NAME, id, includeAllRelationships);
            if (asset instanceof SqlInsightFilter) {
                return (SqlInsightFilter) asset;
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            }
        }
    }

    /**
     * Retrieves a SqlInsightFilter by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SqlInsightFilter to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SqlInsightFilter, including any relationships
     * @return the requested SqlInsightFilter, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SqlInsightFilter does not exist or the provided GUID is not a SqlInsightFilter
     */
    @JsonIgnore
    public static SqlInsightFilter get(AtlanClient client, String id, Collection<AtlanField> attributes)
            throws AtlanException {
        return get(client, id, attributes, Collections.emptyList());
    }

    /**
     * Retrieves a SqlInsightFilter by one of its identifiers, with only the requested attributes (and relationships).
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the asset
     * @param id of the SqlInsightFilter to retrieve, either its GUID or its full qualifiedName
     * @param attributes to retrieve for the SqlInsightFilter, including any relationships
     * @param attributesOnRelated to retrieve on each relationship retrieved for the SqlInsightFilter
     * @return the requested SqlInsightFilter, with only its minimal information and the requested attributes (and relationships)
     * @throws AtlanException on any error during the API invocation, such as the {@link NotFoundException} if the SqlInsightFilter does not exist or the provided GUID is not a SqlInsightFilter
     */
    @JsonIgnore
    public static SqlInsightFilter get(
            AtlanClient client,
            String id,
            Collection<AtlanField> attributes,
            Collection<AtlanField> attributesOnRelated)
            throws AtlanException {
        if (id == null) {
            throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, "(null)");
        } else if (StringUtils.isUUID(id)) {
            Optional<Asset> asset = SqlInsightFilter.select(client)
                    .where(SqlInsightFilter.GUID.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_GUID, id);
            } else if (asset.get() instanceof SqlInsightFilter) {
                return (SqlInsightFilter) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        } else {
            Optional<Asset> asset = SqlInsightFilter.select(client)
                    .where(SqlInsightFilter.QUALIFIED_NAME.eq(id))
                    .includesOnResults(attributes)
                    .includesOnRelations(attributesOnRelated)
                    .includeRelationshipAttributes(true)
                    .pageSize(1)
                    .stream()
                    .findFirst();
            if (!asset.isPresent()) {
                throw new NotFoundException(ErrorCode.ASSET_NOT_FOUND_BY_QN, id, TYPE_NAME);
            } else if (asset.get() instanceof SqlInsightFilter) {
                return (SqlInsightFilter) asset.get();
            } else {
                throw new NotFoundException(ErrorCode.ASSET_NOT_TYPE_REQUESTED, id, TYPE_NAME);
            }
        }
    }

    /**
     * Restore the archived (soft-deleted) SqlInsightFilter to active.
     *
     * @param client connectivity to the Atlan tenant on which to restore the asset
     * @param qualifiedName for the SqlInsightFilter
     * @return true if the SqlInsightFilter is now active, and false otherwise
     * @throws AtlanException on any API problems
     */
    public static boolean restore(AtlanClient client, String qualifiedName) throws AtlanException {
        return Asset.restore(client, TYPE_NAME, qualifiedName);
    }

    /**
     * Builds the minimal object necessary to update a SqlInsightFilter.
     *
     * @param qualifiedName of the SqlInsightFilter
     * @param name of the SqlInsightFilter
     * @return the minimal request necessary to update the SqlInsightFilter, as a builder
     */
    public static SqlInsightFilterBuilder<?, ?> updater(String qualifiedName, String name) {
        return SqlInsightFilter._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .qualifiedName(qualifiedName)
                .name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to a SqlInsightFilter,
     * from a potentially more-complete SqlInsightFilter object.
     *
     * @return the minimal object necessary to update the SqlInsightFilter, as a builder
     * @throws InvalidRequestException if any of the minimal set of required fields for a SqlInsightFilter are not present in the initial object
     */
    @Override
    public SqlInsightFilterBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        Map<String, String> map = new HashMap<>();
        map.put("qualifiedName", this.getQualifiedName());
        map.put("name", this.getName());
        validateRequired(TYPE_NAME, map);
        return updater(this.getQualifiedName(), this.getName());
    }

    public abstract static class SqlInsightFilterBuilder<
                    C extends SqlInsightFilter, B extends SqlInsightFilterBuilder<C, B>>
            extends Asset.AssetBuilder<C, B> {}

    /**
     * Remove the system description from a SqlInsightFilter.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SqlInsightFilter
     * @param name of the SqlInsightFilter
     * @return the updated SqlInsightFilter, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightFilter removeDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SqlInsightFilter) Asset.removeDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the user's description from a SqlInsightFilter.
     *
     * @param client connectivity to the Atlan tenant on which to remove the asset's description
     * @param qualifiedName of the SqlInsightFilter
     * @param name of the SqlInsightFilter
     * @return the updated SqlInsightFilter, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightFilter removeUserDescription(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SqlInsightFilter) Asset.removeUserDescription(client, updater(qualifiedName, name));
    }

    /**
     * Remove the owners from a SqlInsightFilter.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SqlInsightFilter's owners
     * @param qualifiedName of the SqlInsightFilter
     * @param name of the SqlInsightFilter
     * @return the updated SqlInsightFilter, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightFilter removeOwners(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SqlInsightFilter) Asset.removeOwners(client, updater(qualifiedName, name));
    }

    /**
     * Update the certificate on a SqlInsightFilter.
     *
     * @param client connectivity to the Atlan tenant on which to update the SqlInsightFilter's certificate
     * @param qualifiedName of the SqlInsightFilter
     * @param certificate to use
     * @param message (optional) message, or null if no message
     * @return the updated SqlInsightFilter, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightFilter updateCertificate(
            AtlanClient client, String qualifiedName, CertificateStatus certificate, String message)
            throws AtlanException {
        return (SqlInsightFilter)
                Asset.updateCertificate(client, _internal(), TYPE_NAME, qualifiedName, certificate, message);
    }

    /**
     * Remove the certificate from a SqlInsightFilter.
     *
     * @param client connectivity to the Atlan tenant from which to remove the SqlInsightFilter's certificate
     * @param qualifiedName of the SqlInsightFilter
     * @param name of the SqlInsightFilter
     * @return the updated SqlInsightFilter, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightFilter removeCertificate(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SqlInsightFilter) Asset.removeCertificate(client, updater(qualifiedName, name));
    }

    /**
     * Update the announcement on a SqlInsightFilter.
     *
     * @param client connectivity to the Atlan tenant on which to update the SqlInsightFilter's announcement
     * @param qualifiedName of the SqlInsightFilter
     * @param type type of announcement to set
     * @param title (optional) title of the announcement to set (or null for no title)
     * @param message (optional) message of the announcement to set (or null for no message)
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightFilter updateAnnouncement(
            AtlanClient client, String qualifiedName, AtlanAnnouncementType type, String title, String message)
            throws AtlanException {
        return (SqlInsightFilter)
                Asset.updateAnnouncement(client, _internal(), TYPE_NAME, qualifiedName, type, title, message);
    }

    /**
     * Remove the announcement from a SqlInsightFilter.
     *
     * @param client connectivity to the Atlan client from which to remove the SqlInsightFilter's announcement
     * @param qualifiedName of the SqlInsightFilter
     * @param name of the SqlInsightFilter
     * @return the updated SqlInsightFilter, or null if the removal failed
     * @throws AtlanException on any API problems
     */
    public static SqlInsightFilter removeAnnouncement(AtlanClient client, String qualifiedName, String name)
            throws AtlanException {
        return (SqlInsightFilter) Asset.removeAnnouncement(client, updater(qualifiedName, name));
    }

    /**
     * Replace the terms linked to the SqlInsightFilter.
     *
     * @param client connectivity to the Atlan tenant on which to replace the SqlInsightFilter's assigned terms
     * @param qualifiedName for the SqlInsightFilter
     * @param name human-readable name of the SqlInsightFilter
     * @param terms the list of terms to replace on the SqlInsightFilter, or null to remove all terms from the SqlInsightFilter
     * @return the SqlInsightFilter that was updated (note that it will NOT contain details of the replaced terms)
     * @throws AtlanException on any API problems
     */
    public static SqlInsightFilter replaceTerms(
            AtlanClient client, String qualifiedName, String name, List<IGlossaryTerm> terms) throws AtlanException {
        return (SqlInsightFilter) Asset.replaceTerms(client, updater(qualifiedName, name), terms);
    }

    /**
     * Link additional terms to the SqlInsightFilter, without replacing existing terms linked to the SqlInsightFilter.
     * Note: this operation must make two API calls — one to retrieve the SqlInsightFilter's existing terms,
     * and a second to append the new terms.
     *
     * @param client connectivity to the Atlan tenant on which to append terms to the SqlInsightFilter
     * @param qualifiedName for the SqlInsightFilter
     * @param terms the list of terms to append to the SqlInsightFilter
     * @return the SqlInsightFilter that was updated  (note that it will NOT contain details of the appended terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SqlInsightFilter appendTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SqlInsightFilter) Asset.appendTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Remove terms from a SqlInsightFilter, without replacing all existing terms linked to the SqlInsightFilter.
     * Note: this operation must make two API calls — one to retrieve the SqlInsightFilter's existing terms,
     * and a second to remove the provided terms.
     *
     * @param client connectivity to the Atlan tenant from which to remove terms from the SqlInsightFilter
     * @param qualifiedName for the SqlInsightFilter
     * @param terms the list of terms to remove from the SqlInsightFilter, which must be referenced by GUID
     * @return the SqlInsightFilter that was updated (note that it will NOT contain details of the resulting terms)
     * @throws AtlanException on any API problems
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAssignedTerm(GlossaryTerm)}
     */
    @Deprecated
    public static SqlInsightFilter removeTerms(AtlanClient client, String qualifiedName, List<IGlossaryTerm> terms)
            throws AtlanException {
        return (SqlInsightFilter) Asset.removeTerms(client, TYPE_NAME, qualifiedName, terms);
    }

    /**
     * Add Atlan tags to a SqlInsightFilter, without replacing existing Atlan tags linked to the SqlInsightFilter.
     * Note: this operation must make two API calls — one to retrieve the SqlInsightFilter's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SqlInsightFilter
     * @param qualifiedName of the SqlInsightFilter
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @throws AtlanException on any API problems
     * @return the updated SqlInsightFilter
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List)}
     */
    @Deprecated
    public static SqlInsightFilter appendAtlanTags(AtlanClient client, String qualifiedName, List<String> atlanTagNames)
            throws AtlanException {
        return (SqlInsightFilter) Asset.appendAtlanTags(client, TYPE_NAME, qualifiedName, atlanTagNames);
    }

    /**
     * Add Atlan tags to a SqlInsightFilter, without replacing existing Atlan tags linked to the SqlInsightFilter.
     * Note: this operation must make two API calls — one to retrieve the SqlInsightFilter's existing Atlan tags,
     * and a second to append the new Atlan tags.
     *
     * @param client connectivity to the Atlan tenant on which to append Atlan tags to the SqlInsightFilter
     * @param qualifiedName of the SqlInsightFilter
     * @param atlanTagNames human-readable names of the Atlan tags to add
     * @param propagate whether to propagate the Atlan tag (true) or not (false)
     * @param removePropagationsOnDelete whether to remove the propagated Atlan tags when the Atlan tag is removed from this asset (true) or not (false)
     * @param restrictLineagePropagation whether to avoid propagating through lineage (true) or do propagate through lineage (false)
     * @throws AtlanException on any API problems
     * @return the updated SqlInsightFilter
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#appendAtlanTags(List, boolean, boolean, boolean, boolean)}
     */
    @Deprecated
    public static SqlInsightFilter appendAtlanTags(
            AtlanClient client,
            String qualifiedName,
            List<String> atlanTagNames,
            boolean propagate,
            boolean removePropagationsOnDelete,
            boolean restrictLineagePropagation)
            throws AtlanException {
        return (SqlInsightFilter) Asset.appendAtlanTags(
                client,
                TYPE_NAME,
                qualifiedName,
                atlanTagNames,
                propagate,
                removePropagationsOnDelete,
                restrictLineagePropagation);
    }

    /**
     * Remove an Atlan tag from a SqlInsightFilter.
     *
     * @param client connectivity to the Atlan tenant from which to remove an Atlan tag from a SqlInsightFilter
     * @param qualifiedName of the SqlInsightFilter
     * @param atlanTagName human-readable name of the Atlan tag to remove
     * @throws AtlanException on any API problems, or if the Atlan tag does not exist on the SqlInsightFilter
     * @deprecated see {@link com.atlan.model.assets.Asset.AssetBuilder#removeAtlanTag(String)}
     */
    @Deprecated
    public static void removeAtlanTag(AtlanClient client, String qualifiedName, String atlanTagName)
            throws AtlanException {
        Asset.removeAtlanTag(client, TYPE_NAME, qualifiedName, atlanTagName);
    }
}
