/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.responses;

import com.atlan.model.core.Entity;
import com.atlan.net.AtlanObjectJ;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class MutatedEntities extends AtlanObjectJ {
    private static final long serialVersionUID = 2L;

    /** Assets that were created. */
    @JsonProperty("CREATE")
    List<Entity> CREATE;

    /** Assets that were updated. */
    @JsonProperty("UPDATE")
    List<Entity> UPDATE;

    /** Assets that were partially updated. */
    @JsonProperty("PARTIAL_UPDATE")
    List<Entity> PARTIAL_UPDATE;

    /** Assets that were deleted. */
    @JsonProperty("DELETE")
    List<Entity> DELETE;
}
