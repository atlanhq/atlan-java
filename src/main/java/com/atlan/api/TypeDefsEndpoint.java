/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ConflictException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.enums.AtlanTypeCategory;
import com.atlan.model.typedefs.*;
import com.atlan.model.typedefs.TypeDefResponse;
import com.atlan.net.ApiResource;
import com.atlan.util.StringUtils;
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
import java.util.Locale;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * API endpoints for operating on Atlan's type definitions (in simple terms the underlying metadata model).
 */
public class TypeDefsEndpoint extends AtlasEndpoint {

    public static final Set<String> RESERVED_SERVICE_TYPES =
            Set.of("atlas_core", "atlan", "aws", "azure", "gcp", "google");
    private static final String endpoint = "/types/typedefs";
    private static final String endpoint_singular = "/types/typedef";
    private static final String endpoint_by_name = "/types/typedef/name";

    public TypeDefsEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Retrieves a list of the type definitions in Atlan.
     *
     * @param category of type definitions to retrieve
     * @return the requested list of type definitions
     * @throws AtlanException on any API communication issue
     */
    public TypeDefResponse list(AtlanTypeCategory category) throws AtlanException {
        String url = String.format(
                "%s%s",
                getBaseUrl(),
                String.format("%s?type=%s", endpoint, category.getValue().toLowerCase(Locale.ROOT)));
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", TypeDefResponse.class, null);
    }

    /**
     * Retrieves a specific type definition from Atlan.
     *
     * @param internalName the internal (hashed-string, if used) name of the type definition
     * @return details of that specific type definition
     * @throws AtlanException on any API communication issue
     */
    public TypeDef get(String internalName) throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint_by_name, internalName);
        WrappedTypeDef response =
                ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", WrappedTypeDef.class, null);
        return response.getTypeDef();
    }

    /**
     * Create a new type definition in Atlan.
     * Note: only custom metadata, enumerations, and Atlan tag type definitions are currently supported.
     * Furthermore, if any of these are created their respective cache will be force-refreshed.
     *
     * @param typeDef to create
     * @return the resulting type definition that was created
     * @throws AtlanException on any API communication issue
     */
    public TypeDefResponse create(TypeDef typeDef) throws AtlanException {
        TypeDefResponse response = null;
        if (typeDef != null) {
            switch (typeDef.getCategory()) {
                case ATLAN_TAG:
                case CUSTOM_METADATA:
                case ENUM:
                    response = createInternal(typeDef);
                    break;
                default:
                    throw new InvalidRequestException(
                            ErrorCode.UNABLE_TO_CREATE_TYPEDEF_CATEGORY,
                            typeDef.getCategory().getValue());
            }
        }
        return response;
    }

    /**
     * Create a new type definition in Atlan.
     * NOTE: INTERNAL USE ONLY. This will NOT work without specially-configured policies - use createTypeDef instead.
     * Furthermore, if any of Atlan tag, enum or custom metadata is created their respective cache will be force-refreshed.
     *
     * @param typeDef to create
     * @return the resulting type definition that was created
     * @throws AtlanException on any API communication issue
     * @see #create(TypeDef)
     */
    public TypeDefResponse createInternal(TypeDef typeDef) throws AtlanException {
        TypeDefResponse.TypeDefResponseBuilder builder = TypeDefResponse.builder();
        if (typeDef != null) {
            String serviceType = typeDef.getServiceType();
            if (serviceType != null && RESERVED_SERVICE_TYPES.contains(serviceType)) {
                throw new ConflictException(ErrorCode.RESERVED_SERVICE_TYPE, serviceType);
            }
            switch (typeDef.getCategory()) {
                case ATLAN_TAG:
                    builder.atlanTagDefs(List.of((AtlanTagDef) typeDef));
                    break;
                case CUSTOM_METADATA:
                    builder.customMetadataDefs(List.of((CustomMetadataDef) typeDef));
                    break;
                case ENUM:
                    builder.enumDefs(List.of((EnumDef) typeDef));
                    break;
                case STRUCT:
                    builder.structDefs(List.of((StructDef) typeDef));
                    break;
                case ENTITY:
                    builder.entityDefs(List.of((EntityDef) typeDef));
                    break;
                case RELATIONSHIP:
                    builder.relationshipDefs(List.of((RelationshipDef) typeDef));
                    break;
            }
            TypeDefResponse response = createInternal(builder);
            if (response != null) {
                if (!response.getAtlanTagDefs().isEmpty()) {
                    client.getAtlanTagCache().refreshCache();
                }
                if (!response.getCustomMetadataDefs().isEmpty()) {
                    client.getCustomMetadataCache().refreshCache();
                }
                if (!response.getEnumDefs().isEmpty()) {
                    client.getEnumCache().refreshCache();
                }
            }
        }
        // If there was no typedef provided, just return an empty response (noop)
        return builder.build();
    }

    private TypeDefResponse createInternal(TypeDefResponse.TypeDefResponseBuilder builder) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, builder.build(), TypeDefResponse.class, null);
    }

    /**
     * Update an existing type definition in Atlan.
     * Note: only custom metadata and Atlan tag type definitions are currently supported.
     * Furthermore, if any of these are updated their respective cache will be force-refreshed.
     *
     * @param typeDef to update
     * @return the resulting type definition that was updated
     * @throws AtlanException on any API communication issue
     */
    public TypeDefResponse update(TypeDef typeDef) throws AtlanException {
        TypeDefResponse response = null;
        if (typeDef != null) {
            switch (typeDef.getCategory()) {
                case ATLAN_TAG:
                case CUSTOM_METADATA:
                case ENUM:
                    response = updateInternal(typeDef);
                    break;
                default:
                    throw new InvalidRequestException(
                            ErrorCode.UNABLE_TO_UPDATE_TYPEDEF_CATEGORY,
                            typeDef.getCategory().getValue());
            }
        }
        return response;
    }

    /**
     * Update an existing type definition in Atlan.
     * Note: only custom metadata and Atlan tag type definitions are currently supported.
     * Furthermore, if any Atlan tag, enum or custom metadata is updated their respective cache will be force-refreshed.
     *
     * @param typeDef to update
     * @return the resulting type definition that was updated
     * @throws AtlanException on any API communication issue
     */
    public TypeDefResponse updateInternal(TypeDef typeDef) throws AtlanException {
        TypeDefResponse.TypeDefResponseBuilder builder = TypeDefResponse.builder();
        if (typeDef != null) {
            String serviceType = typeDef.getServiceType();
            if (serviceType != null && RESERVED_SERVICE_TYPES.contains(serviceType)) {
                throw new ConflictException(ErrorCode.RESERVED_SERVICE_TYPE, serviceType);
            }
            switch (typeDef.getCategory()) {
                case ATLAN_TAG:
                    builder.atlanTagDefs(List.of((AtlanTagDef) typeDef));
                    break;
                case CUSTOM_METADATA:
                    builder.customMetadataDefs(List.of((CustomMetadataDef) typeDef));
                    break;
                case ENUM:
                    builder.enumDefs(List.of((EnumDef) typeDef));
                    break;
                case STRUCT:
                    builder.structDefs(List.of((StructDef) typeDef));
                    break;
                case ENTITY:
                    builder.entityDefs(List.of((EntityDef) typeDef));
                    break;
                case RELATIONSHIP:
                    builder.relationshipDefs(List.of((RelationshipDef) typeDef));
                    break;
            }
            TypeDefResponse response = updateInternal(builder);
            if (response != null) {
                if (!response.getAtlanTagDefs().isEmpty()) {
                    client.getAtlanTagCache().refreshCache();
                }
                if (!response.getCustomMetadataDefs().isEmpty()) {
                    client.getCustomMetadataCache().refreshCache();
                }
                if (!response.getEnumDefs().isEmpty()) {
                    client.getEnumCache().refreshCache();
                }
            }
        }
        // If there was no typedef provided, just return an empty response (noop)
        return builder.build();
    }

    private TypeDefResponse updateInternal(TypeDefResponse.TypeDefResponseBuilder builder) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.PUT, url, builder.build(), TypeDefResponse.class, null);
    }

    /**
     * Delete the type definition.
     * Furthermore, if an Atlan tag, enum or custom metadata is deleted their respective cache will be force-refreshed
     *
     * @param internalName the internal hashed-string name of the type definition
     * @throws AtlanException on any API communication issue
     */
    public void purge(String internalName) throws AtlanException {
        TypeDef typeDef = get(internalName);
        String serviceType = typeDef.getServiceType();
        if (serviceType != null && RESERVED_SERVICE_TYPES.contains(serviceType)) {
            throw new ConflictException(ErrorCode.RESERVED_SERVICE_TYPE, serviceType);
        }
        String url = String.format(
                "%s%s",
                getBaseUrl(), String.format("%s/name/%s", endpoint_singular, StringUtils.encodeContent(internalName)));
        ApiResource.request(client, ApiResource.RequestMethod.DELETE, url, "", null, null);
        switch (typeDef.getCategory()) {
            case ATLAN_TAG:
                client.getAtlanTagCache().refreshCache();
                break;
            case ENUM:
                client.getEnumCache().refreshCache();
                break;
            case CUSTOM_METADATA:
                client.getCustomMetadataCache().refreshCache();
                break;
            default:
                // Do nothing, no other typedefs are cached
                break;
        }
    }

    /**
     * Necessary for having a typedef object that extends ApiResource for API interactions.
     */
    @Getter
    @JsonSerialize(using = WrappedTypeDefSerializer.class)
    @JsonDeserialize(using = WrappedTypeDefDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    private static final class WrappedTypeDef extends ApiResource {
        TypeDef typeDef;

        public WrappedTypeDef(TypeDef typeDef) {
            this.typeDef = typeDef;
        }
    }

    private static class WrappedTypeDefDeserializer extends StdDeserializer<WrappedTypeDef> {
        private static final long serialVersionUID = 2L;

        public WrappedTypeDefDeserializer() {
            this(null);
        }

        public WrappedTypeDefDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public WrappedTypeDef deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            TypeDef typeDef = parser.getCodec().readValue(parser, new TypeReference<>() {});
            return new WrappedTypeDef(typeDef);
        }
    }

    private static class WrappedTypeDefSerializer extends StdSerializer<WrappedTypeDef> {
        private static final long serialVersionUID = 2L;

        private final AtlanClient client;

        @SuppressWarnings("UnusedMethod")
        public WrappedTypeDefSerializer(AtlanClient client) {
            this(WrappedTypeDef.class, client);
        }

        public WrappedTypeDefSerializer(Class<WrappedTypeDef> t, AtlanClient client) {
            super(t);
            this.client = client;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(WrappedTypeDef wrappedTypeDef, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            TypeDef typeDef = wrappedTypeDef.getTypeDef();
            client.writeValue(gen, typeDef);
        }
    }
}
