package com.atlan.functional;

import static org.testng.Assert.*;

import com.atlan.cache.RoleCache;
import com.atlan.exception.AtlanException;
import org.testng.annotations.Test;

public class AdminTest extends AtlanLiveTest {

    @Test(groups = {"roles.retrieve"})
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
