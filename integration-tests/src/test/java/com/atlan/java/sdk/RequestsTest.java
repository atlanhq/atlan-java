/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.AtlanClient;
import com.atlan.exception.AtlanException;
import com.atlan.exception.AuthenticationException;
import com.atlan.exception.NotFoundException;
import com.atlan.model.admin.*;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.enums.AtlanConnectorType;
import com.atlan.net.HttpClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Test management of requests.
 */
@Slf4j
public class RequestsTest extends AtlanLiveTest {

    private static final String PREFIX = makeUnique("Requests");
    private static final String API_TOKEN_NAME = PREFIX;
    private static final String GLOSSARY_NAME = PREFIX;
    private static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.COGNITE;
    private static final String ATLAN_TAG_NAME = PREFIX;
    private static final String TERM_NAME = PREFIX + " term";

    private static final String ATTR_VALUE_DESCRIPTION = "A new description, as requested.";

    private static AtlanClient requestsClient = null;
    private static ApiToken token = null;
    private static Glossary glossary = null;
    private static Connection connection = null;
    private static GlossaryTerm term = null;
    private static Database database = null;
    private static String attributeRequestGuid = null;
    private static String termLinkRequestGuid = null;
    private static String atlanTagRequestGuid = null;

    private static List<String> taggedAssetGuids = new ArrayList<>();

    /**
     * Create a new API token with a unique name.
     *
     * @param client connectivity to the Atlan tenant
     * @param name to make the token unique
     * @return the token that was created
     * @throws AtlanException on any error creating or reading-back the group
     */
    static ApiToken createToken(AtlanClient client, String name) throws AtlanException {
        ApiToken created = ApiToken.create(client, name);
        assertNotNull(created);
        assertEquals(created.getDisplayName(), name);
        assertNotNull(created.getAttributes());
        assertNotNull(created.getAttributes().getAccessToken());
        return created;
    }

    /**
     * Delete (purge) an API token based on its GUID.
     *
     * @param client connectivity to the Atlan tenant
     * @param guid of the token to purge
     * @throws AtlanException on any errors purging the group
     */
    static void deleteToken(AtlanClient client, String guid) throws AtlanException {
        ApiToken.delete(client, guid);
    }

    @Test(groups = {"request.create.glossary"})
    void createGlossary() throws AtlanException {
        glossary = GlossaryTest.createGlossary(client, GLOSSARY_NAME);
        term = GlossaryTest.createTerm(client, TERM_NAME, glossary);
        taggedAssetGuids.add(term.getGuid());
    }

    @Test(groups = {"request.create.atlantag"})
    void createAtlanTag() throws AtlanException {
        AtlanTagTest.createAtlanTag(client, ATLAN_TAG_NAME);
    }

    @Test(groups = {"request.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, PREFIX, CONNECTOR_TYPE);
        Database toCreate =
                Database.creator(PREFIX, connection.getQualifiedName()).build();
        AssetMutationResponse response = toCreate.save(client);
        assertNotNull(response);
        assertNotNull(response.getCreatedAssets());
        assertEquals(response.getCreatedAssets().size(), 1);
        assertTrue(response.getCreatedAssets().get(0) instanceof Database);
        database = (Database) response.getCreatedAssets().get(0);
    }

    @Test(groups = {"request.create.token"})
    void createToken() throws AtlanException {
        token = createToken(client, API_TOKEN_NAME);
        assertNotNull(token.getClientId());
        String requestsToken = token.getAttributes().getAccessToken();
        requestsClient = new AtlanClient(System.getenv("ATLAN_BASE_URL"), requestsToken);
    }

    @Test(
            groups = {"request.read.token"},
            dependsOnGroups = {"request.create.token"})
    void retrieveTokens() throws AtlanException {
        ApiTokenResponse response = client.apiTokens.list();
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
            groups = {"request.read.token"},
            dependsOnGroups = {"request.create.token"})
    void retrieveTokenByName() throws AtlanException {
        ApiToken retrieved = ApiToken.retrieveByName(client, API_TOKEN_NAME);
        assertNotNull(retrieved);
        assertEquals(retrieved.getDisplayName(), API_TOKEN_NAME);
        assertNotNull(retrieved.getId());
        token = retrieved;
    }

    @Test(
            groups = {"request.update.token"},
            dependsOnGroups = {"request.read.token"})
    void updateToken() throws AtlanException {
        final String description = "Now with a revised description.";
        ApiToken revised = token.toBuilder()
                .attributes(token.getAttributes().toBuilder()
                        .description(description)
                        .build())
                .build();
        ApiToken updated = revised.update(client);
        assertNotNull(updated);
        assertNotNull(updated.getAttributes());
        assertEquals(updated.getAttributes().getDescription(), description);
        assertEquals(updated.getDisplayName(), API_TOKEN_NAME);
    }

    @Test(
            groups = {"request.create.request"},
            dependsOnGroups = {"request.read.token", "request.create.glossary"})
    void createAttributeRequest() throws AtlanException {
        AttributeRequest toCreate = AttributeRequest.creator(term, "userDescription", ATTR_VALUE_DESCRIPTION)
                .build();
        toCreate.create(requestsClient);
    }

    @Test(
            groups = {"request.create.request"},
            dependsOnGroups = {"request.read.token", "request.create.glossary", "request.create.connection"})
    void createTermLinkRequest() throws AtlanException {
        TermLinkRequest toCreate = TermLinkRequest.creator(database, term).build();
        toCreate.create(requestsClient);
    }

    @Test(
            groups = {"request.create.request"},
            dependsOnGroups = {"request.read.token", "request.create.glossary"})
    void createAtlanTagRequest() throws AtlanException, InterruptedException {
        // Force a cache refresh to ensure this client has the tag name available
        String tagId = null;
        int count = 0;
        int maxTries = requestsClient.getMaxNetworkRetries() * 4;
        while (tagId == null && count < maxTries) {
            try {
                requestsClient.getAtlanTagCache().forceRefresh();
                tagId = requestsClient.getAtlanTagCache().getSidForName(ATLAN_TAG_NAME);
            } catch (NotFoundException | AuthenticationException e) {
                log.debug("Atlan tag not yet visible to secondary client, retrying...");
            }
            Thread.sleep(HttpClient.waitTime(count).toMillis());
            count++;
        }
        AtlanTagRequest toCreate = AtlanTagRequest.creator(term, AtlanTagPayload.of(ATLAN_TAG_NAME))
                .build();
        toCreate.create(requestsClient);
    }

    @Test(
            groups = {"request.read.requests"},
            dependsOnGroups = {"request.create.request"},
            alwaysRun = true)
    void readRequests() throws AtlanException {
        String createdBy = requestsClient.users.getCurrentUser().getUsername();
        AtlanRequestResponse response = client.requests.list("{\"createdBy\":\"" + createdBy + "\"}");
        assertNotNull(response);
        assertTrue(response.getTotalRecord() > 0);
        for (AtlanRequest request : response.getRecords()) {
            if (request instanceof AttributeRequest) {
                if (term.getGuid().equals(request.getDestinationGuid())
                        && request.getDestinationAttribute().equals("userDescription")) {
                    attributeRequestGuid = request.getId();
                }
            } else if (request instanceof TermLinkRequest) {
                if (database.getGuid().equals(request.getDestinationGuid())
                        && term.getGuid().equals(request.getSourceGuid())) {
                    termLinkRequestGuid = request.getId();
                }
            } else if (request instanceof AtlanTagRequest) {
                if (term.getGuid().equals(request.getDestinationGuid())) {
                    AtlanTagPayload payload = ((AtlanTagRequest) request).getPayload();
                    if (ATLAN_TAG_NAME.equals(payload.getTypeName())) {
                        atlanTagRequestGuid = request.getId();
                    }
                }
            }
        }
    }

    @Test(
            groups = {"request.read.request"},
            dependsOnGroups = {"request.read.requests"},
            alwaysRun = true)
    void readRequest() throws AtlanException {
        AtlanRequest response = client.requests.get(attributeRequestGuid);
        assertTrue(response instanceof AttributeRequest);
        response = client.requests.get(termLinkRequestGuid);
        assertTrue(response instanceof TermLinkRequest);
        response = client.requests.get(atlanTagRequestGuid);
        assertTrue(response instanceof AtlanTagRequest);
    }

    @Test(
            groups = {"request.approve.request"},
            dependsOnGroups = {"request.read.request"},
            alwaysRun = true)
    void approveAttributeRequest() throws AtlanException {
        assertTrue(client.requests.approve(attributeRequestGuid, "Description change approved!"));
    }

    @Test(
            groups = {"request.approve.request"},
            dependsOnGroups = {"request.read.request"},
            alwaysRun = true)
    void approveAtlanTagRequest() throws AtlanException {
        assertTrue(client.requests.approve(atlanTagRequestGuid, "Atlan tag approved!"));
    }

    @Test(
            groups = {"request.approve.request"},
            dependsOnGroups = {"request.read.request"},
            alwaysRun = true)
    void approveTermLinkRequest() throws AtlanException {
        assertTrue(client.requests.approve(termLinkRequestGuid, "Term link approved!"));
    }

    @Test(
            groups = {"request.read.term"},
            dependsOnGroups = {"request.approve.request"},
            alwaysRun = true)
    void readTerm() throws AtlanException, InterruptedException {
        waitForTagsToSync(taggedAssetGuids, log);
        GlossaryTerm revised = GlossaryTerm.get(client, term.getGuid(), true);
        assertNotNull(revised);
        assertEquals(revised.getUserDescription(), ATTR_VALUE_DESCRIPTION);
        assertNotNull(revised.getAtlanTags());
        assertEquals(revised.getAtlanTags().size(), 1);
        AtlanTag only = new ArrayList<>(revised.getAtlanTags()).get(0);
        assertEquals(only.getTypeName(), ATLAN_TAG_NAME);
        assertTrue(only.getPropagate());
        assertTrue(only.getRemovePropagationsOnEntityDelete());
        assertEquals(only.getEntityGuid(), term.getGuid());
        Set<IAsset> assets = revised.getAssignedEntities();
        assertNotNull(assets);
        assertEquals(assets.size(), 1);
        IAsset one = new ArrayList<>(assets).get(0);
        assertTrue(one instanceof Database);
        assertEquals(one.getGuid(), database.getGuid());
    }

    @Test(
            groups = {"request.purge.token"},
            dependsOnGroups = {"request.create.*", "request.read.*", "request.update.*"},
            alwaysRun = true)
    void purgeToken() throws AtlanException {
        if (token != null) {
            deleteToken(client, token.getId());
        } else {
            ApiToken local = client.apiTokens.get(API_TOKEN_NAME);
            RequestsTest.deleteToken(client, local.getId());
        }
    }

    @Test(
            groups = {"request.purge.connection"},
            dependsOnGroups = {"request.create.*", "request.read.*", "request.update.*"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
    }

    @Test(
            groups = {"request.purge.glossary"},
            dependsOnGroups = {"request.create.*", "request.read.*", "request.update.*", "request.purge.connection"},
            alwaysRun = true)
    void purgeGlossary() throws AtlanException {
        GlossaryTest.deleteTerm(client, term.getGuid());
        GlossaryTest.deleteGlossary(client, glossary.getGuid());
    }

    @Test(
            groups = {"request.purge.atlantag"},
            dependsOnGroups = {
                "request.create.*",
                "request.read.*",
                "request.update.*",
                "request.purge.glossary",
                "request.purge.connection"
            },
            alwaysRun = true)
    void purgeAtlanTag() throws AtlanException {
        AtlanTagTest.deleteAtlanTag(client, ATLAN_TAG_NAME);
    }

    @Test(
            groups = {"request.purge.client"},
            dependsOnGroups = {
                "request.create.*",
                "request.read.*",
                "request.update.*",
                "request.purge.glossary",
                "request.purge.connection"
            },
            alwaysRun = true)
    void closeTemporaryClient() throws IOException {
        if (requestsClient != null) {
            requestsClient.close();
        }
    }
}
