package com.atlan.model.admin;

import com.atlan.model.core.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AtlanRole extends AtlanObject {
    private static final long serialVersionUID = 2L;

    /** Unique identifier for the role (GUID). */
    String id;

    /** Description of the role. */
    String description;

    /** Unique name for the role. */
    String name;

    /** TBC */
    Boolean clientRole;

    /** TBC */
    String level;

    /** Number of users with this role. */
    String memberCount;
}
