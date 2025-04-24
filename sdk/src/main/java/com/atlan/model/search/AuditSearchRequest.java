/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.search;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
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
@SuppressWarnings("serial")
public class AuditSearchRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** When the asset was created. */
    public static final NumericField CREATED = new NumericField("createTime", "created");

    /** Unique identifier (GUID) of the asset that was created or changed. */
    public static final KeywordField ENTITY_ID = new KeywordField("guid", "entityId");

    /** Type of the asset that was created or changed. */
    public static final KeywordField ENTITY_TYPE = new KeywordField("typeName", "typeName");

    /** Unique name of the asset that was created or changed. */
    public static final KeywordField QUALIFIED_NAME = new KeywordField("qualifiedName", "entityQualifiedName");

    /** User who made the update to the asset. */
    public static final KeywordField USER = new KeywordField("updatedBy", "user");

    /** Type of action made against the asset. */
    public static final KeywordField ACTION = new KeywordField("action", "action");

    /** Type of actor (e.g. {@code workflow}) that created or changed the asset, if it was done programmatically. */
    public static final KeywordField AGENT = new KeywordField("headers", "headers.x-atlan-agent");

    /** Name of the package that created or changed the asset. */
    public static final KeywordField PACKAGE_NAME = new KeywordField("headers", "headers.x-atlan-agent-package-name");

    /** Name of the workflow (specific configuration of a package) that created or changed the asset. */
    public static final KeywordField WORKFLOW_ID = new KeywordField("headers", "headers.x-atlan-agent-workflow-id");

    /** Name of the agent (specific run of a workflow) that created or changed the asset. */
    public static final KeywordField AGENT_ID = new KeywordField("headers", "headers.x-atlan-agent-id");

    private static final SortOptions LATEST_FIRST = CREATED.order(SortOrder.Desc);

    /** Parameters for the search itself. */
    IndexSearchDSL dsl;

    /** Attributes to include in the entityDetail of each resulting audit entry. */
    @Singular
    List<String> attributes;

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

    /**
     * Start building an audit search request for the last common action made to any assets.
     *
     * @param client connectivity to the Atlan tenant on which to search the audit logs
     * @param action type of action (e.g. {@code ENTITY_CREATE})
     * @param size number of changes to retrieve
     * @return a request builder pre-configured with these criteria
     */
    public static AuditSearchRequestBuilder<?, ?> byAction(AtlanClient client, String action, int size) {
        return AuditSearch.builder(client)
                .where(ACTION.eq(action))
                .pageSize(size)
                .sort(LATEST_FIRST)
                .toRequestBuilder();
    }
}
