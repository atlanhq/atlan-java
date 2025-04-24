/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.workflow.*;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * API endpoints for operating on Atlan's workflows.
 */
public class WorkflowsEndpoint extends HeraclesEndpoint {

    private static final String workflows_endpoint = "/workflows";
    private static final String workflows_endpoint_run_existing = workflows_endpoint + "/submit";
    private static final String workflows_search_endpoint = workflows_endpoint + "/indexsearch";
    private static final String runs_endpoint = "/runs";
    private static final String runs_search_endpoint = runs_endpoint + "/indexsearch";

    public WorkflowsEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Run the provided workflow.
     *
     * @param workflow details of the workflow to run
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowResponse run(Workflow workflow) throws AtlanException {
        return run(workflow, null);
    }

    /**
     * Run the provided workflow.
     *
     * @param workflow details of the workflow to run
     * @param options to override default client settings
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowResponse run(Workflow workflow, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), String.format("%s?submit=true", workflows_endpoint));
        WorkflowResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, workflow, WorkflowResponse.class, options);
        response.setClient(client);
        return response;
    }

    /**
     * Run the provided pre-existing workflow.
     *
     * @param workflow details of the pre-existing workflow to re-run
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowRunResponse run(WorkflowSearchResultDetail workflow) throws AtlanException {
        return run(workflow, null);
    }

    /**
     * Run the provided pre-existing workflow.
     *
     * @param workflow details of the pre-existing workflow to re-run
     * @param options to override default client settings
     * @return details of the workflow run
     * @throws AtlanException on any API communication issue
     */
    public WorkflowRunResponse run(WorkflowSearchResultDetail workflow, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), workflows_endpoint_run_existing);
        ReRunRequest request = ReRunRequest.builder()
                .namespace(workflow.getMetadata().getNamespace())
                .resourceName(workflow.getSpec().getWorkflowTemplateRef().get("name"))
                .build();
        WorkflowRunResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, WorkflowRunResponse.class, options);
        response.setClient(client);
        return response;
    }

    /**
     * Stop the provided, running workflow.
     *
     * @param runName name of the workflow run to stop
     * @return details of the stopped workflow
     * @throws AtlanException on any API communication issue
     */
    public WorkflowRunResponse stop(String runName) throws AtlanException {
        return stop(runName, null);
    }

    /**
     * Stop the provided, running workflow.
     *
     * @param runName name of the workflow run to stop
     * @param options to override default client settings
     * @return details of the stopped workflow
     * @throws AtlanException on any API communication issue
     */
    public WorkflowRunResponse stop(String runName, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s/stop", getBaseUrl(), runs_endpoint, runName);
        WorkflowRunResponse response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, "", WorkflowRunResponse.class, options);
        response.setClient(client);
        return response;
    }

    /**
     * Archive (delete) the provided workflow.
     *
     * @param workflowName the workflow to delete
     * @throws AtlanException on any API communication issue
     */
    public void archive(String workflowName) throws AtlanException {
        archive(workflowName, null);
    }

    /**
     * Archive (delete) the provided workflow.
     *
     * @param workflowName the workflow to delete
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void archive(String workflowName, RequestOptions options) throws AtlanException {
        String url =
                String.format("%s%s", getBaseUrl(), String.format("%s/%s/archive", workflows_endpoint, workflowName));
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, "", options);
    }

    /**
     * Search for workflow runs that meet the provided criteria.
     *
     * @param request criteria by which to find workflow runs
     * @return the matching workflow runs
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse searchRuns(WorkflowSearchRequest request) throws AtlanException {
        return searchRuns(request, null);
    }

    /**
     * Search for workflow runs that meet the provided criteria.
     *
     * @param request criteria by which to find workflow runs
     * @param options to override default client settings
     * @return the matching workflow runs
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse searchRuns(WorkflowSearchRequest request, RequestOptions options)
            throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), runs_search_endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, WorkflowSearchResponse.class, options);
    }

    /**
     * Search for workflows that meet the provided criteria.
     *
     * @param request criteria by which to find workflows
     * @return the matching workflows
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse search(WorkflowSearchRequest request) throws AtlanException {
        return search(request, null);
    }

    /**
     * Search for workflows that meet the provided criteria.
     *
     * @param request criteria by which to find workflows
     * @param options to override default client settings
     * @return the matching workflows
     * @throws AtlanException on any API communication issue
     */
    public WorkflowSearchResponse search(WorkflowSearchRequest request, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), workflows_search_endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, WorkflowSearchResponse.class, options);
    }

    /**
     * Update a given workflow's configuration.
     *
     * @param workflowName name of the workflow to update
     * @param request full details of the workflow's revised configuration
     * @return the updated workflow configuration
     * @throws AtlanException on any API communication issue
     */
    public Workflow update(String workflowName, Workflow request) throws AtlanException {
        return update(workflowName, request, null);
    }

    /**
     * Update a given workflow's configuration.
     *
     * @param workflowName name of the workflow to update
     * @param request full details of the workflow's revised configuration
     * @param options to override default client settings
     * @return the updated workflow configuration
     * @throws AtlanException on any API communication issue
     */
    public Workflow update(String workflowName, Workflow request, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), workflows_endpoint, workflowName);
        WrappedWorkflow response = ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, request, WrappedWorkflow.class, options);
        if (response != null) {
            Workflow workflow = response.getWorkflow();
            workflow.setRawJsonObject(response.getRawJsonObject());
            return workflow;
        }
        return null;
    }

    /**
     * Request class for re-running a pre-existing workflow.
     */
    @Getter
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    static class ReRunRequest extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Namespace of the workflow. */
        @Builder.Default
        String namespace = "default";

        /** Resource type of the workflow. */
        @Builder.Default
        String resourceKind = "WorkflowTemplate";

        /** Name of the workflow. */
        String resourceName;
    }

    /**
     * Necessary for handling responses that are plain Workflow without any wrapping.
     */
    @Getter
    @JsonSerialize(using = WrappedWorkflowSerializer.class)
    @JsonDeserialize(using = WrappedWorkflowDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    private static final class WrappedWorkflow extends ApiResource {
        private static final long serialVersionUID = 2L;

        Workflow workflow;

        public WrappedWorkflow(Workflow workflow) {
            this.workflow = workflow;
        }
    }

    private static class WrappedWorkflowDeserializer extends StdDeserializer<WrappedWorkflow> {
        private static final long serialVersionUID = 2L;

        public WrappedWorkflowDeserializer() {
            this(WrappedWorkflow.class);
        }

        public WrappedWorkflowDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public WrappedWorkflow deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            Workflow workflow = parser.getCodec().readValue(parser, new TypeReference<>() {});
            return new WrappedWorkflow(workflow);
        }
    }

    private static class WrappedWorkflowSerializer extends StdSerializer<WrappedWorkflow> {
        private static final long serialVersionUID = 2L;

        private final transient AtlanClient client;

        @SuppressWarnings("UnusedMethod")
        public WrappedWorkflowSerializer(AtlanClient client) {
            this(WrappedWorkflow.class, client);
        }

        public WrappedWorkflowSerializer(Class<WrappedWorkflow> t, AtlanClient client) {
            super(t);
            this.client = client;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(WrappedWorkflow wrappedWorkflow, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            Workflow workflow = wrappedWorkflow.getWorkflow();
            client.writeValue(gen, workflow);
        }
    }
}
