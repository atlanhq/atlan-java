/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.net.ApiResource;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class GroupResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    Integer totalRecord;
    Integer filterRecord;
    List<AtlanGroup> records;
}
