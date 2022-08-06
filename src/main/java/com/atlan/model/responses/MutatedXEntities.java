/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.responses;

import com.atlan.model.core.EntityX;
import com.atlan.net.AtlanObject;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MutatedXEntities extends AtlanObject {
    /** Assets that were created. */
    List<EntityX> CREATE;

    /** Assets that were updated. */
    List<EntityX> UPDATE;

    /** Assets that were deleted. */
    List<EntityX> DELETE;
}
