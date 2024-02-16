/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GroupRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Criteria by which to filter the list of groups to retrieve. */
    String filter;

    /** Property by which to sort the resulting list of groups. */
    @Builder.Default
    String sort = "name";

    /** Whether to include an overall count of groups (true) or not (false). */
    @Builder.Default
    boolean count = true;

    /** Starting point for the list of groups when paging. */
    @Builder.Default
    int offset = 0;

    /** Maximum number of groups to return per page. */
    @Builder.Default
    int limit = 20;

    /**
     * Search for groups using the filters specified in this request object.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the groups
     * @return the filtered groups
     * @throws AtlanException on any issues interacting with the APIs
     */
    public GroupResponse list(AtlanClient client) throws AtlanException {
        return client.groups.list(this);
    }
}
