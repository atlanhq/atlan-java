/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.enums.PurposeMetadataPolicyAction;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PurposeMetadataPolicy extends AbstractPurposePolicy {
    private static final long serialVersionUID = 2L;

    /** All the actions included in the policy. */
    @Singular
    SortedSet<PurposeMetadataPolicyAction> actions;

    /** Fixed value. */
    @Builder.Default
    String type = "metadata";
}
