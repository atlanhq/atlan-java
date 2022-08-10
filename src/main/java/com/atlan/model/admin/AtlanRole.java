package com.atlan.model.admin;

import com.atlan.net.AtlanObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
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
