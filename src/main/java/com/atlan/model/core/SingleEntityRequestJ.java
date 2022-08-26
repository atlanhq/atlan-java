/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.net.AtlanObjectJ;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class SingleEntityRequestJ extends AtlanObjectJ {
    /** The entity to update. */
    EntityJ entity;
}
