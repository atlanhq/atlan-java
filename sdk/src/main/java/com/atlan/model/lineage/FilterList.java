/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Class used to define the composition of multiple filters for objects when retrieving lineage.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class FilterList extends AtlanObject {
    private static final long serialVersionUID = 2L;

    public enum Condition implements AtlanEnum {
        AND("AND"),
        OR("OR"),
        ;

        @JsonValue
        @Getter(onMethod_ = {@Override})
        private final String value;

        Condition(String value) {
            this.value = value;
        }

        public static Condition fromValue(String value) {
            for (Condition b : Condition.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            return null;
        }
    }

    /** Whether the criteria must all match (AND) or any matching is sufficient (OR). */
    @Builder.Default
    Condition condition = Condition.AND;

    /** Basis on which to compare a result for inclusion. */
    @JsonProperty("criterion")
    @Singular("criterion")
    List<EntityFilter> criteria;
}
