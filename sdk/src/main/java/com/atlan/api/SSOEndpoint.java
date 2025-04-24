/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.api;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.admin.AtlanGroup;
import com.atlan.model.admin.SSOMapping;
import com.atlan.model.admin.SSOProviderRequest;
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
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * API endpoints for operating on Atlan's SSO configuration.
 */
public class SSOEndpoint extends HeraclesEndpoint {

    private static final String endpoint = "/idp";

    public static final String GROUP_MAPPER_ATTRIBUTE = "memberOf";
    public static final String GROUP_MAPPER_SYNC_MODE = "FORCE";
    public static final String IDP_GROUP_MAPPER = "saml-group-idp-mapper";

    public SSOEndpoint(AtlanClient client) {
        super(client);
    }

    /**
     * Configure an SSO provider.
     *
     * @param request details of the configuration for an SSO provider
     * @throws AtlanException on any API communication issue
     */
    public void configure(SSOProviderRequest request) throws AtlanException {
        configure(request, null);
    }

    /**
     * Configure an SSO provider.
     *
     * @param request details of the configuration for an SSO provider
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void configure(SSOProviderRequest request, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s", getBaseUrl(), endpoint);
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, request, options);
    }

    /**
     * Creates a new Atlan SSO mapping.
     *
     * @param ssoAlias name of the SSO provider
     * @param mapping details of the mapping to create
     * @return created SSO mapping
     * @throws AtlanException on any API communication issue
     */
    public SSOMapping createMapping(String ssoAlias, SSOMapping mapping) throws AtlanException {
        return createMapping(ssoAlias, mapping, null);
    }

    /**
     * Creates a new Atlan SSO mapping.
     *
     * @param ssoAlias name of the SSO provider
     * @param mapping details of the mapping to create
     * @param options to override default client settings
     * @return created SSO mapping
     * @throws AtlanException on any API communication issue
     */
    public SSOMapping createMapping(String ssoAlias, SSOMapping mapping, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s/mappers", getBaseUrl(), endpoint, ssoAlias);
        return ApiResource.request(client, ApiResource.RequestMethod.POST, url, mapping, SSOMapping.class, options);
    }

    /**
     * Retrieves all existing Atlan SSO group mappings.
     *
     * @param ssoAlias name of the SSO provider
     * @return list of existing SSO group mappings
     * @throws AtlanException on any API communication issue
     */
    public List<SSOMapping> listGroupMappings(String ssoAlias) throws AtlanException {
        return listGroupMappings(ssoAlias, null);
    }

    /**
     * Retrieves all existing Atlan SSO group mappings.
     *
     * @param ssoAlias name of the SSO provider
     * @param options to override default client settings
     * @return list of existing SSO group mappings
     * @throws AtlanException on any API communication issue
     */
    public List<SSOMapping> listGroupMappings(String ssoAlias, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s/mappers", getBaseUrl(), endpoint, ssoAlias);
        WrappedMapping mapping =
                ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", WrappedMapping.class, options);
        return mapping != null ? mapping.getMappings() : null;
    }

    /**
     * Retrieves an existing Atlan SSO group mapping.
     *
     * @param ssoAlias name of the SSO provider
     * @param groupMapId existing SSO group mapping identifier
     * @return existing SSO group mapping
     * @throws AtlanException on any API communication issue
     */
    public SSOMapping getGroupMapping(String ssoAlias, String groupMapId) throws AtlanException {
        return getGroupMapping(ssoAlias, groupMapId, null);
    }

    /**
     * Retrieves an existing Atlan SSO group mapping.
     *
     * @param ssoAlias name of the SSO provider
     * @param groupMapId existing SSO group mapping identifier
     * @param options to override default client settings
     * @return existing SSO group mapping
     * @throws AtlanException on any API communication issue
     */
    public SSOMapping getGroupMapping(String ssoAlias, String groupMapId, RequestOptions options)
            throws AtlanException {
        String url = String.format("%s%s/%s/mappers/%s", getBaseUrl(), endpoint, ssoAlias, groupMapId);
        return ApiResource.request(client, ApiResource.RequestMethod.GET, url, "", SSOMapping.class, options);
    }

    /**
     * Creates a new Atlan SSO group mapping.
     *
     * @param ssoAlias name of the SSO provider
     * @param group existing Atlan group
     * @param ssoGroupName name of the SSO group
     * @return created SSO group mapping
     * @throws AtlanException on any API communication issue
     */
    public SSOMapping createGroupMapping(String ssoAlias, AtlanGroup group, String ssoGroupName) throws AtlanException {
        return createGroupMapping(ssoAlias, group, ssoGroupName, null);
    }

    /**
     * Creates a new Atlan SSO group mapping.
     *
     * @param ssoAlias name of the SSO provider
     * @param group existing Atlan group
     * @param ssoGroupName name of the SSO group
     * @param options to override default client settings
     * @return created SSO group mapping
     * @throws AtlanException on any API communication issue
     */
    public SSOMapping createGroupMapping(String ssoAlias, AtlanGroup group, String ssoGroupName, RequestOptions options)
            throws AtlanException {
        checkExistingGroupMappings(ssoAlias, group);
        SSOMapping request = SSOMapping.builder()
                .name(generateGroupMappingName(group.getId()))
                .config(SSOMapping.Config.builder()
                        .attributes("[]")
                        .syncMode(GROUP_MAPPER_SYNC_MODE)
                        .attributeValuesRegex("")
                        .attributeName(GROUP_MAPPER_ATTRIBUTE)
                        .attributeValue(ssoGroupName)
                        .groupName(group.getName())
                        .build())
                .identityProviderAlias(ssoAlias)
                .identityProviderMapper(IDP_GROUP_MAPPER)
                .build();
        return createMapping(ssoAlias, request, options);
    }

    /**
     * Updates an existing Atlan SSO group mapping.
     *
     * @param ssoAlias name of the SSO provider
     * @param group existing Atlan group
     * @param groupMapId existing SSO group mapping identifier
     * @param ssoGroupName name of the SSO group
     * @return created SSO group mapping
     * @throws AtlanException on any API communication issue
     */
    public SSOMapping updateGroupMapping(String ssoAlias, AtlanGroup group, String groupMapId, String ssoGroupName)
            throws AtlanException {
        return updateGroupMapping(ssoAlias, group, groupMapId, ssoGroupName, null);
    }

    /**
     * Updates an existing Atlan SSO group mapping.
     *
     * @param ssoAlias name of the SSO provider
     * @param group existing Atlan group
     * @param groupMapId existing SSO group mapping identifier
     * @param ssoGroupName name of the SSO group
     * @param options to override default client settings
     * @return created SSO group mapping
     * @throws AtlanException on any API communication issue
     */
    public SSOMapping updateGroupMapping(
            String ssoAlias, AtlanGroup group, String groupMapId, String ssoGroupName, RequestOptions options)
            throws AtlanException {
        SSOMapping request = SSOMapping.builder()
                .id(groupMapId)
                .config(SSOMapping.Config.builder()
                        .attributes("[]")
                        .syncMode(GROUP_MAPPER_SYNC_MODE)
                        .attributeName(GROUP_MAPPER_ATTRIBUTE)
                        .attributeValue(ssoGroupName)
                        .groupName(group.getName())
                        .build())
                .identityProviderAlias(ssoAlias)
                .identityProviderMapper(IDP_GROUP_MAPPER)
                .build();
        String url = String.format("%s%s/%s/mappers/%s", getBaseUrl(), endpoint, ssoAlias, groupMapId);
        return ApiResource.request(client, ApiResource.RequestMethod.POST, url, request, SSOMapping.class, options);
    }

    /**
     * Delete an existing Atlan SSO group mapping.
     *
     * @param ssoAlias name of the SSO provider
     * @param groupMapId existing SSO group mapping identifier
     * @throws AtlanException on any API communication issue
     */
    public void deleteGroupMapping(String ssoAlias, String groupMapId) throws AtlanException {
        deleteGroupMapping(ssoAlias, groupMapId, null);
    }

    /**
     * Delete an existing Atlan SSO group mapping.
     *
     * @param ssoAlias name of the SSO provider
     * @param groupMapId existing SSO group mapping identifier
     * @param options to override default client settings
     * @throws AtlanException on any API communication issue
     */
    public void deleteGroupMapping(String ssoAlias, String groupMapId, RequestOptions options) throws AtlanException {
        String url = String.format("%s%s/%s/mappers/%s/delete", getBaseUrl(), endpoint, ssoAlias, groupMapId);
        ApiResource.request(client, ApiResource.RequestMethod.POST, url, "", options);
    }

    /**
     * Check if an SSO group mapping already exists in Atlan.
     * This is necessary to avoid duplicate group mappings with the same configuration due to
     * a unique name generated upon each creation.
     *
     * @param ssoAlias name of the SSO provider
     * @param group existing Atlan group
     * @throws AtlanException on any API communication issue
     */
    private void checkExistingGroupMappings(String ssoAlias, AtlanGroup group) throws AtlanException {
        List<SSOMapping> existing = listGroupMappings(ssoAlias);
        if (existing != null) {
            for (SSOMapping mapping : existing) {
                if (mapping.getName() != null
                        && group != null
                        && mapping.getName().contains(group.getId())) {
                    throw new InvalidRequestException(
                            ErrorCode.SSO_GROUP_MAPPING_ALREADY_EXISTS,
                            group.getAlias(),
                            mapping.getConfig().getAttributeValue());
                }
            }
        }
    }

    /**
     * Generate a unique name for the mapping.
     *
     * @param atlanGroupId unique identifier (GUID) of the Atlan group
     * @return a unique name for the mapping
     */
    private String generateGroupMappingName(String atlanGroupId) {
        return atlanGroupId + "--" + System.currentTimeMillis();
    }

    /**
     * Necessary for handling responses that are directly-wrapped lists of SSO mappings.
     */
    @Getter
    @JsonSerialize(using = WrappedMappingSerializer.class)
    @JsonDeserialize(using = WrappedMappingDeserializer.class)
    @EqualsAndHashCode(callSuper = false)
    @SuppressWarnings("serial")
    private static final class WrappedMapping extends ApiResource {
        private static final long serialVersionUID = 2L;

        List<SSOMapping> mappings;

        public WrappedMapping(List<SSOMapping> mappings) {
            this.mappings = mappings;
        }
    }

    private static class WrappedMappingDeserializer extends StdDeserializer<WrappedMapping> {
        private static final long serialVersionUID = 2L;

        public WrappedMappingDeserializer() {
            this(WrappedMapping.class);
        }

        public WrappedMappingDeserializer(Class<?> t) {
            super(t);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public WrappedMapping deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            List<SSOMapping> mappings = parser.getCodec().readValue(parser, new TypeReference<>() {});
            return new WrappedMapping(mappings);
        }
    }

    private static class WrappedMappingSerializer extends StdSerializer<WrappedMapping> {
        private static final long serialVersionUID = 2L;

        private final transient AtlanClient client;

        @SuppressWarnings("UnusedMethod")
        public WrappedMappingSerializer(AtlanClient client) {
            this(WrappedMapping.class, client);
        }

        public WrappedMappingSerializer(Class<WrappedMapping> t, AtlanClient client) {
            super(t);
            this.client = client;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(WrappedMapping wrappedMapping, JsonGenerator gen, SerializerProvider sp)
                throws IOException, JsonProcessingException {
            List<SSOMapping> mappings = wrappedMapping.getMappings();
            client.writeValue(gen, mappings);
        }
    }
}
