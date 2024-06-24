/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.AtlanClient;
import com.atlan.model.core.AtlanObject;
import io.openlineage.client.OpenLineage;
import io.openlineage.client.OpenLineageClientUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Base class for handling OpenLineage events, passing through to the OpenLineage Java SDK
 * but wrapping events such that they are handled appropriately in the Atlan Java SDK.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class OpenLineageEvent extends AtlanObject {
    private static final long serialVersionUID = 2L;

    OpenLineage.BaseEvent event;

    public OpenLineageEvent(OpenLineage.BaseEvent event) {
        this.event = event;
    }

    /** {@inheritDoc} */
    @Override
    public String toJson(AtlanClient client) {
        return OpenLineageClientUtils.toJson(event);
    }
}
