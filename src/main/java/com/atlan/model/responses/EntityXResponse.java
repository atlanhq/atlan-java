/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.responses;

import com.atlan.model.core.EntityX;
import com.atlan.net.ApiResource;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class EntityXResponse extends ApiResource {
    /** Unused. */
    Object referredEntities;

    /** The retrieved entity. */
    EntityX entity;
}
