/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.api;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.*;
import com.atlan.model.search.*;
import com.atlan.model.tasks.AtlanTask;
import com.atlan.model.tasks.FluentTasks;
import com.atlan.model.tasks.TaskSearchRequest;
import com.atlan.model.tasks.TaskSearchResponse;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
import lombok.extern.slf4j.Slf4j;

/**
 * API endpoints for operating on tasks.
 */
@Slf4j
public class TaskEndpoint extends AtlasEndpoint {

    private static final String search_endpoint = "/task/search";

    public TaskEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Start a fluent task queue search that will return all tasks.
     * Additional conditions can be chained onto the returned filter before any
     * task retrieval is attempted, ensuring all conditions are pushed-down for
     * optimal retrieval.
     *
     * @return a fluent task queue search that includes all tasks
     */
    public FluentTasks.FluentTasksBuilder select() {
        return FluentTasks.builder(client);
    }

    /**
     * Run the requested search.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public TaskSearchResponse search(TaskSearchRequest request) throws AtlanException {
        return search(request, null);
    }

    /**
     * Run the requested search.
     *
     * @param request detailing the search query, parameters, and so on to run
     * @param options to override default client settings
     * @return the results of the search
     * @throws AtlanException on any API interaction problems
     */
    public TaskSearchResponse search(TaskSearchRequest request, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), search_endpoint);
        boolean missingSort =
                request.getDsl().getSort() == null || request.getDsl().getSort().isEmpty();
        boolean missingGuidSort = true;
        if (!missingSort) {
            // If there is some sort, see whether GUID is already included
            for (SortOptions option : request.getDsl().getSort()) {
                if (option.isField()) {
                    String fieldName = option.field().field();
                    if (AtlanTask.START_TIME.getNumericFieldName().equals(fieldName)) {
                        missingGuidSort = false;
                        break;
                    }
                }
            }
        }
        if (missingGuidSort) {
            // If there is no sort by GUID, always add it as a final (tie-breaker) criteria
            // to ensure there is consistent paging (unfortunately sorting by _doc still has duplicates
            // across large number of pages)
            request = request.toBuilder()
                    .dsl(request.getDsl().toBuilder()
                            .sortOption(AtlanTask.START_TIME.order(SortOrder.Asc))
                            .build())
                    .build();
        }
        TaskSearchResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, TaskSearchResponse.class, options);
        response.setClient(client);
        response.setRequest(request);
        return response;
    }
}
