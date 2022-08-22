/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.responses;

import com.atlan.model.core.Entity;
import com.atlan.net.AtlanObject;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MutatedEntities extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Assets that were created. */
    List<Entity> CREATE;

    /** Assets that were updated. */
    List<Entity> UPDATE;

    /** Assets that were partially updated. */
    List<Entity> PARTIAL_UPDATE;

    /** Assets that were deleted. */
    List<Entity> DELETE;
}
