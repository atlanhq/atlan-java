/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.core;

import com.atlan.net.AtlanObjectJ;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class BulkEntityRequestJ extends AtlanObjectJ {
    /** List of entities to operate on in bulk. */
    List<EntityJ> entities;
}
