/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.core.AtlanObject;
import com.atlan.net.ApiResource;
import com.atlan.serde.Serde;
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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

/**
 * API endpoints for interacting with Atlan's purposes.
 */
public class PurposesEndpoint {

    private static final String endpoint = "/api/service/purposes";

    // TODO: eventually provide a rich RQL object for the filter

    /**
     * Retrieves a list of the purposes defined in Atlan.
     *
     * @param filter which purposes to retrieve
     * @param sort property by which to sort the results
     * @param count whether to return the total number of records (true) or not (false)
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @return a list of purposes that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public static PurposeResponse getPurposes(String filter, String sort, boolean count, int offset, int limit)
            throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        if (sort == null) {
            sort = "";
        }
        String url = String.format(
                "%s%s?filter=%s&sort=%s&count=%s&offset=%s&limit=%s",
                Atlan.getBaseUrl(),
                endpoint,
                ApiResource.urlEncode(filter),
                ApiResource.urlEncode(sort),
                count,
                offset,
                limit);
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", PurposeResponse.class, null);
    }

    /**
     * Retrieves a list of the purposes defined in Atlan.
     *
     * @param filter which purposes to retrieve
     * @return a list of purposes that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public static PurposeResponse getPurposes(String filter) throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        String url = String.format("%s%s?filter=%s", Atlan.getBaseUrl(), endpoint, ApiResource.urlEncode(filter));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", PurposeResponse.class, null);
    }

    /**
     * Retrieve all purposes defined in Atlan.
     *
     * @return a list of all the purposes in Atlan
     * @throws AtlanException on any API communication issue
     */
    public static PurposeResponse getAllPurposes() throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), endpoint);
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", PurposeResponse.class, null);
    }

    /**
     * Create a new purpose.
     *
     * @param purpose the details of the new purpose
     * @return details of the created purpose and user/group associations
     * @throws AtlanException on any API communication issue
     */
    public static Purpose createPurpose(Purpose purpose) throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), endpoint);
        WrappedPurpose wrapped =
                ApiResource.request(ApiResource.RequestMethod.POST, url, purpose, WrappedPurpose.class, null);
        if (wrapped != null) {
            return wrapped.getPurpose();
        } else {
            return null;
        }
    }

    /**
     * Update a purpose.
     *
     * @param id unique identifier (GUID) of the purpose to update
     * @param purpose the details to update on the purpose
     * @throws AtlanException on any API communication issue
     */
    public static void updatePurpose(String id, Purpose purpose) throws AtlanException {
        String url = String.format("%s%s/%s", Atlan.getBaseUrl(), endpoint, id);
        ApiResource.request(ApiResource.RequestMethod.POST, url, purpose, null, null);
    }

    /**
     * Delete a purpose.
     *
     * @param id unique identifier (GUID) of the purpose to delete
     * @throws AtlanException on any API communication issue
     */
    public static void deletePurpose(String id) throws AtlanException {
        String url = String.format("%s%s/%s", Atlan.getBaseUrl(), endpoint, id);
        ApiResource.request(ApiResource.RequestMethod.DELETE, url, "", null, null);
    }

    /**
     * Add the provided policy to the purpose with the specified ID.
     *
     * @param id unique identifier (GUID) of the purpose to add the policy to
     * @param policy the policy to add to the purpose
     * @return the policy that was added
     * @throws AtlanException on any API communication issue
     */
    public static AbstractPolicy addPolicyToPurpose(String id, AbstractPolicy policy) throws AtlanException {
        String url = String.format("%s%s/%s/policies", Atlan.getBaseUrl(), endpoint, id);
        PolicyRequest.PolicyRequestBuilder<?, ?> pr = PolicyRequest.builder().policy(policy);
        if (policy instanceof GlossaryPolicy) {
            pr = pr.type("glossaryPolicy");
        } else if (policy instanceof PurposeDataPolicy) {
            pr = pr.type("dataPolicy");
        } else if (policy instanceof PurposeMetadataPolicy) {
            pr = pr.type("metadataPolicy");
        }
        WrappedPolicy wrapped =
                ApiResource.request(ApiResource.RequestMethod.POST, url, pr.build(), WrappedPolicy.class, null);
        if (wrapped != null) {
            return wrapped.getPolicy();
        } else {
            return null;
        }
    }

    /** Request class for adding a policy. */
    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    static class PolicyRequest extends AtlanObject {
        /** Type of policy. */
        String type;
        /** Policy itself. */
        AbstractPolicy policy;
    }

    /**
     * Necessary for having a purpose object that extends ApiResource for API interactions.
     */
    @Data
    @JsonSerialize(using = WrappedPurposeSerializer.class)
    @JsonDeserialize(using = WrappedPurposeDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    private static final class WrappedPurpose extends ApiResource {
        Purpose purpose;

        public WrappedPurpose(Purpose purpose) {
            this.purpose = purpose;
        }
    }

    private static class WrappedPurposeDeserializer extends StdDeserializer<WrappedPurpose> {
        private static final long serialVersionUID = 2L;

        public WrappedPurposeDeserializer() {
            this(null);
        }

        public WrappedPurposeDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public WrappedPurpose deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            Purpose purpose = parser.getCodec().readValue(parser, new TypeReference<>() {});
            return new WrappedPurpose(purpose);
        }
    }

    private static class WrappedPurposeSerializer extends StdSerializer<WrappedPurpose> {
        private static final long serialVersionUID = 2L;

        public WrappedPurposeSerializer() {
            this(null);
        }

        public WrappedPurposeSerializer(Class<WrappedPurpose> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(WrappedPurpose wrappedPurpose, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            Purpose purpose = wrappedPurpose.getPurpose();
            Serde.mapper.writeValue(gen, purpose);
        }
    }
}
