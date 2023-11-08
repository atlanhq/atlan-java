/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import lombok.ToString;

/** Base class for all request payloads. */
@ToString(callSuper = true)
public abstract class AtlanRequestPayload extends AtlanObject {
    private static final long serialVersionUID = 2L;
}
