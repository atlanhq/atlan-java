/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.responses;

import com.atlan.model.core.Entity;
import com.atlan.net.ApiResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EntityResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Unused. */
    Object referredEntities;

    /** The retrieved entity. */
    Entity entity;
}
