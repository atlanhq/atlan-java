/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
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

/**
 * API endpoints for interacting with Atlan's personas.
 */
public class PersonasEndpoint {

    private static final String endpoint = "/api/service/personas";

    // TODO: eventually provide a rich RQL object for the filter

    /**
     * Retrieves a list of the personas defined in Atlan.
     *
     * @param filter which personas to retrieve
     * @param sort property by which to sort the results
     * @param count whether to return the total number of records (true) or not (false)
     * @param offset starting point for results to return, for paging
     * @param limit maximum number of results to be returned
     * @return a list of personas that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public static PersonaResponse getPersonas(String filter, String sort, boolean count, int offset, int limit)
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
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", PersonaResponse.class, null);
    }

    /**
     * Retrieves a list of the personas defined in Atlan.
     *
     * @param filter which personas to retrieve
     * @return a list of personas that match the provided criteria
     * @throws AtlanException on any API communication issue
     */
    public static PersonaResponse getPersonas(String filter) throws AtlanException {
        if (filter == null) {
            filter = "";
        }
        String url = String.format("%s%s?filter=%s", Atlan.getBaseUrl(), endpoint, ApiResource.urlEncode(filter));
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", PersonaResponse.class, null);
    }

    /**
     * Retrieve all personas defined in Atlan.
     *
     * @return a list of all the personas in Atlan
     * @throws AtlanException on any API communication issue
     */
    public static PersonaResponse getAllPersonas() throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), endpoint);
        return ApiResource.request(ApiResource.RequestMethod.GET, url, "", PersonaResponse.class, null);
    }

    /**
     * Create a new persona.
     *
     * @param persona the details of the new persona
     * @return details of the created persona and user/group associations
     * @throws AtlanException on any API communication issue
     */
    public static Persona createPersona(Persona persona) throws AtlanException {
        String url = String.format("%s%s", Atlan.getBaseUrl(), endpoint);
        WrappedPersona wrapped =
                ApiResource.request(ApiResource.RequestMethod.POST, url, persona, WrappedPersona.class, null);
        if (wrapped != null) {
            return wrapped.getPersona();
        } else {
            return null;
        }
    }

    /**
     * Update a persona.
     *
     * @param id unique identifier (GUID) of the persona to update
     * @param persona the details to update on the persona
     * @throws AtlanException on any API communication issue
     */
    public static void updatePersona(String id, Persona persona) throws AtlanException {
        String url = String.format("%s%s/%s", Atlan.getBaseUrl(), endpoint, id);
        ApiResource.request(ApiResource.RequestMethod.POST, url, persona, null, null);
    }

    /**
     * Delete a persona.
     *
     * @param id unique identifier (GUID) of the persona to delete
     * @throws AtlanException on any API communication issue
     */
    public static void deletePersona(String id) throws AtlanException {
        String url = String.format("%s%s/%s", Atlan.getBaseUrl(), endpoint, id);
        ApiResource.request(ApiResource.RequestMethod.DELETE, url, "", null, null);
    }

    /**
     * Necessary for having a persona object that extends ApiResource for API interactions.
     */
    @Data
    @JsonSerialize(using = WrappedPersonaSerializer.class)
    @JsonDeserialize(using = WrappedPersonaDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    private static final class WrappedPersona extends ApiResource {
        Persona persona;

        public WrappedPersona(Persona persona) {
            this.persona = persona;
        }
    }

    private static class WrappedPersonaDeserializer extends StdDeserializer<WrappedPersona> {
        private static final long serialVersionUID = 2L;

        public WrappedPersonaDeserializer() {
            this(null);
        }

        public WrappedPersonaDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public WrappedPersona deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            Persona persona = parser.getCodec().readValue(parser, new TypeReference<>() {});
            return new WrappedPersona(persona);
        }
    }

    private static class WrappedPersonaSerializer extends StdSerializer<WrappedPersona> {
        private static final long serialVersionUID = 2L;

        public WrappedPersonaSerializer() {
            this(null);
        }

        public WrappedPersonaSerializer(Class<WrappedPersona> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(WrappedPersona unwrappedPersona, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            Persona persona = unwrappedPersona.getPersona();
            Serde.mapper.writeValue(gen, persona);
        }
    }
}
