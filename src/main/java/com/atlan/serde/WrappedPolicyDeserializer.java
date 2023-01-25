/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.serde;

import com.atlan.model.admin.*;
import com.atlan.model.enums.DataPolicyAction;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;

public class WrappedPolicyDeserializer extends StdDeserializer<WrappedPolicy> {
    private static final long serialVersionUID = 2L;

    public WrappedPolicyDeserializer() {
        this(null);
    }

    public WrappedPolicyDeserializer(Class<?> t) {
        super(t);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WrappedPolicy deserialize(JsonParser parser, DeserializationContext context) throws IOException {

        TreeNode tree = parser.getCodec().readTree(parser);
        TreeNode connectionId = tree.get("connectionId");
        TreeNode glossaryQNs = tree.get("glossaryQualifiedNames");

        AbstractPolicy policy = null;
        if (connectionId != null && !connectionId.isMissingNode()) {
            // If there is a connectionId, it must be a persona policy
            TreeNode actions = tree.get("actions");
            if (actions.isArray()) {
                ArrayNode actionsList = (ArrayNode) actions;
                String firstAction = actionsList.get(0).asText();
                if (firstAction != null && firstAction.equals(DataPolicyAction.SELECT.getValue())) {
                    // If the action is a data policy action, it's a data policy
                    policy = parser.getCodec().treeToValue(tree, PersonaDataPolicy.class);
                } else {
                    // Otherwise it can only be a metadata policy
                    policy = parser.getCodec().treeToValue(tree, PersonaMetadataPolicy.class);
                }
            }
        } else if (glossaryQNs != null && !glossaryQNs.isMissingNode()) {
            // If there is a glossaryQualifiedNames then it must be a glossary policy
            policy = parser.getCodec().treeToValue(tree, GlossaryPolicy.class);
        } else {
            // Otherwise it must be some form of purpose policy
            TreeNode actions = tree.get("actions");
            if (actions.isArray()) {
                ArrayNode actionsList = (ArrayNode) actions;
                String firstAction = actionsList.get(0).asText();
                if (firstAction != null && firstAction.equals(DataPolicyAction.SELECT.getValue())) {
                    // If there is only a single SELECT action present, it is a data policy
                    policy = parser.getCodec().treeToValue(tree, PurposeDataPolicy.class);
                } else {
                    // Otherwise it can only be a metadata policy
                    policy = parser.getCodec().treeToValue(tree, PurposeMetadataPolicy.class);
                }
            }
        }
        return new WrappedPolicy(policy);
    }
}
