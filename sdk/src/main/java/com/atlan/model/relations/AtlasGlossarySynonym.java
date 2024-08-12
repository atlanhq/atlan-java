/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.relations;

import com.atlan.exception.InvalidRequestException;
import com.atlan.model.assets.GlossaryTerm;
import com.atlan.model.assets.IGlossaryTerm;
import com.atlan.model.enums.AtlasGlossaryTermRelationshipStatus;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/** Terms that have the same (or a very similar) meaning, in the same language. */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings("cast")
public class AtlasGlossarySynonym extends GlossaryTerm {
    // TODO: having a class for the relationship works, but it can only extend ONE of the endDef
    //  types -- so we'd probably need to create a separate class for each end of the relationship
    //  (for any relationships where the two ends are different types)?
    private static final long serialVersionUID = 2L;

    RA relationshipAttributes;

    /**
     * Builds the minimal object necessary for creating a term.
     *
     * @param name of the term
     * @param glossary in which the term should be created
     * @return the minimal request necessary to create the term, as a builder
     * @throws InvalidRequestException if the glossary provided is without a GUID or qualifiedName
     */
    public static AtlasGlossarySynonymBuilder<?, ?> ref(IGlossaryTerm term) throws InvalidRequestException {
        if (term.getGuid() != null && !term.getGuid().isBlank()) {
            return _internal().guid(term.getGuid());
        } else {
            return _internal()
                    .uniqueAttributes(UniqueAttributes.builder()
                            .qualifiedName(term.getQualifiedName())
                            .build());
        }
    }

    @Getter
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    public static final class RA extends RelationshipAttributes {
        private static final long serialVersionUID = 2L;

        /** Details about the relationship. */
        String description;

        /** Expression used to set the relationship. */
        String expression;

        /** Status of the synonym assignment, typically used by discovery engines. */
        AtlasGlossaryTermRelationshipStatus status;

        /** User responsible for assessing the relationship and deciding if it should be approved or not. */
        String steward;

        /** Source of the relationship. */
        String source;

        @Override
        public Map<String, Object> getAll() {
            Map<String, Object> map = new HashMap<>();
            if (description != null) {
                map.put("description", description);
            }
            if (expression != null) {
                map.put("expression", expression);
            }
            if (status != null) {
                map.put("status", status);
            }
            if (steward != null) {
                map.put("steward", steward);
            }
            if (source != null) {
                map.put("source", source);
            }
            return map;
        }
    }
}
