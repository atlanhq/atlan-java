/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.relations;

import com.atlan.model.core.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class UniqueAttributes extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Unique name of the related entity. */
    String qualifiedName;

    /** Unique resource number for AWS resources (only). */
    String awsArn;
}
