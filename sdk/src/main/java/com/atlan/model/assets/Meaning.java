/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanObject;
import java.util.Comparator;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about a term related to an asset.
 */
@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Meaning extends AtlanObject implements Comparable<Meaning> {
    private static final long serialVersionUID = 2L;

    private static final Comparator<String> stringComparator = Comparator.nullsFirst(String::compareTo);
    private static final Comparator<Meaning> meaningComparator = Comparator.comparing(
                    Meaning::getTermGuid, stringComparator)
            .thenComparing(Meaning::getRelationGuid, stringComparator);

    /** Unique identifier (GUID) of the related term. */
    String termGuid;

    /** Unique identifier (GUID) of the relationship itself. */
    String relationGuid;

    /** Human-readable display name of the related term. */
    String displayText;

    /** Unused. */
    Integer confidence;

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Meaning o) {
        return meaningComparator.compare(this, o);
    }

    public abstract static class MeaningBuilder<C extends Meaning, B extends MeaningBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
