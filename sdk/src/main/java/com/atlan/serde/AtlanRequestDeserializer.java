/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.*;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;

/**
 * Deserialization of all {@link AtlanRequest} objects.
 */
public class AtlanRequestDeserializer extends StdDeserializer<AtlanRequest> {

    private static final long serialVersionUID = 2L;

    private final transient AtlanClient client;

    public AtlanRequestDeserializer(AtlanClient client) {
        this(AtlanRequest.class, client);
    }

    public AtlanRequestDeserializer(Class<?> t, AtlanClient client) {
        super(t);
        this.client = client;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object deserializeWithType(
            JsonParser parser, DeserializationContext context, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(parser, context);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AtlanRequest deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        JsonNode root = parser.getCodec().readTree(parser);
        JsonNode requestType = root.get("requestType");

        if ((root.has("requestType") && (requestType == null || requestType.isNull()))) {
            // If the JSON has explicit null values, return those as explicit nulls rather than errors
            return null;
        } else if (requestType != null && requestType.isTextual()) {

            AtlanRequest.AtlanRequestBuilder<?, ?> builder = null;

            String type = JacksonUtils.deserializeString(root, "requestType");
            JsonNode jsonPayload = root.get("payload");
            JsonNode jsonDestAttribute = root.get("destinationAttribute");
            String destinationAttribute = null;
            if (jsonDestAttribute != null && !jsonDestAttribute.isNull() && jsonDestAttribute.isTextual()) {
                destinationAttribute = jsonDestAttribute.asText();
            }
            if (type != null) {
                switch (type) {
                    case AtlanTagRequest.REQUEST_TYPE:
                        String typeName = JacksonUtils.deserializeString(jsonPayload, "typeName");
                        Boolean propagate = JacksonUtils.deserializeBoolean(jsonPayload, "propagate");
                        Boolean removeOnDelete =
                                JacksonUtils.deserializeBoolean(jsonPayload, "removePropagationsOnEntityDelete");
                        String humanReadableAtlanTag;
                        try {
                            humanReadableAtlanTag = client.getAtlanTagCache().getNameForSid(typeName);
                        } catch (NotFoundException e) {
                            humanReadableAtlanTag = AtlanClient.DELETED_AUDIT_OBJECT;
                        } catch (AtlanException e) {
                            throw new IOException("Unable to translate Atlan tag with the ID: " + typeName, e);
                        }
                        if (humanReadableAtlanTag == null) {
                            humanReadableAtlanTag = AtlanClient.DELETED_AUDIT_OBJECT;
                        }
                        AtlanTagPayload.AtlanTagPayloadBuilder payloadBuilder =
                                AtlanTagPayload.builder().typeName(humanReadableAtlanTag);
                        if (propagate != null) {
                            payloadBuilder = payloadBuilder.propagate(propagate);
                        }
                        if (removeOnDelete != null) {
                            payloadBuilder = payloadBuilder.removePropagationsOnEntityDelete(removeOnDelete);
                        }
                        builder = AtlanTagRequest.builder().payload(payloadBuilder.build());
                        break;
                    case CustomMetadataRequest.REQUEST_TYPE:
                        String cmId = destinationAttribute;
                        CustomMetadataAttributes cma;
                        try {
                            destinationAttribute =
                                    client.getCustomMetadataCache().getNameForId(cmId);
                            cma = client.getCustomMetadataCache().getCustomMetadataAttributes(cmId, jsonPayload);
                        } catch (NotFoundException e) {
                            destinationAttribute = AtlanClient.DELETED_AUDIT_OBJECT;
                            cma = CustomMetadataAttributes.builder().build();
                        } catch (AtlanException e) {
                            throw new IOException(
                                    "Unable to translate custom metadata attributes for " + cmId + ": " + jsonPayload,
                                    e);
                        }
                        builder = CustomMetadataRequest.builder()
                                .payload(CustomMetadataPayload.builder()
                                        .attributes(cma)
                                        .build());
                        break;
                    case AttributeRequest.REQUEST_TYPE:
                        builder = AttributeRequest.builder();
                        break;
                    case TermLinkRequest.REQUEST_TYPE:
                        builder = TermLinkRequest.builder();
                        break;
                    default:
                        throw new IOException("Unknown request type " + type + ": " + jsonPayload);
                }
            }
            if (builder != null) {
                JsonNode destinationEntity = root.get("destinationEntity");
                JsonNode entityType = root.get("entityType");
                if (destinationEntity != null
                        && !destinationEntity.isNull()
                        && entityType != null
                        && !entityType.isNull()) {
                    // Inject typeName into the destinationEntity so that we can deserialize it
                    // appropriately
                    ((ObjectNode) destinationEntity).set("typeName", entityType);
                    builder.destinationEntity(client.convertValue(destinationEntity, new TypeReference<>() {}));
                }
                AtlanRequest request = builder.id(JacksonUtils.deserializeString(root, "id"))
                        .version(JacksonUtils.deserializeString(root, "version"))
                        .isActive(JacksonUtils.deserializeBoolean(root, "isActive"))
                        .createdAt(JacksonUtils.deserializeLong(root, "createdAt"))
                        .updatedAt(JacksonUtils.deserializeLong(root, "updatedAt"))
                        .createdBy(JacksonUtils.deserializeString(root, "createdBy"))
                        .tenantId(JacksonUtils.deserializeString(root, "tenantId"))
                        .sourceType(JacksonUtils.deserializeString(root, "sourceType"))
                        .sourceGuid(JacksonUtils.deserializeString(root, "sourceGuid"))
                        .sourceQualifiedName(JacksonUtils.deserializeString(root, "sourceQualifiedName"))
                        .sourceAttribute(JacksonUtils.deserializeString(root, "sourceAttribute"))
                        .destinationGuid(JacksonUtils.deserializeString(root, "destinationGuid"))
                        .destinationQualifiedName(JacksonUtils.deserializeString(root, "destinationQualifiedName"))
                        .destinationAttribute(destinationAttribute)
                        .destinationValue(JacksonUtils.deserializeString(root, "destinationValue"))
                        .destinationValueType(JacksonUtils.deserializeString(root, "destinationValueType"))
                        .entityType(JacksonUtils.deserializeString(root, "entityType"))
                        .requestType(JacksonUtils.deserializeString(root, "requestType"))
                        .approvedBy(JacksonUtils.deserializeString(root, "approvedBy"))
                        .rejectedBy(JacksonUtils.deserializeString(root, "rejectedBy"))
                        .status(JacksonUtils.deserializeObject(client, root, "status", new TypeReference<>() {}))
                        .message(JacksonUtils.deserializeString(root, "message"))
                        .approvalType(JacksonUtils.deserializeString(root, "approvalType"))
                        .hash(JacksonUtils.deserializeLong(root, "hash"))
                        .isDuplicate(JacksonUtils.deserializeBoolean(root, "isDuplicate"))
                        .destinationValueAction(JacksonUtils.deserializeString(root, "destinationValueAction"))
                        .requestApproverUsers(JacksonUtils.deserializeObject(
                                client, root, "requestApproverUsers", new TypeReference<>() {}))
                        .requestApproverGroups(JacksonUtils.deserializeObject(
                                client, root, "requestApproverGroups", new TypeReference<>() {}))
                        .requestApproverRoles(JacksonUtils.deserializeObject(
                                client, root, "requestApproverRoles", new TypeReference<>() {}))
                        .requestDenyUsers(JacksonUtils.deserializeObject(
                                client, root, "requestDenyUsers", new TypeReference<>() {}))
                        .requestDenyGroups(JacksonUtils.deserializeObject(
                                client, root, "requestDenyGroups", new TypeReference<>() {}))
                        .requestDenyRoles(JacksonUtils.deserializeObject(
                                client, root, "requestDenyRoles", new TypeReference<>() {}))
                        .build();
                request.setRawJsonObject(root);
                return request;
            }

            /* Unused.
            Object destinationValueArray;
            Object destinationValueObject;
            Object confidenceScore;
            Object botRunId;
            Object requestsBatch;
            Object assignedApprovers;
            Object accessStartDate;
            Object accessEndDate;
            Object sourceEntity;
            */

        } else {
            throw new IOException("Request type currently not handled: " + root);
        }
        return null;
    }
}
