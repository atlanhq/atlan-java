package com.atlan.live.jackson;

import static org.testng.Assert.*;

import com.atlan.cache.RoleCacheJ;
import com.atlan.exception.AtlanException;
import com.atlan.live.AtlanLiveTest;
import org.testng.annotations.Test;

@Test(groups = {"admin"})
public class AdminJTest extends AtlanLiveTest {

    @Test(groups = {"read.roles"})
    void retrieveRoles() {
        try {
            String adminRoleGuid = RoleCacheJ.getIdForName("$admin");
            assertNotNull(adminRoleGuid);
        } catch (AtlanException e) {
            e.printStackTrace();
            assertNull(e, "Unexpected exception when retrieving roles.");
        }
    }
}
