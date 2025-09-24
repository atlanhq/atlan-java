/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.enums.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import java.io.IOException;

/**
 * Deserialization of all {@link AtlanPolicyAction} objects.
 * This custom deserialization is necessary to handle varying sub-enumerations given Java's lack of
 * inheritance for enumerations.
 */
public class AtlanPolicyActionDeserializer extends StdDeserializer<AtlanPolicyAction>
        implements ResolvableDeserializer {

    private static final long serialVersionUID = 2L;

    private final transient JsonDeserializer<?> defaultDeserializer;

    public AtlanPolicyActionDeserializer(JsonDeserializer<?> defaultDeserializer) {
        super(AtlanPolicyAction.class);
        this.defaultDeserializer = defaultDeserializer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
            throws IOException {
        return deserialize(p, ctxt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AtlanPolicyAction deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.getCodec().readTree(parser);
        if (root != null && root.isTextual()) {
            String value = root.asText();
            return deserialize(value);
        }
        return null;
    }

    public static AtlanPolicyAction deserialize(String value) {
        if (value.startsWith("persona")) {
            if (value.startsWith("persona-glossary")) {
                return PersonaGlossaryAction.fromValue(value);
            } else if (value.startsWith("persona-domain")) {
                return PersonaDomainAction.fromValue(value);
            } else if (value.startsWith("persona-ai")) {
                return PersonaAIAction.fromValue(value);
            } else {
                return PersonaMetadataAction.fromValue(value);
            }
        } else if (value.equals("select")) {
            return DataAction.fromValue(value);
        } else if (value.startsWith("admin-")) {
            return AdminAction.fromValue(value);
        } else if (value.startsWith("type-") || value.endsWith("-label") || value.endsWith("-relationship")) {
            return TypeDefAction.fromValue(value);
        } else {
            return PurposeMetadataAction.fromValue(value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        ((ResolvableDeserializer) defaultDeserializer).resolve(ctxt);
    }
}
