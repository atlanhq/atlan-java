/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.requests;

import com.atlan.model.core.Entity;
import com.atlan.net.AtlanObjectJ;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class SingleEntityRequest extends AtlanObjectJ {
    /** The entity to update. */
    Entity entity;
}
