/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.enums;

import com.atlan.serde.AtlanPolicyActionDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = AtlanPolicyActionDeserializer.class)
public interface AtlanPolicyAction {}
