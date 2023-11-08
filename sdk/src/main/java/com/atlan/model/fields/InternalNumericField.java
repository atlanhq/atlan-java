/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.fields;

import lombok.Getter;

/**
 * Represents any field in Atlan that can be searched using only numeric search operations,
 * and can also be searched against a special internal field directly within Atlan.
 */
public class InternalNumericField extends NumericField implements IInternalSearchable {

    @Getter
    private final String internalFieldName;

    /**
     * Default constructor.
     *
     * @param atlan name of the attribute in the metastore
     * @param internal name of the internal searchable attribute in the metastore
     * @param numeric name of the numeric field in the search index
     */
    public InternalNumericField(String atlan, String internal, String numeric) {
        super(atlan, numeric);
        this.internalFieldName = internal;
    }
}
