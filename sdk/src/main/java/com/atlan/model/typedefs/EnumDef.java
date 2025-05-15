/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.typedefs;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanTypeCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structural definition of an enumeration.
 * Note that unlike other type definitions, enumerations do NOT use hashed internal string IDs. Their
 * name is precisely the same as the name viewable in the UI.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class EnumDef extends TypeDef {
    private static final long serialVersionUID = 2L;

    /** Fixed category for enum typedefs. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    AtlanTypeCategory category = AtlanTypeCategory.ENUM;

    /** Individual valid values for the enumeration. */
    @Singular
    List<ElementDef> elementDefs;

    /**
     * Translate the element definitions in this enumeration into a simple list of strings.
     *
     * @return list of valid values for the enumeration
     */
    @JsonIgnore
    public List<String> getValidValues() {
        if (elementDefs != null && !elementDefs.isEmpty()) {
            return elementDefs.stream().map(ElementDef::getValue).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Builds the minimal object necessary to create an enumeration definition.
     *
     * @param displayName the human-readable name for the enumeration
     * @param values the list of valid values (as strings) for the enumeration
     * @return the minimal request necessary to create the enumeration typedef, as a builder
     */
    public static EnumDefBuilder<?, ?> creator(String displayName, List<String> values) {
        return EnumDef.builder().name(displayName).elementDefs(ElementDef.from(values));
    }

    /**
     * Builds the minimal object necessary to update an enumeration definition.
     *
     * @param client connectivity to the Atlan tenant on which to update the enumeration
     * @param displayName the human-readable name for the enumeration
     * @param values the list of additional valid values (as strings) to add to the existing enumeration
     * @param replaceExisting if true, will replace all existing values in the enumeration with the new ones; or if false the new ones will be appended to the existing set
     * @return the minimal request necessary to update the enumeration typedef, as a builder
     * @throws AtlanException on any API issues related to retrieving the existing enumeration
     */
    public static EnumDefBuilder<?, ?> updater(
            AtlanClient client, String displayName, List<String> values, boolean replaceExisting)
            throws AtlanException {
        List<String> combined;
        if (replaceExisting) {
            combined = values;
        } else {
            combined = new ArrayList<>();
            List<String> existing = client.getEnumCache().getByName(displayName).getValidValues();
            for (String one : existing) {
                if (!values.contains(one)) {
                    combined.add(one);
                }
            }
            combined.addAll(values);
        }
        return EnumDef.builder().name(displayName).elementDefs(ElementDef.from(combined));
    }

    /**
     * Create this enumeration definition in Atlan.
     *
     * @param client connectivity to the Atlan tenant on which to create the enumeration
     * @return the result of the creation, or null if the creation failed
     * @throws AtlanException on any API communication issues
     */
    public synchronized EnumDef create(AtlanClient client) throws AtlanException {
        TypeDefResponse response = client.typeDefs.create(this);
        if (response != null && !response.getEnumDefs().isEmpty()) {
            return response.getEnumDefs().get(0);
        }
        return null;
    }

    /**
     * Update this enumeration definition in Atlan.
     *
     * @param client connectivity to the Atlan tenant on which to update the enumeration
     * @return the result of the update, or null if the update failed
     * @throws AtlanException on any API communication issues
     */
    public synchronized EnumDef update(AtlanClient client) throws AtlanException {
        TypeDefResponse response = client.typeDefs.update(this);
        if (response != null && !response.getEnumDefs().isEmpty()) {
            return response.getEnumDefs().get(0);
        }
        return null;
    }

    /**
     * Hard-deletes (purges) an enumeration by its human-readable name. This operation is irreversible.
     * If there are any existing enumeration instances, this operation will fail.
     *
     * @param client connectivity to the Atlan tenant from which to purge the enumeration
     * @param displayName human-readable name of the enumeration
     * @throws AtlanException on any error during the API invocation
     */
    public static synchronized void purge(AtlanClient client, String displayName) throws AtlanException {
        client.typeDefs.purge(displayName);
    }

    /**
     * Structure for definition of a valid value in an enumeration.
     */
    @Getter
    @Jacksonized
    @SuperBuilder
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = true)
    public static class ElementDef extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Value of the element (the valid value). */
        String value;

        /** (Optional) Description of the element. */
        String description;

        /** Position of the element within the list of valid values. */
        Integer ordinal;

        /**
         * Build a valid value definition.
         *
         * @param ordinal position of the valid value definition in the overall list
         * @param value of the valid value
         * @return the valid value definition
         */
        public static ElementDef of(int ordinal, String value) {
            return ElementDef.builder().ordinal(ordinal).value(value).build();
        }

        /**
         * Build a list of valid values from the provided list of strings.
         *
         * @param values to enumerate as valid values
         * @return a list of the valid values
         */
        protected static List<ElementDef> from(List<String> values) {
            if (values != null && !values.isEmpty()) {
                List<ElementDef> elements = new ArrayList<>();
                for (int i = 0; i < values.size(); i++) {
                    elements.add(ElementDef.of(i, values.get(i)));
                }
                return Collections.unmodifiableList(elements);
            } else {
                return Collections.emptyList();
            }
        }
    }

    public abstract static class EnumDefBuilder<C extends EnumDef, B extends EnumDefBuilder<C, B>>
            extends TypeDef.TypeDefBuilder<C, B> {}
}
