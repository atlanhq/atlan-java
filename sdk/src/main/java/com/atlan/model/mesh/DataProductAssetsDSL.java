/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.mesh;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.search.IndexSearchDSL;
import com.atlan.model.search.IndexSearchRequest;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Class from which to configure the assets that should be selected for a data product.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DataProductAssetsDSL extends AtlanObject {
    private static final long serialVersionUID = 2L;

    private static final List<String> ATTR_LIST = List.of(
            "__traitNames",
            "connectorName",
            "__customAttributes",
            "certificateStatus",
            "tenantId",
            "anchor",
            "parentQualifiedName",
            "Query.parentQualifiedName",
            "AtlasGlossaryTerm.anchor",
            "databaseName",
            "schemaName",
            "parent",
            "connectionQualifiedName",
            "collectionQualifiedName",
            "announcementMessage",
            "announcementTitle",
            "announcementType",
            "announcementUpdatedAt",
            "announcementUpdatedBy",
            "allowQuery",
            "allowQueryPreview",
            "adminGroups",
            "adminRoles",
            "adminUsers",
            "category",
            "credentialStrategy",
            "connectionSSOCredentialGuid",
            "certificateStatus",
            "certificateUpdatedAt",
            "certificateUpdatedBy",
            "classifications",
            "connectionId",
            "connectionQualifiedName",
            "connectorName",
            "dataType",
            "defaultDatabaseQualifiedName",
            "defaultSchemaQualifiedName",
            "description",
            "displayName",
            "links",
            "link",
            "meanings",
            "name",
            "ownerGroups",
            "ownerUsers",
            "qualifiedName",
            "typeName",
            "userDescription",
            "displayDescription",
            "subDataType",
            "rowLimit",
            "queryTimeout",
            "previewCredentialStrategy",
            "policyStrategy",
            "policyStrategyForSamplePreview",
            "useObjectStorage",
            "objectStorageUploadThreshold",
            "outputPortDataProducts");

    /**
     * Build a search using the provided query and default options.
     *
     * @param query the query to use for the search
     * @return the search request, with default options
     */
    public static DataProductAssetsDSLBuilder<?, ?> builder(Query query) {
        return builder(IndexSearchDSL.of(query));
    }

    /**
     * Build a search using the provided index search DSL and default options.
     *
     * @param dsl containing the query to use for the search
     * @return the search request, with default options
     */
    public static DataProductAssetsDSLBuilder<?, ?> builder(IndexSearchDSL dsl) {
        // TODO: Ideally we can remove this wrapping eventually
        Query q = dsl.getQuery();
        FilterQuery.Builder builder = new FilterQuery.Builder();
        Query wrapped = builder.filter(q).build()._toQuery();
        return DataProductAssetsDSL._internal()
                .query(IndexSearchRequest.builder(dsl.toBuilder()
                                .query(wrapped)
                                .size(null)
                                .trackTotalHits(null)
                                .build())
                        .requestMetadata(null)
                        .showSearchScore(null)
                        .excludeAtlanTags(null)
                        .excludeMeanings(null)
                        .allowDeletedRelations(null)
                        .attributes(ATTR_LIST)
                        .build());
    }

    /** Parameters for the search itself. */
    IndexSearchRequest query;

    /** Whether or not to filter scrubbed records. */
    @Builder.Default
    Boolean filterScrubbed = true;
}
