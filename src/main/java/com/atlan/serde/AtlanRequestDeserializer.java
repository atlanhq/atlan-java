/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.ClassificationCache;
import com.atlan.cache.CustomMetadataCache;
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

    public AtlanRequestDeserializer() {
        this(null);
    }

    public AtlanRequestDeserializer(Class<?> t) {
        super(t);
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
                    case ClassificationRequest.REQUEST_TYPE:
                        String typeName = JacksonUtils.deserializeString(jsonPayload, "typeName");
                        Boolean propagate = JacksonUtils.deserializeBoolean(jsonPayload, "propagate");
                        Boolean removeOnDelete =
                                JacksonUtils.deserializeBoolean(jsonPayload, "removePropagationsOnEntityDelete");
                        String humanReadableClassification;
                        try {
                            humanReadableClassification = ClassificationCache.getNameForId(typeName);
                        } catch (NotFoundException e) {
                            humanReadableClassification = AtlanRequestSerializer.DELETED;
                        } catch (AtlanException e) {
                            throw new IOException("Unable to translate classification with the ID: " + typeName, e);
                        }
                        ClassificationPayload.ClassificationPayloadBuilder payloadBuilder =
                                ClassificationPayload.builder().typeName(humanReadableClassification);
                        if (propagate != null) {
                            payloadBuilder = payloadBuilder.propagate(propagate);
                        }
                        if (removeOnDelete != null) {
                            payloadBuilder = payloadBuilder.removePropagationsOnEntityDelete(removeOnDelete);
                        }
                        builder = ClassificationRequest.builder().payload(payloadBuilder.build());
                        break;
                    case CustomMetadataRequest.REQUEST_TYPE:
                        String cmId = destinationAttribute;
                        CustomMetadataAttributes cma;
                        try {
                            destinationAttribute = CustomMetadataCache.getNameForId(cmId);
                            cma = CustomMetadataCache.getCustomMetadataAttributes(cmId, jsonPayload);
                        } catch (NotFoundException e) {
                            destinationAttribute = AtlanRequestSerializer.DELETED;
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
                    builder = builder.destinationEntity(
                            Serde.mapper.readValue(destinationEntity.toString(), new TypeReference<>() {}));
                }
                return builder.id(JacksonUtils.deserializeString(root, "id"))
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
                        .status(JacksonUtils.deserializeObject(root, "status", new TypeReference<>() {}))
                        .message(JacksonUtils.deserializeString(root, "message"))
                        .approvalType(JacksonUtils.deserializeString(root, "approvalType"))
                        .hash(JacksonUtils.deserializeLong(root, "hash"))
                        .isDuplicate(JacksonUtils.deserializeBoolean(root, "isDuplicate"))
                        .destinationValueAction(JacksonUtils.deserializeString(root, "destinationValueAction"))
                        .requestApproverUsers(
                                JacksonUtils.deserializeObject(root, "requestApproverUsers", new TypeReference<>() {}))
                        .requestApproverGroups(
                                JacksonUtils.deserializeObject(root, "requestApproverGroups", new TypeReference<>() {}))
                        .requestApproverRoles(
                                JacksonUtils.deserializeObject(root, "requestApproverRoles", new TypeReference<>() {}))
                        .requestDenyUsers(
                                JacksonUtils.deserializeObject(root, "requestDenyUsers", new TypeReference<>() {}))
                        .requestDenyGroups(
                                JacksonUtils.deserializeObject(root, "requestDenyGroups", new TypeReference<>() {}))
                        .requestDenyRoles(
                                JacksonUtils.deserializeObject(root, "requestDenyRoles", new TypeReference<>() {}))
                        .build();
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
