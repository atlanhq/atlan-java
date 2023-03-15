/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.api.ApiTokensEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.assets.Glossary;
import com.atlan.model.assets.GlossaryTerm;
import org.testng.annotations.Test;

/**
 * Test management of requests.
 */
@Test(groups = {"request"})
public class RequestsTest extends AtlanLiveTest {

    private static final String PREFIX = "RequestsTest";
    private static final String API_TOKEN_NAME = PREFIX;
    private static final String GLOSSARY_NAME = PREFIX;
    private static final String TERM_NAME = PREFIX + " term";

    private static final String originalToken = Atlan.getApiToken();
    private static String requestsToken = null;
    private static ApiToken token = null;
    private static Glossary glossary = null;
    private static GlossaryTerm term = null;

    /**
     * Create a new API token with a unique name.
     *
     * @param name to make the token unique
     * @return the token that was created
     * @throws AtlanException on any error creating or reading-back the group
     */
    static ApiToken createToken(String name) throws AtlanException {
        ApiToken created = ApiToken.create(name);
        assertNotNull(created);
        assertEquals(created.getDisplayName(), name);
        assertNotNull(created.getAttributes());
        assertNotNull(created.getAttributes().getAccessToken());
        return created;
    }

    /**
     * Delete (purge) an API token based on its GUID.
     *
     * @param guid of the token to purge
     * @throws AtlanException on any errors purging the group
     */
    static void deleteToken(String guid) throws AtlanException {
        ApiToken.delete(guid);
    }

    @Test(groups = {"create.glossary"})
    void createGlossary() throws AtlanException {
        Atlan.setApiToken(originalToken);
        glossary = GlossaryTest.createGlossary(GLOSSARY_NAME);
        term = GlossaryTest.createTerm(TERM_NAME, glossary.getGuid());
    }

    @Test(groups = {"create.token"})
    void createToken() throws AtlanException {
        token = createToken(API_TOKEN_NAME);
        assertNotNull(token.getClientId());
        requestsToken = token.getAttributes().getAccessToken();
    }

    @Test(
            groups = {"read.token"},
            dependsOnGroups = {"create.token"})
    void retrieveTokens() throws AtlanException {
        ApiTokenResponse response = ApiTokensEndpoint.getTokens();
        assertNotNull(response);
        assertTrue(response.getTotalRecord() > 1);
        assertTrue(response.getTotalRecord() < 100);
        boolean found = false;
        for (ApiToken one : response.getRecords()) {
            String displayName = one.getDisplayName();
            found = API_TOKEN_NAME.equals(displayName);
            if (found) break;
        }
        assertTrue(found);
    }

    @Test(
            groups = {"read.token"},
            dependsOnGroups = {"create.token"})
    void retrieveTokenByName() throws AtlanException {
        ApiToken retrieved = ApiToken.retrieveByName(API_TOKEN_NAME);
        assertNotNull(retrieved);
        assertEquals(retrieved.getDisplayName(), API_TOKEN_NAME);
        assertNotNull(retrieved.getId());
        token = retrieved;
    }

    @Test(
            groups = {"update.token"},
            dependsOnGroups = {"read.token"})
    void updateToken() throws AtlanException {
        final String description = "Now with a revised description.";
        ApiToken revised = token.toBuilder()
                .attributes(token.getAttributes().toBuilder()
                        .description(description)
                        .build())
                .build();
        ApiToken updated = revised.update();
        assertNotNull(updated);
        assertNotNull(updated.getAttributes());
        assertEquals(updated.getAttributes().getDescription(), description);
        assertEquals(updated.getDisplayName(), API_TOKEN_NAME);
    }

    @Test(
            groups = {"purge.token"},
            dependsOnGroups = {"create.*", "read.*", "update.*"},
            alwaysRun = true)
    void purgeToken() throws AtlanException {
        Atlan.setApiToken(originalToken);
        deleteToken(token.getId());
    }

    @Test(
            groups = {"purge.glossary"},
            dependsOnGroups = {"create.*", "read.*", "update.*"},
            alwaysRun = true)
    void purgeGlossary() throws AtlanException {
        Atlan.setApiToken(originalToken);
        GlossaryTest.deleteTerm(term.getGuid());
        GlossaryTest.deleteGlossary(glossary.getGuid());
    }
}
