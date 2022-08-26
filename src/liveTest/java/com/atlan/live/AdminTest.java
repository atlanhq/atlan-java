/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.cache.RoleCache;
import com.atlan.exception.AtlanException;
import org.testng.annotations.Test;

@Test(groups = {"admin"})
public class AdminTest extends AtlanLiveTest {

    @Test(groups = {"read.roles"})
    void retrieveRoles() {
        try {
            String adminRoleGuid = RoleCache.getIdForName("$admin");
            assertNotNull(adminRoleGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when retrieving roles.");
        }
    }
}
