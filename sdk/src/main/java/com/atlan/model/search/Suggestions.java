/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.Asset;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.fields.AtlanField;
import com.atlan.serde.Serde;
import com.atlan.util.ParallelBatch;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

/**
 * Suggestion abstraction mechanism, to simplify finding suggestions for metadata enrichment
 * for a given asset. This works purely by looking at other assets with the same name (and type)
 * that have this metadata populated, and rank-ordering any such metadata by how frequently it
 * occurs across other assets of the same type with the same name.
 */
@Builder(builderMethodName = "_internal")
public class Suggestions {

    private static final String AGG_DESCRIPTION = "group_by_description";
    private static final String AGG_USER_DESCRIPTION = "group_by_userDescription";
    private static final String AGG_OWNER_USERS = "group_by_ownerUsers";
    private static final String AGG_OWNER_GROUPS = "group_by_ownerGroups";
    private static final String AGG_ATLAN_TAGS = "group_by_tags";
    private static final String AGG_TERMS = "group_by_terms";

    public enum TYPE {
        /** System-level description suggestions. */
        SystemDescription,
        /** User-provided description suggestions. */
        UserDescription,
        /** Suggestions for individual users who could be owners. */
        IndividualOwners,
        /** Suggestions for groups who could be owners. */
        GroupOwners,
        /** Suggestions for Atlan tags to assign to the asset. */
        Tags,
        /** Suggestions for terms to assign to the asset. */
        Terms,
    }

    /**
     * Build a suggestion finder against the provided Atlan tenant for the provided asset.
     *
     * @param client connectivity to an Atlan tenant
     * @param asset for which to find suggestions
     * @return the start of a suggestion finder for the provided asset, against the specified tenant
     */
    public static SuggestionsBuilder finder(AtlanClient client, Asset asset) {
        return _internal().client(client).asset(asset).includeArchived(false).maxSuggestions(5);
    }

    /** Client through which to find suggestions. */
    AtlanClient client;

    /** Asset for which to find suggestions. */
    Asset asset;

    /** Whether to include archived assets as part of suggestions (true) or not (false, default). */
    Boolean includeArchived;

    /** Which type(s) of suggestions to include in the search and results. */
    @Singular
    Collection<TYPE> includes;

    /** Maximum number of suggestions to return (default: 5). */
    Integer maxSuggestions;

    /**
     * By default, we will only look for suggestions on other assets with exactly the same
     * type. You may want to expand this, for example, for suggested metadata for tables you might also want to look
     * at Views. You can add any additional types here you want to consider where an asset with the same name as
     * this asset is likely have similar metadata (and thus be valid for providing suggestions).
     */
    @Singular
    Collection<String> withOtherTypes;

    /**
     * By default, we will only match on the name (exactly) of the provided asset.
     * You may want to expand this, for example, to look for assets with the same name as well as with
     * some other context, for example, looking only at columns with the same name that are also in parent
     * tables that have the same name. (Columns like 'ID' may otherwise be insufficiently unique to have
     * very useful suggestions.)
     */
    @Singular
    Collection<Query> wheres;

    /**
     * By default, we will only match on the name (exactly) of the provided asset.
     * You may want to expand this, for example, to look for assets with the same name as well as without
     * some other context, for example, looking only at columns with the same name that are not in a particular
     * schema (e.g. one used purely for testing purposes).
     */
    @Singular
    Collection<Query> whereNots;

    public static class SuggestionsBuilder {

        /**
         * Find the requested suggestions and return the results
         * (but do not make any changes to the asset itself).
         *
         * @throws AtlanException on any issue interacting with the APIs
         */
        public SuggestionResponse get() throws AtlanException {
            List<String> allTypes = new ArrayList<>();
            allTypes.add(asset.getTypeName());
            if (withOtherTypes != null && !withOtherTypes.isEmpty()) {
                allTypes.addAll(withOtherTypes);
            }
            FluentSearch.FluentSearchBuilder<?, ?> builder = client.assets
                    .select(includeArchived)
                    .where(Asset.TYPE_NAME.in(allTypes))
                    .where(Asset.NAME.eq(asset.getName()))
                    .pageSize(0) // We only care about the aggregations, not results
                    .minSomes(1);
            if (wheres != null && !wheres.isEmpty()) {
                for (Query condition : wheres) {
                    builder.where(condition);
                }
            }
            if (whereNots != null && !whereNots.isEmpty()) {
                for (Query condition : whereNots) {
                    builder.whereNot(condition);
                }
            }
            for (TYPE include : includes) {
                switch (include) {
                    case SystemDescription:
                        builder.whereSome(Asset.DESCRIPTION.hasAnyValue())
                                .aggregate(AGG_DESCRIPTION, Asset.DESCRIPTION.bucketBy(maxSuggestions, true));
                        break;
                    case UserDescription:
                        builder.whereSome(Asset.USER_DESCRIPTION.hasAnyValue())
                                .aggregate(AGG_USER_DESCRIPTION, Asset.USER_DESCRIPTION.bucketBy(maxSuggestions, true));
                        break;
                    case IndividualOwners:
                        builder.whereSome(Asset.OWNER_USERS.hasAnyValue())
                                .aggregate(AGG_OWNER_USERS, Asset.OWNER_USERS.bucketBy(maxSuggestions));
                        break;
                    case GroupOwners:
                        builder.whereSome(Asset.OWNER_GROUPS.hasAnyValue())
                                .aggregate(AGG_OWNER_GROUPS, Asset.OWNER_GROUPS.bucketBy(maxSuggestions));
                        break;
                    case Tags:
                        builder.whereSome(Asset.ATLAN_TAGS.hasAnyValue())
                                .aggregate(AGG_ATLAN_TAGS, Asset.ATLAN_TAGS.bucketBy(maxSuggestions));
                        break;
                    case Terms:
                        builder.whereSome(Asset.ASSIGNED_TERMS.hasAnyValue())
                                .aggregate(AGG_TERMS, Asset.ASSIGNED_TERMS.bucketBy(maxSuggestions));
                        break;
                    default:
                        // Do nothing -- unknown type
                }
            }
            IndexSearchRequest request = builder.toRequest();
            IndexSearchResponse response = request.search(client);
            Map<String, AggregationResult> aggregations = response.getAggregations();
            SuggestionResponse.SuggestionResponseBuilder responseBuilder = SuggestionResponse.builder();
            for (TYPE include : includes) {
                switch (include) {
                    case SystemDescription:
                        responseBuilder.systemDescriptions(
                                getDescriptions(aggregations.get(AGG_DESCRIPTION), Asset.DESCRIPTION));
                        break;
                    case UserDescription:
                        responseBuilder.userDescriptions(
                                getDescriptions(aggregations.get(AGG_USER_DESCRIPTION), Asset.USER_DESCRIPTION));
                        break;
                    case IndividualOwners:
                        responseBuilder.ownerUsers(getOthers(aggregations.get(AGG_OWNER_USERS)));
                        break;
                    case GroupOwners:
                        responseBuilder.ownerGroups(getOthers(aggregations.get(AGG_OWNER_GROUPS)));
                        break;
                    case Tags:
                        responseBuilder.atlanTags(getTags(client, aggregations.get(AGG_ATLAN_TAGS)));
                        break;
                    case Terms:
                        responseBuilder.assignedTerms(getTerms(aggregations.get(AGG_TERMS)));
                        break;
                }
            }
            return responseBuilder.build();
        }

        /**
         * Find the requested suggestions and apply the top suggestions as
         * changes to the asset.
         * Note: this will NOT validate whether there is any existing value for what
         * you are setting, so will clobber any existing value with the suggestion.
         * If you want to be certain you are only updating empty values, you should ensure
         * you are only building a finder for suggestions for values that do not already
         * exist on the asset in question.
         *
         * @throws AtlanException on any issue interacting with the APIs
         */
        public AssetMutationResponse apply() throws AtlanException {
            return apply(false);
        }

        /**
         * Find the requested suggestions and apply the top suggestions as
         * changes to the asset.
         * Note: this will NOT validate whether there is any existing value for what
         * you are setting, so will clobber any existing value with the suggestion.
         * If you want to be certain you are only updating empty values, you should ensure
         * you are only building a finder for suggestions for values that do not already
         * exist on the asset in question.
         *
         * @param allowMultiple if true, allow multiple suggestions to be applied to the asset (up to maxSuggestions requested), i.e. for owners, terms and tags
         * @throws AtlanException on any issue interacting with the APIs
         */
        public AssetMutationResponse apply(boolean allowMultiple) throws AtlanException {
            Apply result = _apply(allowMultiple);
            return result.getAsset().save(client);
        }

        /**
         * Find the requested suggestions and apply the top suggestions as
         * changes to the asset within the provided batch.
         * Note: this will NOT validate whether there is any existing value for what
         * you are setting, so will clobber any existing value with the suggestion. Also,
         * to ensure tags are applied you MUST set your provided batch up to replace tags
         * BEFORE using it here.
         * If you want to be certain you are only updating empty values, you should ensure
         * you are only building a finder for suggestions for values that do not already
         * exist on the asset in question.
         *
         * @throws AtlanException on any issue interacting with the APIs
         */
        public AssetMutationResponse apply(ParallelBatch batch) throws AtlanException {
            return apply(batch, false);
        }

        /**
         * Find the requested suggestions and apply the top suggestions as
         * changes to the asset within the provided batch.
         * Note: this will NOT validate whether there is any existing value for what
         * you are setting, so will clobber any existing value with the suggestion.
         * If you want to be certain you are only updating empty values, you should ensure
         * you are only building a finder for suggestions for values that do not already
         * exist on the asset in question.
         *
         * @param allowMultiple if true, allow multiple suggestions to be applied to the asset (up to maxSuggestions requested), i.e. for owners, terms and tags
         * @throws AtlanException on any issue interacting with the APIs
         */
        public AssetMutationResponse apply(ParallelBatch batch, boolean allowMultiple) throws AtlanException {
            return batch.add(_apply(allowMultiple).getAsset());
        }

        private Apply _apply(boolean allowMultiple) throws AtlanException {
            SuggestionResponse response = get();
            Asset.AssetBuilder<?, ?> builder = asset.trimToRequired();
            String descriptionToApply = getDescriptionToApply(response);
            // Note: only ever set the description over a user-provided description (never the system-sourced
            // description)
            builder.userDescription(descriptionToApply);
            if (response.getOwnerGroups() != null && !response.getOwnerGroups().isEmpty()) {
                if (allowMultiple) {
                    builder.ownerGroups(response.getOwnerGroups().stream()
                            .map(SuggestionResponse.SuggestedItem::getValue)
                            .collect(Collectors.toSet()));
                } else {
                    builder.ownerGroup(response.getOwnerGroups().get(0).getValue());
                }
            }
            if (response.getOwnerUsers() != null && !response.getOwnerUsers().isEmpty()) {
                if (allowMultiple) {
                    builder.ownerUsers(response.getOwnerUsers().stream()
                            .map(SuggestionResponse.SuggestedItem::getValue)
                            .collect(Collectors.toSet()));
                } else {
                    builder.ownerUser(response.getOwnerUsers().get(0).getValue());
                }
            }
            boolean includesTags = false;
            if (response.getAtlanTags() != null && !response.getAtlanTags().isEmpty()) {
                includesTags = true;
                if (allowMultiple) {
                    builder.atlanTags(response.getAtlanTags().stream()
                            .map(t -> AtlanTag.builder()
                                    .typeName(t.getValue())
                                    .propagate(false)
                                    .build())
                            .collect(Collectors.toSet()));
                } else {
                    builder.atlanTag(AtlanTag.builder()
                            .typeName(response.getAtlanTags().get(0).getValue())
                            .propagate(false)
                            .build());
                }
            }
            if (response.getAssignedTerms() != null
                    && !response.getAssignedTerms().isEmpty()) {
                if (allowMultiple) {
                    builder.assignedTerms(response.getAssignedTerms().stream()
                            .map(SuggestionResponse.SuggestedTerm::getValue)
                            .collect(Collectors.toSet()));
                } else {
                    builder.assignedTerm(response.getAssignedTerms().get(0).getValue());
                }
            }
            return new Apply(builder.build(), includesTags);
        }

        private static List<SuggestionResponse.SuggestedItem> getDescriptions(AggregationResult res, AtlanField field) {
            List<SuggestionResponse.SuggestedItem> results = new ArrayList<>();
            if (res instanceof AggregationBucketResult) {
                AggregationBucketResult result = (AggregationBucketResult) res;
                for (AggregationBucketDetails bucket : result.getBuckets()) {
                    long count = bucket.getDocCount();
                    String value = bucket.getSourceValue(field).toString();
                    if (!value.isBlank()) {
                        results.add(new SuggestionResponse.SuggestedItem(count, value));
                    }
                }
            }
            return results;
        }

        private static List<SuggestionResponse.SuggestedTerm> getTerms(AggregationResult res) {
            List<SuggestionResponse.SuggestedTerm> results = new ArrayList<>();
            if (res instanceof AggregationBucketResult) {
                AggregationBucketResult result = (AggregationBucketResult) res;
                for (AggregationBucketDetails bucket : result.getBuckets()) {
                    long count = bucket.getDocCount();
                    String value = bucket.getKey().toString();
                    if (!value.isBlank()) {
                        results.add(new SuggestionResponse.SuggestedTerm(count, value));
                    }
                }
            }
            return results;
        }

        private static List<SuggestionResponse.SuggestedItem> getTags(AtlanClient client, AggregationResult res)
                throws AtlanException {
            List<SuggestionResponse.SuggestedItem> results = new ArrayList<>();
            if (res instanceof AggregationBucketResult) {
                AggregationBucketResult result = (AggregationBucketResult) res;
                for (AggregationBucketDetails bucket : result.getBuckets()) {
                    long count = bucket.getDocCount();
                    String value = bucket.getKey().toString();
                    if (!value.isBlank()) {
                        String name;
                        try {
                            name = client.getAtlanTagCache().getNameForSid(value);
                        } catch (NotFoundException e) {
                            name = Serde.DELETED_AUDIT_OBJECT;
                        }
                        if (name == null) {
                            name = Serde.DELETED_AUDIT_OBJECT;
                        }
                        results.add(new SuggestionResponse.SuggestedItem(count, name));
                    }
                }
            }
            return results;
        }

        private static List<SuggestionResponse.SuggestedItem> getOthers(AggregationResult res) {
            List<SuggestionResponse.SuggestedItem> results = new ArrayList<>();
            if (res instanceof AggregationBucketResult) {
                AggregationBucketResult result = (AggregationBucketResult) res;
                for (AggregationBucketDetails bucket : result.getBuckets()) {
                    long count = bucket.getDocCount();
                    String value = bucket.getKey().toString();
                    if (!value.isBlank()) {
                        results.add(new SuggestionResponse.SuggestedItem(count, value));
                    }
                }
            }
            return results;
        }
    }

    private static @Nullable String getDescriptionToApply(SuggestionResponse response) {
        long maxDescriptionCount = 0;
        String descriptionToApply = null;
        if (response.getUserDescriptions() != null
                && !response.getUserDescriptions().isEmpty()) {
            maxDescriptionCount = response.getUserDescriptions().get(0).getCount();
            descriptionToApply = response.getUserDescriptions().get(0).getValue();
        }
        if (response.getSystemDescriptions() != null
                && !response.getSystemDescriptions().isEmpty()) {
            if (response.getSystemDescriptions().get(0).getCount() > maxDescriptionCount) {
                descriptionToApply = response.getSystemDescriptions().get(0).getValue();
            }
        }
        return descriptionToApply;
    }

    @Getter
    private static class Apply {
        boolean includesTags;
        Asset asset;

        private Apply(Asset asset, boolean includesTags) {
            this.asset = asset;
            this.includesTags = includesTags;
        }
    }
}
