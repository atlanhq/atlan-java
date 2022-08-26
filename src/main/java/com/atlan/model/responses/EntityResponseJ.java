/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.responses;

import com.atlan.model.core.EntityJ;
import com.atlan.net.ApiResourceJ;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EntityResponseJ extends ApiResourceJ {
    private static final long serialVersionUID = 2L;

    /** Unused. */
    Object referredEntities;

    /** The retrieved entity. */
    EntityJ entity;
}
