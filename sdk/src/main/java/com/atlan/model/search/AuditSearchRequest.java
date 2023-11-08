/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.Atlan;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.fields.KeywordField;
import com.atlan.model.fields.NumericField;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Class from which to configure and run a search against Atlan's activity log.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AuditSearchRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    public static final NumericField CREATED = new NumericField("createTime", "created");
    public static final KeywordField ENTITY_ID = new KeywordField("guid", "entityId");
    public static final KeywordField ENTITY_TYPE = new KeywordField("typeName", "typeName");
    public static final KeywordField QUALIFIED_NAME = new KeywordField("qualifiedName", "entityQualifiedName");
    public static final KeywordField USER = new KeywordField("updatedBy", "user");

    private static final SortOptions LATEST_FIRST = CREATED.order(SortOrder.Desc);

    /** Parameters for the search itself. */
    IndexSearchDSL dsl;

    /** Attributes to include in the entityDetail of each resulting audit entry. */
    @Singular
    List<String> attributes;

    /**
     * Run the search.
     *
     * @return the matching audit log records
     */
    public AuditSearchResponse search() throws AtlanException {
        return search(Atlan.getDefaultClient());
    }

    /**
     * Run the search.
     *
     * @param client connectivity to the Atlan tenant on which to search the audit logs
     * @return the matching audit log records
     */
    public AuditSearchResponse search(AtlanClient client) throws AtlanException {
        return client.assets.auditLogs(this);
    }

    /**
     * Start building an audit search request for the last changes to an asset, by its GUID.
     *
     * @param guid unique identifier of the asset for which to retrieve the audit history
     * @param size number of changes to retrieve
     * @return a request builder pre-configured with these criteria
     */
    public static AuditSearchRequestBuilder<?, ?> byGuid(String guid, int size) {
        return byGuid(Atlan.getDefaultClient(), guid, size);
    }

    /**
     * Start building an audit search request for the last changes to an asset, by its GUID.
     *
     * @param client connectivity to the Atlan tenant on which to search the audit logs
     * @param guid unique identifier of the asset for which to retrieve the audit history
     * @param size number of changes to retrieve
     * @return a request builder pre-configured with these criteria
     */
    public static AuditSearchRequestBuilder<?, ?> byGuid(AtlanClient client, String guid, int size) {
        return AuditSearch.builder(client)
                .where(ENTITY_ID.eq(guid))
                .pageSize(size)
                .sort(LATEST_FIRST)
                .toRequestBuilder();
    }

    /**
     * Start building an audit search request for the last changes to an asset, by its qualifiedName.
     *
     * @param typeName the type of asset for which to retrieve the audit history
     * @param qualifiedName unique name of the asset for which to retrieve the audit history
     * @param size number of changes to retrieve
     * @return a request builder pre-configured with these criteria
     */
    public static AuditSearchRequestBuilder<?, ?> byQualifiedName(String typeName, String qualifiedName, int size) {
        return byQualifiedName(Atlan.getDefaultClient(), typeName, qualifiedName, size);
    }

    /**
     * Start building an audit search request for the last changes to an asset, by its qualifiedName.
     *
     * @param client connectivity to the Atlan tenant on which to search the audit logs
     * @param typeName the type of asset for which to retrieve the audit history
     * @param qualifiedName unique name of the asset for which to retrieve the audit history
     * @param size number of changes to retrieve
     * @return a request builder pre-configured with these criteria
     */
    public static AuditSearchRequestBuilder<?, ?> byQualifiedName(
            AtlanClient client, String typeName, String qualifiedName, int size) {
        return AuditSearch.builder(client)
                .where(QUALIFIED_NAME.eq(qualifiedName))
                .where(ENTITY_TYPE.eq(typeName))
                .pageSize(size)
                .sort(LATEST_FIRST)
                .toRequestBuilder();
    }

    /**
     * Start building an audit search request for the last changes made to any assets, by a given user.
     *
     * @param userName the name of the user for which to look for any changes
     * @param size number of changes to retrieve
     * @return a request builder pre-configured with these criteria
     */
    public static AuditSearchRequestBuilder<?, ?> byUser(String userName, int size) {
        return byUser(Atlan.getDefaultClient(), userName, size);
    }

    /**
     * Start building an audit search request for the last changes made to any assets, by a given user.
     *
     * @param client connectivity to the Atlan tenant on which to search the audit logs
     * @param userName the name of the user for which to look for any changes
     * @param size number of changes to retrieve
     * @return a request builder pre-configured with these criteria
     */
    public static AuditSearchRequestBuilder<?, ?> byUser(AtlanClient client, String userName, int size) {
        return AuditSearch.builder(client)
                .where(USER.eq(userName))
                .pageSize(size)
                .sort(LATEST_FIRST)
                .toRequestBuilder();
    }
}
