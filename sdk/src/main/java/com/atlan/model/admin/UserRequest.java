/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class UserRequest extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** TBC */
    @Builder.Default
    int maxLoginEvents = 1;

    /** Criteria by which to filter the list of users to retrieve. */
    String filter;

    /** Property by which to sort the resulting list of users. */
    @Builder.Default
    String sort = "username";

    /** Whether to include an overall count of users (true) or not (false). */
    @Builder.Default
    boolean count = true;

    /** Starting point for the list of users when paging. */
    @Builder.Default
    int offset = 0;

    /** Maximum number of users to return per page. */
    @Builder.Default
    int limit = 20;

    /** List of columns to be returned about each user in the response. */
    @Singular
    List<String> columns;

    /**
     * Search for users using the filters specified in this request object.
     *
     * @param client connectivity to the Atlan tenant from which to retrieve the users
     * @return the filtered users
     * @throws AtlanException on any issues interacting with the APIs
     */
    public UserResponse list(AtlanClient client) throws AtlanException {
        return client.users.list(this);
    }

    public abstract static class UserRequestBuilder<C extends UserRequest, B extends UserRequestBuilder<C, B>>
            extends AtlanObject.AtlanObjectBuilder<C, B> {}
}
