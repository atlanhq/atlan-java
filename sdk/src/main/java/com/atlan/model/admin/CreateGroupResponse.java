/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import com.atlan.net.ApiResource;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Specialized response for creating a group, as the details included are different
 * from those when updating or retrieving a group.
 */
@Getter
@Jacksonized
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class CreateGroupResponse extends ApiResource {
    private static final long serialVersionUID = 2L;

    /** Unique identifier (GUID) of the group within Atlan. */
    String group;

    /** Map of user association statuses, keyed by unique identifier (GUID) of the user. */
    Map<String, UserStatus> users;

    /** Status of the user being added to the group during its creation. */
    @Getter
    @Jacksonized
    @SuperBuilder(toBuilder = true)
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static final class UserStatus extends AtlanObject {
        private static final long serialVersionUID = 2L;

        /** Response code for the association (200 is success). */
        Integer status;

        /** Status message for the association ({@code success} means the association was successful). */
        String statusMessage;

        /** Indicates whether the association was made successfully (true) or not (false). */
        @JsonIgnore
        public boolean wasSuccessful() {
            if (status != null) {
                return status == 200;
            }
            if (statusMessage != null) {
                return statusMessage.equals("success");
            }
            return false;
        }
    }
}
