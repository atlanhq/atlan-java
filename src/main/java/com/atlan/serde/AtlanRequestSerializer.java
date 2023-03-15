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
import com.atlan.model.enums.AtlanRequestType;
import com.atlan.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Serialization of all {@link AtlanRequest} objects.
 * This custom serialization is necessary to create some specific aspects of complexity in Atlan's payloads:
 * <ul>
 *     <li>The nested <code>payload</code> structures, which varies depending on classification and custom metadata details.</li>
 * </ul>
 */
public class AtlanRequestSerializer extends StdSerializer<AtlanRequest> {
    private static final long serialVersionUID = 2L;

    public AtlanRequestSerializer() {
        this(null);
    }

    public AtlanRequestSerializer(Class<AtlanRequest> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serializeWithType(
            AtlanRequest value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer)
            throws IOException {
        serialize(value, gen, serializers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void serialize(AtlanRequest request, JsonGenerator gen, SerializerProvider sp)
            throws IOException, JsonProcessingException {

        AtlanRequestType requestType = request.getRequestType();
        String destinationAttribute = request.getDestinationAttribute();
        AtlanRequestPayload untranslatedPayload = request.getPayload();
        Object translatedPayload = null;

        if (untranslatedPayload instanceof ClassificationPayload) {
            ClassificationPayload cls = (ClassificationPayload) untranslatedPayload;
            String clsName = cls.getTypeName();
            try {
                String clsId = ClassificationCache.getIdForName(clsName);
                translatedPayload = cls.toBuilder().typeName(clsId).build();
            } catch (AtlanException e) {
                throw new IOException("Unable to find classification with name: " + clsName, e);
            }
        } else if (untranslatedPayload instanceof CustomMetadataPayload) {
            CustomMetadataPayload cm = (CustomMetadataPayload) untranslatedPayload;
            String cmName = destinationAttribute;
            try {
                destinationAttribute = CustomMetadataCache.getIdForName(cmName);
                Map<String, Object> attrValues = new HashMap<>();
                CustomMetadataCache.getAttributesFromCustomMetadata(
                        destinationAttribute, cmName, cm.getAttributes(), attrValues);
                translatedPayload = attrValues;
            } catch (AtlanException e) {
                throw new IOException("Unable to find custom metadata with name: " + cmName, e);
            }
        }

        gen.writeStartObject();

        JacksonUtils.serializeString(gen, "id", request.getId());
        JacksonUtils.serializeString(gen, "version", request.getVersion());
        JacksonUtils.serializeBoolean(gen, "isActive", request.getIsActive());
        JacksonUtils.serializeLong(gen, "createdAt", request.getCreatedAt());
        JacksonUtils.serializeLong(gen, "updatedAt", request.getUpdatedAt());
        JacksonUtils.serializeString(gen, "createdBy", request.getCreatedBy());
        JacksonUtils.serializeString(gen, "tenantId", request.getTenantId());
        JacksonUtils.serializeString(gen, "sourceType", request.getSourceType());
        JacksonUtils.serializeString(gen, "sourceGuid", request.getSourceGuid());
        JacksonUtils.serializeString(gen, "sourceQualifiedName", request.getSourceQualifiedName());
        JacksonUtils.serializeString(gen, "sourceAttribute", request.getSourceAttribute());
        JacksonUtils.serializeString(gen, "destinationGuid", request.getDestinationGuid());
        JacksonUtils.serializeString(gen, "destinationQualifiedName", request.getDestinationQualifiedName());
        JacksonUtils.serializeString(gen, "destinationAttribute", destinationAttribute);
        JacksonUtils.serializeString(gen, "destinationValue", request.getDestinationValue());
        JacksonUtils.serializeString(gen, "destinationValueType", request.getDestinationValueType());
        JacksonUtils.serializeString(gen, "entityType", request.getEntityType());
        JacksonUtils.serializeObject(gen, "requestType", requestType);
        JacksonUtils.serializeString(gen, "approvedBy", request.getApprovedBy());
        JacksonUtils.serializeString(gen, "rejectedBy", request.getRejectedBy());
        JacksonUtils.serializeObject(gen, "status", request.getStatus());
        JacksonUtils.serializeString(gen, "message", request.getMessage());
        JacksonUtils.serializeString(gen, "approvalType", request.getApprovalType());
        JacksonUtils.serializeLong(gen, "hash", request.getHash());
        JacksonUtils.serializeBoolean(gen, "isDuplicate", request.getIsDuplicate());
        JacksonUtils.serializeString(gen, "destinationValueAction", request.getDestinationValueAction());
        JacksonUtils.serializeObject(gen, "requestApproverUsers", request.getRequestApproverUsers());
        JacksonUtils.serializeObject(gen, "requestApproverGroups", request.getRequestApproverGroups());
        JacksonUtils.serializeObject(gen, "requestDenyUsers", request.getRequestDenyUsers());
        JacksonUtils.serializeObject(gen, "requestDenyGroups", request.getRequestDenyGroups());
        JacksonUtils.serializeObject(gen, "requestDenyRoles", request.getRequestDenyRoles());
        JacksonUtils.serializeObject(gen, "destinationEntity", request.getDestinationEntity());
        JacksonUtils.serializeObject(gen, "payload", translatedPayload);

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

        gen.writeEndObject();
    }
}
