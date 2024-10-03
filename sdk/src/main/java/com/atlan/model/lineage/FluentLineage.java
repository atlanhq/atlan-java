/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.Asset;
import com.atlan.model.enums.AtlanLineageDirection;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.fields.*;
import com.atlan.model.relations.Reference;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Singular;

/**
 * Lineage abstraction mechanism, to simplify the most common lineage requests against Atlan
 * (removing the need to understand the guts of Elastic).
 */
@Builder(builderMethodName = "_internal")
public class FluentLineage {

    public static final LineageFilter ACTIVE = Asset.STATUS.inLineage.eq(AtlanStatus.ACTIVE);

    /**
     * Build a fluent lineage request against the provided Atlan tenant.
     *
     * @param client connectivity to an Atlan tenant
     * @param startingFrom an asset reference that contains at least a GUID, from which to start traversing lineage
     * @return the start of a fluent lineage request against the tenant
     */
    public static FluentLineageBuilder builder(AtlanClient client, Reference startingFrom) {
        return builder(client, startingFrom.getGuid());
    }

    /**
     * Build a fluent lineage request against the provided Atlan tenant.
     *
     * @param client connectivity to an Atlan tenant
     * @param startingGuid unique identifier (GUID) of the asset from which to start lineage
     * @return the start of a fluent lineage request against the tenant
     */
    public static FluentLineageBuilder builder(AtlanClient client, String startingGuid) {
        return _internal().client(client).startingGuid(startingGuid);
    }

    /** Client through which to retrieve the lineage. */
    AtlanClient client;

    /** Unique identifier (GUID) of the asset from which to start lineage. */
    String startingGuid;

    /** Direction of lineage to fetch (upstream or downstream). */
    @Builder.Default
    AtlanLineageDirection direction = AtlanLineageDirection.DOWNSTREAM;

    /** Number of results to retrieve per underlying API request. */
    @Builder.Default
    Integer pageSize = 1000;

    /** Number of degrees of separation (hops) across which lineage should be fetched. */
    Integer depth;

    /** Filters to apply on assets. Any assets excluded by the filters will exclude all assets beyond, as well. */
    @Singular
    List<LineageFilter> whereAssets;

    /** Whether the {@code whereAssets} criteria should be combined (AND) or any matching is sufficient (OR). */
    @Builder.Default
    FilterList.Condition assetsCondition = FilterList.Condition.AND;

    /** Filters to apply on relationships. Any relationships excluded by the filters will exclude all assets and relationships beyond, as well. */
    @Singular
    List<LineageFilter> whereRelationships;

    /** Whether the {@code whereRelationships} criteria should be combined (AND) or any matching is sufficient (OR). */
    @Builder.Default
    FilterList.Condition relationshipsCondition = FilterList.Condition.AND;

    /**
     * Assets to include in the results. Any assets not matching these filters will not be included in the results,
     * but will still be traversed in the lineage so that any assets beyond them are still considered for inclusion
     * in the results.
     */
    @Singular("includeInResults")
    List<LineageFilter> includesInResults;

    /** Whether the {@code includesInResults} criteria should be combined (AND) or any matching is sufficient (OR). */
    @Builder.Default
    FilterList.Condition includesCondition = FilterList.Condition.AND;

    /** Attributes to retrieve for each asset in the lineage results. */
    @Singular("includeOnResults")
    List<AtlanField> includesOnResults;

    /** Attributes to retrieve for each asset in the lineage results (for internal use, unchecked!). */
    @Singular("_includeOnResults")
    List<String> _includesOnResults;

    /** Attributes to retrieve for each asset related to the assets in the results. */
    @Singular("includeOnRelations")
    List<AtlanField> includesOnRelations;

    /** Attributes to retrieve for each asset related to the assets in the results (for internal use, unchecked!). */
    @Singular("_includeOnRelations")
    List<String> _includesOnRelations;

    /**
     * Translate the Atlan fluent lineage request into an Atlan lineage list request builder.
     *
     * @return an Atlan lineage list request builder that encapsulates the fluent lineage request
     */
    protected LineageListRequest.LineageListRequestBuilder<?, ?> _requestBuilder() {
        LineageListRequest.LineageListRequestBuilder<?, ?> request =
                LineageListRequest.builder(startingGuid).direction(direction);
        if (pageSize != null) {
            request.size(pageSize);
        }
        if (depth != null) {
            request.depth(depth);
        }
        if (whereAssets != null) {
            FilterList.FilterListBuilder<?, ?> entities = FilterList.builder().condition(assetsCondition);
            for (LineageFilter asset : whereAssets) {
                String attrName = getInternalAtlanName(asset.getField());
                entities.criterion(EntityFilter.builder()
                        .attributeName(attrName)
                        .operator(asset.getOperator())
                        .attributeValue(asset.getValue())
                        .build());
            }
            request.entityTraversalFilters(entities.build());
        }
        if (whereRelationships != null) {
            FilterList.FilterListBuilder<?, ?> relationships =
                    FilterList.builder().condition(relationshipsCondition);
            for (LineageFilter relationship : whereRelationships) {
                String attrName = getInternalAtlanName(relationship.getField());
                relationships.criterion(EntityFilter.builder()
                        .attributeName(attrName)
                        .operator(relationship.getOperator())
                        .attributeValue(relationship.getValue())
                        .build());
            }
            request.relationshipTraversalFilters(relationships.build());
        }
        if (includesInResults != null) {
            FilterList.FilterListBuilder<?, ?> entities = FilterList.builder().condition(includesCondition);
            for (LineageFilter asset : includesInResults) {
                String attrName = getInternalAtlanName(asset.getField());
                entities.criterion(EntityFilter.builder()
                        .attributeName(attrName)
                        .operator(asset.getOperator())
                        .attributeValue(asset.getValue())
                        .build());
            }
            request.entityFilters(entities.build());
        }
        if (_includesOnResults != null) {
            request.attributes(_includesOnResults);
        }
        if (includesOnResults != null) {
            request.attributes(includesOnResults.stream()
                    .map(AtlanField::getAtlanFieldName)
                    .collect(Collectors.toList()));
        }
        if (_includesOnRelations != null) {
            request.relationAttributes(_includesOnRelations);
        }
        if (includesOnRelations != null) {
            request.relationAttributes(includesOnRelations.stream()
                    .map(AtlanField::getAtlanFieldName)
                    .collect(Collectors.toList()));
        }
        return request;
    }

    private String getInternalAtlanName(SearchableField candidate) {
        String attrName = "";
        if (candidate instanceof IInternalSearchable) {
            attrName = ((IInternalSearchable) candidate).getInternalFieldName();
            // TODO: filtering lineage by custom metadata not currently possible
        } else if (candidate != null && !(candidate instanceof CustomMetadataField)) {
            attrName = candidate.getAtlanFieldName();
        }
        return attrName;
    }

    public static class FluentLineageBuilder {

        /**
         * Translate the Atlan fluent lineage request into an Atlan lineage list request builder.
         *
         * @return an Atlan lineage list request builder that encapsulates the fluent lineage request
         */
        public LineageListRequest.LineageListRequestBuilder<?, ?> toRequestBuilder() {
            return build()._requestBuilder();
        }

        /**
         * Translate the Atlan fluent lineage request into an Atlan lineage list request.
         *
         * @return an Atlan lineage list request that encapsulates the fluent lineage request
         */
        public LineageListRequest toRequest() {
            return toRequestBuilder().build();
        }

        /**
         * Run the fluent lineage request to retrieve assets that match the supplied criteria.
         *
         * @return a stream of assets that match the specified criteria, lazily-fetched
         * @throws AtlanException on any issues interacting with the Atlan APIs
         */
        public Stream<Asset> stream() throws AtlanException {
            if (client == null) {
                throw new InvalidRequestException(ErrorCode.NO_ATLAN_CLIENT);
            }
            LineageListRequest request = toRequest();
            return request.fetch(client).stream();
        }
    }
}
