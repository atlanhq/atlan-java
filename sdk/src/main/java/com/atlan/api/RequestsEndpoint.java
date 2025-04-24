/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.enums.AtlanRequestStatus;
import com.atlan.net.ApiResource;
import com.atlan.net.RequestOptions;
import com.fasterxml.jackson.annotation.JsonInclude;
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
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * API endpoints for interacting with Atlan's requests.
 */
public class RequestsEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/requests";
    private static final int defaultLimit = 40;

    public RequestsEndpoint(AtlanClient client) {
        super(client);
    }

    // TODO: eventually provide a rich RQL object for the filter

    /**
     * Retrieve a single request by its unique identifier (GUID).
     *
     * @param guid of the request to retrieve
     * @return the unique request, or null if none exists with that GUID
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequest get(String guid) throws AtlanException {
        return get(guid, null);
    }

    /**
     * Retrieve a single request by its unique identifier (GUID).
     *
     * @param guid of the request to retrieve
     * @param options to override default client settings
     * @return the unique request, or null if none exists with that GUID
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequest get(String guid, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint, guid);
        WrappedRequest result =
                ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", WrappedRequest.class, options);
        if (result != null) {
            AtlanRequest request = result.getRequest();
            request.setRawJsonObject(result.getRawJsonObject());
            return request;
        }
        return null;
    }

    /**
     * Retrieves a list of the requests defined in Atlan.
     *
     * @param filter which requests to retrieve
     * @param sort property by which to sort the results
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @return a list of requests that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequestResponse list(String filter, String sort, int offset, int limit) throws AtlanException {
        return list(filter, sort, offset, limit, null);
    }

    /**
     * Retrieves a list of the requests defined in Atlan.
     *
     * @param filter which requests to retrieve
     * @param sort property by which to sort the results
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @param options to override default client settings
     * @return a list of requests that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequestResponse list(String filter, String sort, int offset, int limit, RequestOptions options)
            throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        if (sort == null) {
            sort = "";
        }
        String url = String.format(
                "%s%s?limit=%s&offset=%s&sort=%s&filter=%s",
                getBaseUrl(), endpoint, limit, offset, ApiResource.urlEncode(sort), ApiResource.urlEncode(filter));
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", AtlanRequestResponse.class, options);
    }

    /**
     * Retrieves a list of the requests defined in Atlan.
     *
     * @param filter which requests to retrieve
     * @return a list of requests that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequestResponse list(String filter) throws AtlanException {
        return list(filter, null);
    }

    /**
     * Retrieves a list of the requests defined in Atlan.
     *
     * @param filter which requests to retrieve
     * @param options to override default client settings
     * @return a list of requests that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequestResponse list(String filter, RequestOptions options) throws AtlanException {
        return list(filter, "-createdAt", 0, defaultLimit, options);
    }

    /**
     * Retrieve the list of requests defined in Atlan as you would via the Admin UI.
     *
     * @return a list of all the requests in Atlan
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequestResponse list() throws AtlanException {
        return list((RequestOptions) null);
    }

    /**
     * Retrieve the list of requests defined in Atlan as you would via the Admin UI.
     *
     * @param options to override default client settings
     * @return a list of all the requests in Atlan
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequestResponse list(RequestOptions options) throws AtlanException {
        return list(
                "{\"$and\":[{\"isDuplicate\":false},{\"status\":{\"$in\":[\"active\"]}}]}",
                "-createdAt",
                0,
                defaultLimit,
                options);
    }

    /**
     * Retrieve the list of requests that can be acted upon.
     *
     * @param filter which requests to retrieve
     * @param sort property by which to sort the results
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @return a list of actionable requests that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequestResponse listActionable(String filter, String sort, int offset, int limit)
            throws AtlanException {
        return listActionable(filter, sort, offset, limit, null);
    }

    /**
     * Retrieve the list of requests that can be acted upon.
     *
     * @param filter which requests to retrieve
     * @param sort property by which to sort the results
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @param options to override default client settings
     * @return a list of actionable requests that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequestResponse listActionable(
            String filter, String sort, int offset, int limit, RequestOptions options) throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        if (sort == null) {
            sort = "";
        }
        String url = String.format(
                "%s%s/actionable?limit=%s&offset=%s&sort=%s&filter=%s",
                getBaseUrl(), endpoint, limit, offset, ApiResource.urlEncode(sort), ApiResource.urlEncode(filter));
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", AtlanRequestResponse.class, options);
    }

    /**
     * Retrieve the list of requests that can be acted upon.
     *
     * @param filter which requests to retrieve
     * @return a list of actionable requests that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequestResponse listActionable(String filter) throws AtlanException {
        return listActionable(filter, null);
    }

    /**
     * Retrieve the list of requests that can be acted upon.
     *
     * @param filter which requests to retrieve
     * @param options to override default client settings
     * @return a list of actionable requests that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequestResponse listActionable(String filter, RequestOptions options) throws AtlanException {
        return listActionable(filter, "-createdAt", 0, defaultLimit, options);
    }

    /**
     * Retrieve the list of requests that can be acted upon.
     *
     * @return a list of actionable requests based on default criteria
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequestResponse listActionable() throws AtlanException {
        return listActionable((RequestOptions) null);
    }

    /**
     * Retrieve the list of requests that can be acted upon.
     *
     * @param options to override default client settings
     * @return a list of actionable requests based on default criteria
     * @throws AtlanException on any API communication issue
     */
    public AtlanRequestResponse listActionable(RequestOptions options) throws AtlanException {
        return listActionable("", "-createdAt", 0, defaultLimit, options); // TODO: check default filter and limit
    }

    /**
     * Create a new request.
     *
     * @param request the details of the new request
     * @throws AtlanException on any API communication issue
     */
    public void create(AtlanRequest request) throws AtlanException {
        create(request, null);
    }

    /**
     * Create a new request.
     *
     * @param request the details of the new request
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void create(AtlanRequest request, RequestOptions options) throws AtlanException {
        if (request != null) {
            create(List.of(request), options);
        }
    }

    /**
     * Create multiple new requests.
     *
     * @param requests the details of the new requests to create
     * @throws AtlanException on any API communication issue
     */
    public void create(List<AtlanRequest> requests) throws AtlanException {
        create(requests, null);
    }

    /**
     * Create multiple new requests.
     *
     * @param requests the details of the new requests to create
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void create(List<AtlanRequest> requests, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/bulk", getBaseUrl(), endpoint);
        BulkRequest br = new BulkRequest(requests);
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, br, null, options);
    }

    /**
     * Approve the specified request in Atlan.
     *
     * @param guid unique identifier (GUID) of the request to approve
     * @param message (optional) message to include with the approval
     * @return true if the approval succeeded, otherwise false
     * @throws AtlanException on any API interaction issues
     */
    public boolean approve(String guid, String message) throws AtlanException {
        return approve(guid, message, null);
    }

    /**
     * Approve the specified request in Atlan.
     *
     * @param guid unique identifier (GUID) of the request to approve
     * @param message (optional) message to include with the approval
     * @param options to override default client settings
     * @return true if the approval succeeded, otherwise false
     * @throws AtlanException on any API interaction issues
     */
    public boolean approve(String guid, String message, RequestOptions options) throws AtlanException {
        return action(guid, AtlanRequestStatus.APPROVED, message, options);
    }

    /**
     * Reject the specified request in Atlan.
     *
     * @param guid unique identifier (GUID) of the request to reject
     * @param message (optional) message to include with the rejection
     * @return true if the rejection succeeded, otherwise false
     * @throws AtlanException on any API interaction issues
     */
    public boolean reject(String guid, String message) throws AtlanException {
        return reject(guid, message, null);
    }

    /**
     * Reject the specified request in Atlan.
     *
     * @param guid unique identifier (GUID) of the request to reject
     * @param message (optional) message to include with the rejection
     * @param options to override default client settings
     * @return true if the rejection succeeded, otherwise false
     * @throws AtlanException on any API interaction issues
     */
    public boolean reject(String guid, String message, RequestOptions options) throws AtlanException {
        return action(guid, AtlanRequestStatus.REJECTED, message, options);
    }

    private boolean action(String guid, AtlanRequestStatus action, String message, RequestOptions options)
            throws AtlanException {
        String url = String.format("%s%s/%s/action", getBaseUrl(), endpoint, guid);
        if (message == null) {
            message = "";
        }
        AtlanRequestAction ara = new AtlanRequestAction(action, message);
        WrappedString string =
                ApiResource.request(client, ApiResource.RequestMethod.POST, url, ara, WrappedString.class, options);
        return string != null
                && string.getResult() != null
                && string.getResult().equals("success");
    }

    /**
     * Necessary for wrapping requests into an object that extends ApiResource for API interactions.
     */
    @Getter
    @EqualsAndHashCode(callSuper = false)
    @SuppressWarnings("serial")
    private static final class BulkRequest extends ApiResource {
        private static final long serialVersionUID = 2L;

        List<AtlanRequest> requests;

        public BulkRequest(List<AtlanRequest> requests) {
            this.requests = requests;
        }
    }

    /**
     * Necessary for wrapping approval and rejection requests into an object that extends ApiResource for API interactions.
     */
    @Getter
    @EqualsAndHashCode(callSuper = false)
    private static final class AtlanRequestAction extends ApiResource {
        private static final long serialVersionUID = 2L;

        /** Action to take on the request. */
        AtlanRequestStatus action;

        /** Optional comment to include alongside the action. */
        @JsonInclude(JsonInclude.Include.ALWAYS)
        String message;

        public AtlanRequestAction(AtlanRequestStatus action, String message) {
            this.action = action;
            this.message = message;
        }
    }

    /**
     * Necessary for handling responses that are single requests.
     */
    @Getter
    @JsonSerialize(using = WrappedRequestSerializer.class)
    @JsonDeserialize(using = WrappedRequestDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    private static final class WrappedRequest extends ApiResource {
        private static final long serialVersionUID = 2L;

        AtlanRequest request;

        public WrappedRequest(AtlanRequest request) {
            this.request = request;
        }
    }

    private static class WrappedRequestDeserializer extends StdDeserializer<WrappedRequest> {
        private static final long serialVersionUID = 2L;

        public WrappedRequestDeserializer() {
            this(null);
        }

        public WrappedRequestDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public WrappedRequest deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            List<AtlanRequest> response = parser.getCodec().readValue(parser, new TypeReference<>() {});
            if (response != null && !response.isEmpty()) {
                return new WrappedRequest(response.get(0));
            }
            return null;
        }
    }

    private static class WrappedRequestSerializer extends StdSerializer<WrappedRequest> {
        private static final long serialVersionUID = 2L;

        private final transient AtlanClient client;

        @SuppressWarnings("UnusedMethod")
        public WrappedRequestSerializer(AtlanClient client) {
            this(WrappedRequest.class, client);
        }

        public WrappedRequestSerializer(Class<WrappedRequest> t, AtlanClient client) {
            super(t);
            this.client = client;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(WrappedRequest wrappedRequest, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            AtlanRequest request = wrappedRequest.getRequest();
            client.writeValue(gen, List.of(request));
        }
    }

    /**
     * Necessary for handling responses that are plain strings rather than JSON.
     */
    @Getter
    @JsonSerialize(using = WrappedStringSerializer.class)
    @JsonDeserialize(using = WrappedStringDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    private static final class WrappedString extends ApiResource {
        private static final long serialVersionUID = 2L;

        String result;

        public WrappedString(String result) {
            this.result = result;
        }
    }

    private static class WrappedStringDeserializer extends StdDeserializer<WrappedString> {
        private static final long serialVersionUID = 2L;

        public WrappedStringDeserializer() {
            this(null);
        }

        public WrappedStringDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public WrappedString deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            String string = parser.getCodec().readValue(parser, new TypeReference<>() {});
            return new WrappedString(string);
        }
    }

    private static class WrappedStringSerializer extends StdSerializer<WrappedString> {
        private static final long serialVersionUID = 2L;

        private final transient AtlanClient client;

        @SuppressWarnings("UnusedMethod")
        public WrappedStringSerializer(AtlanClient client) {
            this(WrappedString.class, client);
        }

        public WrappedStringSerializer(Class<WrappedString> t, AtlanClient client) {
            super(t);
            this.client = client;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(WrappedString wrappedString, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            String string = wrappedString.getResult();
            client.writeValue(gen, string);
        }
    }
}
