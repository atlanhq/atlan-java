/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.net.ApiResource;
import com.atlan.serde.WrappedPolicyDeserializer;
import com.atlan.serde.WrappedPolicySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Necessary for having a policy object that extends ApiResource for API interactions.
 */
@Data
@JsonSerialize(using = WrappedPolicySerializer.class)
@JsonDeserialize(using = WrappedPolicyDeserializer.class)
@EqualsAndHashCode(callSuper = false)
public final class WrappedPolicy extends ApiResource {
    AbstractPolicy policy;

    public WrappedPolicy(AbstractPolicy policy) {
        this.policy = policy;
    }
}
