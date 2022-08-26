package com.atlan.model.typedefs;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanClassificationColor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Options that can be defined for a classification.
 */
@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class ClassificationOptions extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /**
     * Instantiate a new set of classification options from the provided parameters.
     * @param color for the classification
     * @return the classification options
     */
    public static ClassificationOptions of(AtlanClassificationColor color) {
        return new ClassificationOptions(color);
    }

    private ClassificationOptions(AtlanClassificationColor color) {
        this.color = color;
    }

    /** Color to use for the classification. */
    AtlanClassificationColor color;
}
