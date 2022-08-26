/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.relations;

import com.atlan.net.AtlanObjectJ;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class UniqueAttributes extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    /** Unique name of the related entity. */
    String qualifiedName;

    /** Unique resource number for AWS resources (only). */
    String awsArn;
}
