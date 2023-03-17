/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.enums.AtlanRequestStatus;
import com.atlan.net.ApiResource;
import com.atlan.serde.Serde;
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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API endpoints for interacting with Atlan's requests.
 */
public class RequestsEndpoint {

    private static final String endpoint = "/api/service/requests";
    private static final int defaultLimit = 40;

    // TODO: eventually provide a rich RQL object for the filter

    /**
     * Retrieve a single request by its unique identifier (GUID).
     *
     * @param guid of the request to retrieve
     * @return the unique request, or null if none exists with that GUID
     * @throws AtlanException on any API communication issue
     */
    public static AtlanRequest getRequestByGuid(String guid) throws AtlanException {
        String url = String.format("%s%s/%s", Atlan.getBaseUrl(), endpoint, guid);
        WrappedRequest result = ApiResource.request(ApiResource.RequestMethod.GET, url, "", WrappedRequest.class, null);
        if (result != null) {
            return result.getRequest();
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
    public static AtlanRequestResponse getRequests(String filter, String sort, int offset, int limit)
            throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        if (sort == null) {
            sort = "";
        }
        String url = String.format(
                "%s%s?limit=%s&offset=%s&sort=%s&filter=%s",
                Atlan.getBaseUrl(),
                endpoint,
                limit,
                offset,
                ApiResource.urlEncode(sort),
                ApiResource.urlEncode(filter));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", AtlanRequestResponse.class, null);
    }

    /**
     * Retrieves a list of the requests defined in Atlan.
     *
     * @param filter which requests to retrieve
     * @return a list of requests that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public static AtlanRequestResponse getRequests(String filter) throws AtlanException {
        return getRequests(filter, "-createdAt", 0, defaultLimit);
    }

    /**
     * Retrieve the list of requests defined in Atlan as you would via the Admin UI.
     *
     * @return a list of all the requests in Atlan
     * @throws AtlanException on any API communication issue
     */
    public static AtlanRequestResponse getRequests() throws AtlanException {
        return getRequests(
                "{\"$and\":[{\"isDuplicate\":false},{\"status\":{\"$in\":[\"active\"]}}]}",
                "-createdAt",
                0,
                defaultLimit);
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
    public static AtlanRequestResponse getActionableRequests(String filter, String sort, int offset, int limit)
            throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        if (sort == null) {
            sort = "";
        }
        String url = String.format(
                "%s%s/actionable?limit=%s&offset=%s&sort=%s&filter=%s",
                Atlan.getBaseUrl(),
                endpoint,
                limit,
                offset,
                ApiResource.urlEncode(sort),
                ApiResource.urlEncode(filter));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", AtlanRequestResponse.class, null);
    }

    /**
     * Retrieve the list of requests that can be acted upon.
     *
     * @param filter which requests to retrieve
     * @return a list of actionable requests that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public static AtlanRequestResponse getActionableRequests(String filter) throws AtlanException {
        return getActionableRequests(filter, "-createdAt", 0, defaultLimit);
    }

    /**
     * Retrieve the list of requests that can be acted upon.
     *
     * @return a list of actionable requests based on default criteria
     * @throws AtlanException on any API communication issue
     */
    public static AtlanRequestResponse getActionableRequests() throws AtlanException {
        return getActionableRequests("", "-createdAt", 0, defaultLimit); // TODO: check default filter and limit
    }

    /**
     * Create a new request.
     *
     * @param request the details of the new request
     * @throws AtlanException on any API communication issue
     */
    public static void createRequest(AtlanRequest request) throws AtlanException {
        if (request != null) {
            createRequests(List.of(request));
        }
    }

    /**
     * Create multiple new requests.
     *
     * @param requests the details of the new requests to create
     * @throws AtlanException on any API communication issue
     */
    public static void createRequests(List<AtlanRequest> requests) throws AtlanException {
        String url = String.format("%s%s/bulk", Atlan.getBaseUrl(), endpoint);
        BulkRequest br = new BulkRequest(requests);
        ApiResource.request(ApiResource.RequestMethod.POST, url, br, null, null);
    }

    /**
     * Approve the specified request in Atlan.
     *
     * @param guid unique identifier (GUID) of the request to approve
     * @param message (optional) message to include with the approval
     * @return true if the approval succeeded, otherwise false
     * @throws AtlanException on any API interaction issues
     */
    public static boolean approveRequest(String guid, String message) throws AtlanException {
        return actionRequest(guid, AtlanRequestStatus.APPROVED, message);
    }

    /**
     * Reject the specified request in Atlan.
     *
     * @param guid unique identifier (GUID) of the request to reject
     * @param message (optional) message to include with the rejection
     * @return true if the rejection succeeded, otherwise false
     * @throws AtlanException on any API interaction issues
     */
    public static boolean rejectRequest(String guid, String message) throws AtlanException {
        return actionRequest(guid, AtlanRequestStatus.REJECTED, message);
    }

    private static boolean actionRequest(String guid, AtlanRequestStatus action, String message) throws AtlanException {
        String url = String.format("%s%s/%s/action", Atlan.getBaseUrl(), endpoint, guid);
        if (message == null) {
            message = "";
        }
        AtlanRequestAction ara = new AtlanRequestAction(action, message);
        WrappedString string = ApiResource.request(ApiResource.RequestMethod.POST, url, ara, WrappedString.class, null);
        return string != null
                && string.getResult() != null
                && string.getResult().equals("success");
    }

    /**
     * Necessary for wrapping requests into an object that extends ApiResource for API interactions.
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    private static final class BulkRequest extends ApiResource {
        List<AtlanRequest> requests;

        public BulkRequest(List<AtlanRequest> requests) {
            this.requests = requests;
        }
    }

    /**
     * Necessary for wrapping approval and rejection requests into an object that extends ApiResource for API interactions.
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    private static final class AtlanRequestAction extends ApiResource {
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
    @Data
    @JsonSerialize(using = WrappedRequestSerializer.class)
    @JsonDeserialize(using = WrappedRequestDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    private static final class WrappedRequest extends ApiResource {
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

        public WrappedRequestSerializer() {
            this(null);
        }

        public WrappedRequestSerializer(Class<WrappedRequest> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(WrappedRequest wrappedRequest, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            AtlanRequest request = wrappedRequest.getRequest();
            Serde.mapper.writeValue(gen, List.of(request));
        }
    }

    /**
     * Necessary for handling responses that are plain strings rather than JSON.
     */
    @Data
    @JsonSerialize(using = WrappedStringSerializer.class)
    @JsonDeserialize(using = WrappedStringDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    private static final class WrappedString extends ApiResource {
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

        public WrappedStringSerializer() {
            this(null);
        }

        public WrappedStringSerializer(Class<WrappedString> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(WrappedString wrappedString, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            String string = wrappedString.getResult();
            Serde.mapper.writeValue(gen, string);
        }
    }
}
