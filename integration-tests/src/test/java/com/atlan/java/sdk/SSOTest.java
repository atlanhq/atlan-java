/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.api.SSOEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.admin.*;
import com.atlan.model.enums.AtlanSSO;
import java.util.List;
import org.testng.annotations.Test;

/**
 * Test management of SSO configuration.
 */
public class SSOTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("SSO");
    // private static final String FIXED_USER = "aryaman.bhushan";
    private static final String GROUP_NAME = PREFIX;

    private static final String SSO_TYPE = AtlanSSO.JUMPCLOUD.getValue();
    private static final String SSO_GROUP_NAME = PREFIX;
    private static final String SSO_GROUP_NAME_UPDATED = SSO_GROUP_NAME + "-updated";

    private static AtlanGroup group1 = null;
    private static SSOMapping groupMapping = null;

    @Test(groups = {"sso.create.group"})
    void createGroup() throws AtlanException {
        group1 = AdminTest.createGroup(client, GROUP_NAME);
    }

    @Test(
            groups = {"sso.create.mapping"},
            dependsOnGroups = {"sso.create.group"})
    void createSsoMapping() throws AtlanException {
        SSOMapping response = client.sso.createGroupMapping(SSO_TYPE, group1, SSO_GROUP_NAME);
        assertNotNull(response);
    }

    @Test(
            groups = {"sso.read.mapping"},
            dependsOnGroups = {"sso.create.mapping"})
    void readSsoMapping() throws AtlanException {
        List<SSOMapping> mappings = client.sso.listGroupMappings(SSO_TYPE);
        assertNotNull(mappings);
        for (SSOMapping mapping : mappings) {
            if (mapping.getName().contains(group1.getId())
                    && mapping.getIdentityProviderMapper().equals(SSOEndpoint.IDP_GROUP_MAPPER)) {
                groupMapping = mapping;
                break;
            }
        }
        assertNotNull(groupMapping);
        validateSsoMapping(group1, groupMapping, false);
    }

    @Test(
            groups = {"sso.create.mapping.again"},
            dependsOnGroups = {"sso.read.mapping"})
    void createSsoMappingAgain() {
        assertThrows(
                InvalidRequestException.class, () -> client.sso.createGroupMapping(SSO_TYPE, group1, SSO_GROUP_NAME));
    }

    @Test(
            groups = {"sso.update.mapping"},
            dependsOnGroups = {"sso.create.mapping.again"})
    void updateSsoMapping() throws AtlanException {
        SSOMapping updated = client.sso.updateGroupMapping(groupMapping, group1, SSO_GROUP_NAME_UPDATED);
        validateSsoMapping(group1, updated, true);
    }

    @Test(
            groups = {"sso.delete.mapping"},
            dependsOnGroups = {"sso.update.mapping"})
    void deleteSsoMapping() throws AtlanException, InterruptedException {
        client.sso.deleteGroupMapping(SSO_TYPE, groupMapping.getId());
        Thread.sleep(5000);
        List<SSOMapping> mappings = client.sso.listGroupMappings(SSO_TYPE);
        assertNotNull(mappings);
        SSOMapping local = null;
        for (SSOMapping mapping : mappings) {
            if (mapping.getName().contains(group1.getId())
                    && mapping.getIdentityProviderMapper().equals(SSOEndpoint.IDP_GROUP_MAPPER)) {
                local = mapping;
                break;
            }
        }
        assertNull(local);
    }

    private void validateSsoMapping(AtlanGroup group, SSOMapping mapping, boolean isUpdated) {
        assertNotNull(mapping);
        assertNotNull(mapping.getId());
        assertEquals(mapping.getIdentityProviderAlias(), SSO_TYPE);
        assertEquals(mapping.getIdentityProviderMapper(), SSOEndpoint.IDP_GROUP_MAPPER);
        assertNotNull(mapping.getConfig());
        assertEquals(mapping.getConfig().getAttributes(), "[]");
        assertEquals(mapping.getConfig().getGroupName(), group.getName());
        assertNull(mapping.getConfig().getAttributeValuesRegex());
        assertNull(mapping.getConfig().getAttributeFriendlyName());
        assertEquals(mapping.getConfig().getSyncMode(), SSOEndpoint.GROUP_MAPPER_SYNC_MODE);
        assertEquals(mapping.getConfig().getAttributeName(), SSOEndpoint.GROUP_MAPPER_ATTRIBUTE);
        assertTrue(mapping.getName().contains(group.getId()));
        if (isUpdated) {
            assertEquals(mapping.getConfig().getAttributeValue(), SSO_GROUP_NAME_UPDATED);
        } else {
            assertEquals(mapping.getConfig().getAttributeValue(), SSO_GROUP_NAME);
        }
    }

    @Test(
            groups = {"sso.purge.mappings"},
            dependsOnGroups = {"sso.create.*", "sso.read.*", "sso.update.*", "sso.delete.*"},
            alwaysRun = true)
    void purgeMappings() throws AtlanException {
        AdminTest.deleteGroup(client, group1.getId());
    }
}
