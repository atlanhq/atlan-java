/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PackageParameter {

    /** Name of the parameter. */
    String parameter;

    /** Type of the parameter. */
    String type;

    /** Details of the parameter. */
    Map<String, Object> body;
}
