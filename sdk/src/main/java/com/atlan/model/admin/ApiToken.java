/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.core.AtlanObject;
import com.atlan.serde.StringToSetDeserializer;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ApiToken extends AtlanObject {

    private static final long serialVersionUID = 2L;
    public static final String API_USERNAME_PREFIX = "service-account-";

    /** Unique identifier (GUID) of the API token. */
    @Getter
    String id;

    /** Unique client identifier (GUID) of the API token. */
    String clientId;

    /** Human-readable name provided when creating the token. */
    String displayName;

    /** Detailed characteristics of the API token. */
    @Getter
    ApiTokenAttributes attributes;

    public String getClientId() {
        if (clientId != null) {
            return clientId;
        } else if (attributes != null) {
            return attributes.getClientId();
        }
        return null;
    }

    public String getDisplayName() {
        if (displayName != null) {
            return displayName;
        } else if (attributes != null) {
            return attributes.getDisplayName();
        }
        return null;
    }

    /**
     * Retrieve the username that represents this API token.
     * This name can be used to assign the token as a user to objects that require a user,
     * such as policies.
     *
     * @return a username for this API token
     */
    @JsonIgnore
    public String getApiTokenUsername() {
        return API_USERNAME_PREFIX + getClientId();
    }

    /**
     * Create a new API token that never expires and is not assigned to any personas.
     *
     * @param client connectivity to the Atlan tenant on which to create the new API token
     * @param displayName readable name for the API token
     * @return the API token details
     * @throws AtlanException on any API communication issues
     */
    public static ApiToken create(AtlanClient client, String displayName) throws AtlanException {
        return create(client, displayName, null, null, -1L);
    }

    /**
     * Create a new API token with the provided details.
     *
     * @param client connectivity to the Atlan tenant on which to create the new API token
     * @param displayName readable name for the API token
     * @param description explanation of the API token
     * @param personas unique names (qualifiedNames) of personas that should be linked to the token
     * @param validity time in seconds after which the token should expire (negative numbers are treated as a token should never expire)
     * @return the API token details
     * @throws AtlanException on any API communication issues
     */
    public static ApiToken create(
            AtlanClient client, String displayName, String description, Set<String> personas, Long validity)
            throws AtlanException {
        return client.apiTokens.create(displayName, description, personas, validity);
    }

    /**
     * Retrieves the API token with a name that exactly matches the provided string.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the API token
     * @param displayName name (as it appears in the UI) by which to retrieve the API token
     * @return the API token whose name (in the UI) contains the provided string, or null if there is none
     * @throws AtlanException on any error during API invocation
     */
    public static ApiToken retrieveByName(AtlanClient client, String displayName) throws AtlanException {
        return client.apiTokens.get(displayName);
    }

    /**
     * Sends this API token to Atlan to update it in Atlan.
     *
     * @param client connectivity to the Atlan tenant on which to update the API token
     * @return the updated API token
     * @throws AtlanException on any error during API invocation
     */
    public ApiToken update(AtlanClient client) throws AtlanException {
        if (this.id == null || this.id.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_TOKEN_ID);
        }
        if (this.displayName == null || this.displayName.isEmpty()) {
            throw new InvalidRequestException(ErrorCode.MISSING_TOKEN_NAME);
        }
        String description = null;
        Set<String> personas = null;
        if (attributes != null) {
            description = attributes.getDescription();
            if (attributes.getPersonas() != null) {
                personas = new HashSet<>();
                for (ApiTokenPersona persona : attributes.getPersonas()) {
                    personas.add(persona.getPersonaQualifiedName());
                }
            }
        }
        return client.apiTokens.update(this.id, this.displayName, description, personas);
    }

    /**
     * Delete (purge) the API token with the provided GUID.
     *
     * @param client connectivity to the Atlan tenant from which to remove the API token
     * @param guid unique identifier (GUID) of the API token to hard-delete
     * @throws AtlanException on any API communication issues
     */
    public static void delete(AtlanClient client, String guid) throws AtlanException {
        client.apiTokens.purge(guid);
    }

    @Getter
    @JsonSerialize(using = ApiTokenAttributesSerializer.class)
    @JsonDeserialize(using = ApiTokenAttributesDeserializer.class)
    @Builder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    @SuppressWarnings("serial")
    public static final class ApiTokenAttributes extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Time, in seconds, from createdAt after which the token will expire. */
        @JsonProperty("access.token.lifespan")
        Long accessTokenLifespan;

        /** The actual API token that can be used as a bearer token. */
        final String accessToken;

        /** Unique client identifier (GUID) of the API token. */
        String clientId;

        /** Epoch time, in milliseconds, at which the API token was created. */
        Long createdAt;

        /** User who created the API token. */
        String createdBy;

        /** Explanation of the API token. */
        String description;

        /** Human-readable name provided when creating the token. */
        String displayName;

        /** Personas associated with the API token. */
        @JsonProperty("personaQualifiedName")
        @Singular
        SortedSet<ApiTokenPersona> personas;

        /**
         * This was a possible future placeholder for purposes associated with the token,
         * but no longer exists on payloads. It is left here purely for any existing code
         * that may have referenced it, but should be removed from that code as it will be
         * removed in the next major release from this object.
         */
        @JsonIgnore
        @Deprecated
        String purposes;

        /** Detailed permissions given to the API token. */
        @Singular
        SortedSet<String> workspacePermissions;
    }

    @Getter
    @Jacksonized
    @Builder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class ApiTokenPersona extends AtlanObject implements Comparable<ApiTokenPersona> {
        private static final long serialVersionUID = 2L;

        private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
        private static final Comparator<ApiTokenPersona> personaComparator = Comparator.comparing(
                        ApiTokenPersona::getId, stringComparator)
                .thenComparing(ApiTokenPersona::getPersona, stringComparator)
                .thenComparing(ApiTokenPersona::getPersonaQualifiedName, stringComparator);

        /** Unique identifier (GUID) of the linked persona. */
        String id;
        /** Unique name of the linked persona. */
        String persona;
        /** Unique name (qualifiedName) of the linked persona. */
        String personaQualifiedName;

        public static ApiTokenPersona of(String id, String persona, String personaQualifiedName) {
            return ApiTokenPersona.builder()
                    .id(id)
                    .persona(persona)
                    .personaQualifiedName(personaQualifiedName)
                    .build();
        }

        /** {@inheritDoc} */
        @Override
        public int compareTo(ApiTokenPersona o) {
            return personaComparator.compare(this, o);
        }
    }

    /**
     * Custom serialization of {@link ApiTokenAttributes} objects.
     * In particular, this translates string-based numeric values into strings.
     */
    static class ApiTokenAttributesSerializer extends StdSerializer<ApiTokenAttributes> {
        private static final long serialVersionUID = 2L;

        public ApiTokenAttributesSerializer() {
            this(null);
        }

        public ApiTokenAttributesSerializer(Class<ApiTokenAttributes> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serializeWithType(
                ApiTokenAttributes value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
                throws IOException {
            serialize(value, gen, serializers);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(ApiTokenAttributes ata, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            gen.writeStartObject();
            JacksonUtils.serializeString(gen, "access.token.lifespan", "" + ata.getAccessTokenLifespan());
            JacksonUtils.serializeString(gen, "accessToken", ata.getAccessToken());
            JacksonUtils.serializeString(gen, "clientId", ata.getClientId());
            JacksonUtils.serializeString(gen, "createdAt", "" + ata.getCreatedAt());
            JacksonUtils.serializeString(gen, "createdBy", ata.getCreatedBy());
            JacksonUtils.serializeString(gen, "description", ata.getDescription());
            JacksonUtils.serializeString(gen, "displayName", ata.getDisplayName());
            JacksonUtils.serializeObject(gen, "personaQualifiedName", ata.getPersonas());
            JacksonUtils.serializeObject(gen, "workspacePermissions", ata.getWorkspacePermissions());
            gen.writeEndObject();
        }
    }

    /**
     * Custom deserialization of {@link ApiTokenAttributes} objects.
     * In particular, this translates from endpoints that represent lists as strings as well as those
     * that represent lists as actual lists.
     */
    static class ApiTokenAttributesDeserializer extends StdDeserializer<ApiTokenAttributes> {
        private static final long serialVersionUID = 2L;

        private final transient AtlanClient client;

        public ApiTokenAttributesDeserializer(AtlanClient client) {
            super(ApiToken.class);
            this.client = client;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object deserializeWithType(
                JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer)
                throws IOException {
            return deserialize(parser, context);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ApiTokenAttributes deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            JsonNode root = parser.getCodec().readTree(parser);

            Set<ApiTokenPersona> personas = new TreeSet<>();
            JsonNode personasJson = root.get("personaQualifiedName");
            if (personasJson.isTextual()) {
                Set<String> personaNames = StringToSetDeserializer.deserialize(client, personasJson.asText());
                for (String name : personaNames) {
                    personas.add(ApiTokenPersona.of(null, name, null));
                }
            } else if (personasJson.isArray()) {
                personas =
                        JacksonUtils.deserializeObject(client, root, "personaQualifiedName", new TypeReference<>() {});
            }

            Set<String> workspacePermissions = new TreeSet<>();
            JsonNode workspacePermissionsJson = root.get("workspacePermissions");
            if (workspacePermissionsJson.isTextual()) {
                workspacePermissions = StringToSetDeserializer.deserialize(client, workspacePermissionsJson.asText());
            } else if (workspacePermissionsJson.isArray()) {
                workspacePermissions =
                        JacksonUtils.deserializeObject(client, root, "workspacePermissions", new TypeReference<>() {});
            }

            ApiTokenAttributes result = ApiTokenAttributes.builder()
                    .accessTokenLifespan(JacksonUtils.deserializeLong(root, "access.token.lifespan"))
                    .accessToken(JacksonUtils.deserializeString(root, "accessToken"))
                    .clientId(JacksonUtils.deserializeString(root, "clientId"))
                    .createdAt(JacksonUtils.deserializeLong(root, "createdAt"))
                    .createdBy(JacksonUtils.deserializeString(root, "createdBy"))
                    .description(JacksonUtils.deserializeString(root, "description"))
                    .displayName(JacksonUtils.deserializeString(root, "displayName"))
                    .personas(personas)
                    .workspacePermissions(workspacePermissions)
                    .build();
            result.setRawJsonObject(root);
            return result;
        }
    }
}
