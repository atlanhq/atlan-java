/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.model.contracts;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Capture the detailed specification of a data contract for an asset.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("cast")
public class DCS_V_0_0_2 extends DataContractSpec {
    private static final long serialVersionUID = 2L;

    /** Fixed typeName for Tables. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String templateVersion = "0.0.2";
}
