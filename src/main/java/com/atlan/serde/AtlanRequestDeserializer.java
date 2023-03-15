/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.cache.ClassificationCache;
import com.atlan.cache.CustomMetadataCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.AtlanRequest;
import com.atlan.model.admin.AtlanRequestPayload;
import com.atlan.model.admin.ClassificationPayload;
import com.atlan.model.admin.CustomMetadataPayload;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.AtlanRequestType;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
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
            AtlanRequestType type = JacksonUtils.deserializeObject(root, "requestType", new TypeReference<>() {});
            JsonNode jsonPayload = root.get("payload");
            JsonNode jsonDestAttribute = root.get("destinationAttribute");
            String destinationAttribute = null;
            if (!jsonDestAttribute.isNull() && jsonDestAttribute.isTextual()) {
                destinationAttribute = jsonDestAttribute.asText();
            }
            AtlanRequestPayload payload = null;
            if (type != null) {
                switch (type) {
                    case CLASSIFICATION:
                        String typeName = JacksonUtils.deserializeString(jsonPayload, "typeName");
                        Boolean propagate = JacksonUtils.deserializeBoolean(jsonPayload, "propagate");
                        Boolean removeOnDelete =
                                JacksonUtils.deserializeBoolean(jsonPayload, "removePropagationsOnEntityDelete");
                        try {
                            String humanReadableClassification = ClassificationCache.getNameForId(typeName);
                            ClassificationPayload.ClassificationPayloadBuilder builder =
                                    ClassificationPayload.builder().typeName(humanReadableClassification);
                            if (propagate != null) {
                                builder = builder.propagate(propagate);
                            }
                            if (removeOnDelete != null) {
                                builder = builder.removePropagationsOnEntityDelete(removeOnDelete);
                            }
                            payload = builder.build();
                        } catch (AtlanException e) {
                            throw new IOException("Unable to find any classification with the ID: " + typeName, e);
                        }
                        break;
                    case CUSTOM_METADATA:
                        String cmId = jsonDestAttribute.asText();
                        CustomMetadataAttributes cma;
                        try {
                            destinationAttribute = CustomMetadataCache.getNameForId(cmId);
                            cma = CustomMetadataCache.getCustomMetadataAttributes(cmId, jsonPayload);
                        } catch (AtlanException e) {
                            throw new IOException(
                                    "Unable to translate custom metadata attributes for " + cmId + ": " + jsonPayload,
                                    e);
                        }
                        payload =
                                CustomMetadataPayload.builder().attributes(cma).build();
                        break;
                    default:
                        // Nothing to do for other types, where payload will anyway be empty
                        break;
                }
            }
            return AtlanRequest.builder()
                    .id(JacksonUtils.deserializeString(root, "id"))
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
                    .requestType(JacksonUtils.deserializeObject(root, "requestType", new TypeReference<>() {}))
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
                    .requestDenyUsers(
                            JacksonUtils.deserializeObject(root, "requestDenyUsers", new TypeReference<>() {}))
                    .requestDenyGroups(
                            JacksonUtils.deserializeObject(root, "requestDenyGroups", new TypeReference<>() {}))
                    .requestDenyRoles(
                            JacksonUtils.deserializeObject(root, "requestDenyRoles", new TypeReference<>() {}))
                    .destinationEntity(
                            JacksonUtils.deserializeObject(root, "destinationEntity", new TypeReference<>() {}))
                    .payload(payload)
                    .build();

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
    }
}
