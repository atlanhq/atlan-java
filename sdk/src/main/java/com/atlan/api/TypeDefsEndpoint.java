/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
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
import com.atlan.net.RequestOptions;
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
        return list(category, null);
    }

    /**
     * Retrieves a list of the type definitions in Atlan.
     *
     * @param category of type definitions to retrieve
     * @param options to override default client settings
     * @return the requested list of type definitions
     * @throws AtlanException on any API communication issue
     */
    public TypeDefResponse list(AtlanTypeCategory category, RequestOptions options) throws AtlanException {
        return list(List.of(category), options);
    }

    /**
     * Retrieves a list of the type definitions in Atlan.
     *
     * @param categories of type definitions to retrieve
     * @return the requested list of type definitions
     * @throws AtlanException on any API communication issue
     */
    public TypeDefResponse list(List<AtlanTypeCategory> categories) throws AtlanException {
        return list(categories, null);
    }

    /**
     * Retrieves a list of the type definitions in Atlan.
     *
     * @param categories of type definitions to retrieve
     * @param options to override default client settings
     * @return the requested list of type definitions
     * @throws AtlanException on any API communication issue
     */
    public TypeDefResponse list(List<AtlanTypeCategory> categories, RequestOptions options) throws AtlanException {
        StringBuilder url = new StringBuilder(String.format("%s%s", getBaseUrl(), String.format("%s", endpoint)));
        for (int i = 0; i < categories.size(); i++) {
            AtlanTypeCategory category = categories.get(i);
            if (i == 0) {
                url.append("?");
            } else {
                url.append("&");
            }
            url.append(String.format("type=%s", category.getValue().toLowerCase(Locale.ROOT)));
        }
        return ApiResource.request(
                client, ApiResource.RequestMethod.GET, url.toString(), "", TypeDefResponse.class, options);
    }

    /**
     * Retrieves a specific type definition from Atlan.
     *
     * @param internalName the internal (hashed-string, if used) name of the type definition
     * @return details of that specific type definition
     * @throws AtlanException on any API communication issue
     */
    public TypeDef get(String internalName) throws AtlanException {
        return get(internalName, null);
    }

    /**
     * Retrieves a specific type definition from Atlan.
     *
     * @param internalName the internal (hashed-string, if used) name of the type definition
     * @param options to override default client settings
     * @return details of that specific type definition
     * @throws AtlanException on any API communication issue
     */
    public TypeDef get(String internalName, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s", getBaseUrl(), endpoint_by_name, internalName);
        WrappedTypeDef response =
                ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", WrappedTypeDef.class, options);
        TypeDef typeDef = response.getTypeDef();
        typeDef.setRawJsonObject(response.getRawJsonObject());
        return typeDef;
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
    public synchronized TypeDefResponse create(TypeDef typeDef) throws AtlanException {
        return create(typeDef, null);
    }

    /**
     * Create a new type definition in Atlan.
     * Note: only custom metadata, enumerations, and Atlan tag type definitions are currently supported.
     * Furthermore, if any of these are created their respective cache will be force-refreshed.
     *
     * @param typeDef to create
     * @param options to override default client settings
     * @return the resulting type definition that was created
     * @throws AtlanException on any API communication issue
     */
    public synchronized TypeDefResponse create(TypeDef typeDef, RequestOptions options) throws AtlanException {
        return create(List.of(typeDef), options);
    }

    /**
     * Create new type definitions in Atlan.
     * Note: only custom metadata, enumerations, and Atlan tag type definitions are currently supported.
     * Furthermore, if any of these are created their respective cache will be force-refreshed.
     *
     * @param typeDefs to create
     * @return the resulting type definition that was created
     * @throws AtlanException on any API communication issue
     */
    public synchronized TypeDefResponse create(List<TypeDef> typeDefs) throws AtlanException {
        return create(typeDefs, null);
    }

    /**
     * Create new type definitions in Atlan.
     * Note: only custom metadata, enumerations, and Atlan tag type definitions are currently supported.
     * Furthermore, if any of these are created their respective cache will be force-refreshed.
     *
     * @param typeDefs to create
     * @param options to override default client settings
     * @return the resulting type definition that was created
     * @throws AtlanException on any API communication issue
     */
    public synchronized TypeDefResponse create(List<TypeDef> typeDefs, RequestOptions options) throws AtlanException {
        TypeDefResponse response = null;
        if (typeDefs != null) {
            for (TypeDef typeDef : typeDefs) {
                switch (typeDef.getCategory()) {
                    case ATLAN_TAG:
                    case CUSTOM_METADATA:
                    case ENUM:
                        // All good, skip to the next one
                        break;
                    default:
                        throw new InvalidRequestException(
                                ErrorCode.UNABLE_TO_CREATE_TYPEDEF_CATEGORY,
                                typeDef.getCategory().getValue());
                }
            }
            response = _create(typeDefs, options);
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
    public synchronized TypeDefResponse _create(TypeDef typeDef) throws AtlanException {
        return _create(typeDef, null);
    }

    /**
     * Create a new type definition in Atlan.
     * NOTE: INTERNAL USE ONLY. This will NOT work without specially-configured policies - use createTypeDef instead.
     * Furthermore, if any of Atlan tag, enum or custom metadata is created their respective cache will be force-refreshed.
     *
     * @param typeDef to create
     * @param options to override default client settings
     * @return the resulting type definition that was created
     * @throws AtlanException on any API communication issue
     * @see #create(TypeDef)
     */
    public synchronized TypeDefResponse _create(TypeDef typeDef, RequestOptions options) throws AtlanException {
        return _create(List.of(typeDef), options);
    }

    /**
     * Create new type definitions in Atlan.
     * NOTE: INTERNAL USE ONLY. This will NOT work without specially-configured policies - use createTypeDef instead.
     * Furthermore, if any of Atlan tag, enum or custom metadata is created their respective cache will be force-refreshed.
     *
     * @param typeDefs to create
     * @return the resulting type definition that was created
     * @throws AtlanException on any API communication issue
     * @see #create(TypeDef)
     */
    public synchronized TypeDefResponse _create(List<TypeDef> typeDefs) throws AtlanException {
        return _create(typeDefs, null);
    }

    /**
     * Create new type definitions in Atlan.
     * NOTE: INTERNAL USE ONLY. This will NOT work without specially-configured policies - use createTypeDef instead.
     * Furthermore, if any of Atlan tag, enum or custom metadata is created their respective cache will be force-refreshed.
     *
     * @param typeDefs to create
     * @param options to override default client settings
     * @return the resulting type definition that was created
     * @throws AtlanException on any API communication issue
     * @see #create(TypeDef)
     */
    public synchronized TypeDefResponse _create(List<TypeDef> typeDefs, RequestOptions options) throws AtlanException {
        if (typeDefs != null) {
            TypeDefResponse.TypeDefResponseBuilder builder = buildTypeDefList(typeDefs);
            TypeDefResponse response = _create(builder, options);
            if (response != null) {
                if (!response.getAtlanTagDefs().isEmpty()) {
                    client.getAtlanTagCache().forceRefresh();
                }
                if (!response.getCustomMetadataDefs().isEmpty()) {
                    client.getCustomMetadataCache().forceRefresh();
                }
                if (!response.getEnumDefs().isEmpty()) {
                    client.getEnumCache().refreshCache();
                }
                return response;
            }
        }
        // If there was no typedef provided, just return an empty response (noop)
        return TypeDefResponse.builder().build();
    }

    private synchronized TypeDefResponse _create(TypeDefResponse.TypeDefResponseBuilder builder, RequestOptions options)
            throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.POST, url, builder.build(), TypeDefResponse.class, options);
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
    public synchronized TypeDefResponse update(TypeDef typeDef) throws AtlanException {
        return update(typeDef, null);
    }

    /**
     * Update existing type definitions in Atlan.
     * Note: only custom metadata and Atlan tag type definitions are currently supported.
     * Furthermore, if any of these are updated their respective cache will be force-refreshed.
     *
     * @param typeDefs to update
     * @return the resulting type definition that was updated
     * @throws AtlanException on any API communication issue
     */
    public synchronized TypeDefResponse update(List<TypeDef> typeDefs) throws AtlanException {
        return update(typeDefs, null);
    }

    /**
     * Update an existing type definition in Atlan.
     * Note: only custom metadata and Atlan tag type definitions are currently supported.
     * Furthermore, if any of these are updated their respective cache will be force-refreshed.
     *
     * @param typeDef to update
     * @param options to override default client settings
     * @return the resulting type definition that was updated
     * @throws AtlanException on any API communication issue
     */
    public synchronized TypeDefResponse update(TypeDef typeDef, RequestOptions options) throws AtlanException {
        return update(List.of(typeDef), options);
    }

    /**
     * Update existing type definitions in Atlan.
     * Note: only custom metadata and Atlan tag type definitions are currently supported.
     * Furthermore, if any of these are updated their respective cache will be force-refreshed.
     *
     * @param typeDefs to update
     * @param options to override default client settings
     * @return the resulting type definition that was updated
     * @throws AtlanException on any API communication issue
     */
    public synchronized TypeDefResponse update(List<TypeDef> typeDefs, RequestOptions options) throws AtlanException {
        if (typeDefs != null) {
            for (TypeDef typeDef : typeDefs) {
                switch (typeDef.getCategory()) {
                    case ATLAN_TAG:
                    case CUSTOM_METADATA:
                    case ENUM:
                        // Do nothing, these are update-able
                        break;
                    default:
                        throw new InvalidRequestException(
                                ErrorCode.UNABLE_TO_UPDATE_TYPEDEF_CATEGORY,
                                typeDef.getCategory().getValue());
                }
            }
        }
        return _update(typeDefs, options);
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
    public synchronized TypeDefResponse _update(TypeDef typeDef) throws AtlanException {
        return _update(List.of(typeDef), null);
    }

    /**
     * Update existing type definitions in Atlan.
     * Note: only custom metadata and Atlan tag type definitions are currently supported.
     * Furthermore, if any Atlan tag, enum or custom metadata is updated their respective cache will be force-refreshed.
     *
     * @param typeDefs to update
     * @param options to override default client settings
     * @return the resulting type definition that was updated
     * @throws AtlanException on any API communication issue
     */
    public synchronized TypeDefResponse _update(List<TypeDef> typeDefs, RequestOptions options) throws AtlanException {
        if (typeDefs != null) {
            TypeDefResponse.TypeDefResponseBuilder builder = buildTypeDefList(typeDefs);
            TypeDefResponse response = _update(builder, options);
            if (response != null) {
                if (!response.getAtlanTagDefs().isEmpty()) {
                    client.getAtlanTagCache().forceRefresh();
                }
                if (!response.getCustomMetadataDefs().isEmpty()) {
                    client.getCustomMetadataCache().forceRefresh();
                }
                if (!response.getEnumDefs().isEmpty()) {
                    client.getEnumCache().refreshCache();
                }
                return response;
            }
        }
        // If there was no typedef provided, just return an empty response (noop)
        return TypeDefResponse.builder().build();
    }

    private synchronized TypeDefResponse.TypeDefResponseBuilder buildTypeDefList(List<TypeDef> typeDefs)
            throws ConflictException {
        TypeDefResponse.TypeDefResponseBuilder builder = TypeDefResponse.builder();
        for (TypeDef typeDef : typeDefs) {
            String serviceType = typeDef.getServiceType();
            if (serviceType != null && RESERVED_SERVICE_TYPES.contains(serviceType)) {
                throw new ConflictException(ErrorCode.RESERVED_SERVICE_TYPE, serviceType);
            }
            switch (typeDef.getCategory()) {
                case ATLAN_TAG:
                    builder.atlanTagDef((AtlanTagDef) typeDef);
                    break;
                case CUSTOM_METADATA:
                    builder.customMetadataDef((CustomMetadataDef) typeDef);
                    break;
                case ENUM:
                    builder.enumDef((EnumDef) typeDef);
                    break;
                case STRUCT:
                    builder.structDef((StructDef) typeDef);
                    break;
                case ENTITY:
                    builder.entityDef((EntityDef) typeDef);
                    break;
                case RELATIONSHIP:
                    builder.relationshipDef((RelationshipDef) typeDef);
                    break;
            }
        }
        return builder;
    }

    private synchronized TypeDefResponse _update(TypeDefResponse.TypeDefResponseBuilder builder, RequestOptions options)
            throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        return ApiResource.request(
                client, ApiResource.RequestMethod.PUT, url, builder.build(), TypeDefResponse.class, options);
    }

    /**
     * Delete the type definition.
     * Furthermore, if an Atlan tag, enum or custom metadata is deleted their respective cache will be force-refreshed
     *
     * @param internalName the internal hashed-string name of the type definition
     * @throws AtlanException on any API communication issue
     */
    public synchronized void purge(String internalName) throws AtlanException {
        purge(internalName, null);
    }

    /**
     * Delete the type definition.
     * Furthermore, if an Atlan tag, enum or custom metadata is deleted their respective cache will be force-refreshed
     *
     * @param internalName the internal hashed-string name of the type definition
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public synchronized void purge(String internalName, RequestOptions options) throws AtlanException {
        TypeDef typeDef = get(internalName);
        String serviceType = typeDef.getServiceType();
        if (serviceType != null && RESERVED_SERVICE_TYPES.contains(serviceType)) {
            throw new ConflictException(ErrorCode.RESERVED_SERVICE_TYPE, serviceType);
        }
        String url = String.format(
                "%s%s",
                getBaseUrl(), String.format("%s/name/%s", endpoint_singular, StringUtils.encodeContent(internalName)));
        ApiResource.request(client, ApiResource.RequestMethod.DELETE, url, "", null, options);
        switch (typeDef.getCategory()) {
            case ATLAN_TAG:
                client.getAtlanTagCache().forceRefresh();
                break;
            case ENUM:
                client.getEnumCache().refreshCache();
                break;
            case CUSTOM_METADATA:
                client.getCustomMetadataCache().forceRefresh();
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
        private static final long serialVersionUID = 2L;

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
