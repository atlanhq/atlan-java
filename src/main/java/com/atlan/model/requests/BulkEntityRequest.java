/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.requests;

import com.atlan.model.core.AtlanObject;
import com.atlan.model.core.Entity;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class BulkEntityRequest extends AtlanObject {
    /** List of entities to operate on in bulk. */
    List<Entity> entities;
}
