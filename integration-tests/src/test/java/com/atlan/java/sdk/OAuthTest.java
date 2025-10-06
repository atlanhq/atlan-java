/* SPDX-License-Identifier: Apache-2.0
   Copyright 2024 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.PermissionException;
import com.atlan.model.admin.OAuthClient;
import com.atlan.model.admin.OAuthClientResponse;
import com.atlan.model.assets.Glossary;
import java.io.IOException;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class OAuthTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("OAuth");
    private static final String CLIENT_NAME = PREFIX;

    private static OAuthClient oauthGuest = null;
    private static AtlanClient guestClient = null;

    /** Create a new OAuth client.
     *
     * @param client connectivity to the Atlan tenant
     * @param name of the client
     * @param description to give the client
     * @param personaQNs qualifiedNames of personas to bind to the client
     * @param role workspace role to which to bind the client
     * @return the created client
     * @throws AtlanException on any errors creating the client
     */
    static OAuthClient createOAuthClient(
            AtlanClient client, String name, String description, Set<String> personaQNs, String role)
            throws AtlanException {
        OAuthClient created = OAuthClient.create(client, name, description, personaQNs, role);
        assertNotNull(created);
        assertNotNull(created.getClientId());
        assertNotNull(created.getClientSecret());
        assertNotNull(created.getTokenExpirySeconds());
        assertNotNull(created.getCreatedBy());
        assertNotNull(created.getCreatedAt());
        assertEquals(created.getDisplayName(), name);
        assertEquals(created.getDescription(), description);
        return created;
    }

    /**
     * Delete (purge) an OAuth client based on its clientId.
     *
     * @param client connectivity to the Atlan tenant
     * @param clientId unique identifier of the OAuth client
     * @throws AtlanException on any errors purging the client
     */
    static void deleteOAuthClient(AtlanClient client, String clientId) throws AtlanException {
        OAuthClient.delete(client, clientId);
    }

    @Test(groups = {"oauth.create.guest"})
    void createGuestClient() throws AtlanException, InterruptedException {
        oauthGuest = createOAuthClient(client, CLIENT_NAME, "Guest client.", Set.of(), "$guest");
        String clientId = oauthGuest.getClientId();
        String secret = oauthGuest.getClientSecret();
        assertNotNull(clientId);
        assertNotNull(secret);
        guestClient = new AtlanClient(System.getenv("ATLAN_BASE_URL"), clientId, secret);
    }

    @Test(
            groups = {"oauth.read.clients"},
            dependsOnGroups = {"oauth.create.guest"})
    void retrieveClients() throws AtlanException {
        OAuthClientResponse response = client.oauthClients.list();
        assertNotNull(response);
        assertTrue(response.getTotalRecord() >= 1);
        boolean found = false;
        for (OAuthClient one : response.getRecords()) {
            String displayName = one.getDisplayName();
            found = CLIENT_NAME.equals(displayName) && oauthGuest.getClientId().equals(one.getClientId());
            if (found) break;
        }
        assertTrue(found);
    }

    @Test(
            groups = {"oauth.read.guest"},
            dependsOnGroups = {"oauth.create.guest"})
    void retrieveGuestClientByName() throws AtlanException {
        OAuthClient retrieved = OAuthClient.retrieveByName(client, CLIENT_NAME);
        assertNotNull(retrieved);
        assertEquals(retrieved.getDisplayName(), CLIENT_NAME);
        assertNotNull(retrieved.getId());
        assertEquals(oauthGuest.getClientId(), retrieved.getClientId());
        assertNull(retrieved.getClientSecret());
        assertEquals(retrieved.getRole(), "$guest");
        assertTrue(
                retrieved.getPersonaQNs() == null || retrieved.getPersonaQNs().isEmpty());
    }

    @Test(
            groups = {"oauth.update.guest"},
            dependsOnGroups = {"oauth.read.guest"})
    void updateGuestClient() throws AtlanException {
        final String description = "Now with a revised description.";
        OAuthClient updated =
                client.oauthClients.update(oauthGuest.getClientId(), oauthGuest.getDisplayName(), description);
        assertNotNull(updated);
        assertEquals(updated.getDescription(), description);
        assertEquals(updated.getDisplayName(), CLIENT_NAME);
    }

    @Test(
            groups = {"oauth.use.guest"},
            dependsOnGroups = {"oauth.update.guest"})
    void findGlossariesAsGuest() throws AtlanException {
        long glossaryCount = Glossary.select(guestClient).count();
        assertTrue(glossaryCount > 1);
    }

    @Test(
            groups = {"oauth.use.guest"},
            dependsOnGroups = {"oauth.update.guest"})
    void cannotCreateGlossaryAsGuest() {
        Glossary toCreate = Glossary.creator(CLIENT_NAME).build();
        assertThrows(PermissionException.class, () -> toCreate.save(guestClient));
    }

    @Test(
            groups = {"oauth.purge.guest"},
            dependsOnGroups = {"oauth.create.*", "oauth.read.*", "oauth.update.*", "oauth.use.*"},
            alwaysRun = true)
    void purgeGuest() throws AtlanException {
        if (oauthGuest != null) {
            deleteOAuthClient(client, oauthGuest.getClientId());
        } else {
            OAuthClient local = client.oauthClients.get(CLIENT_NAME);
            if (local != null) {
                deleteOAuthClient(client, local.getClientId());
            }
        }
    }

    @Test(
            groups = {"oauth.purge.client"},
            dependsOnGroups = {"oauth.create.*", "oauth.read.*", "oauth.update.*", "oauth.use.*"},
            alwaysRun = true)
    void closeTemporaryClient() throws IOException {
        if (guestClient != null) {
            guestClient.close();
        }
    }
}
