/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.admin;

import com.atlan.net.ApiResource;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class PermissionsResponse extends ApiResource {
    private static final long serialVersionUID = 3L;

    @Deprecated
    AssignedRole assignedRole;

    List<DecentralizedRole> decentralizedRoles;

    List<String> defaultRoles;

    String designation;

    List<Group> groups;

    List<String> permissions;

    List<Permission> personas;

    String profileRole;

    String profileRoleOther;

    List<Permission> purposes;

    List<Permission> roles;

    String userId;

    String username;

    String workspaceRole;

    @Getter
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = true)
    public static final class Permission {
        /** Unique identifier (UUID) of the permission object. */
        String id;

        /** Unique identifier (UUID) of the role for the object. */
        String roleId;

        /** Internal name of the role for the object. */
        String roleName;

        /** Name of the permission object. */
        String name;
    }

    @Getter
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = true)
    public static final class DecentralizedRole {
        /** Unique identifier (UUID) of the role. */
        String roleId;

        /** Level to which the permission applies. */
        String level;

        /** Privilege within the level. */
        String privilege;
    }

    @Getter
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = true)
    public static final class AssignedRole {
        /** Unique identifier (UUID) of the role. */
        String id;

        /** Internal name of the role. */
        String name;

        /** Name of the role used in the user interface. */
        String description;

        /** Level to which the permission applies. */
        String level;

        /** TBC */
        String summary;
    }

    @Getter
    @EqualsAndHashCode(callSuper = false)
    @ToString(callSuper = true)
    public static final class Group {
        /** Unique identifier (UUID) of the group. */
        String id;

        /** Internal name of the group. */
        String name;

        /** TBC */
        String path;
    }
}
