/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.live;

import static org.testng.Assert.*;

import com.atlan.Atlan;
import com.atlan.api.ApiTokensEndpoint;
import com.atlan.exception.AtlanException;
import com.atlan.model.admin.*;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.Classification;
import com.atlan.model.enums.AtlanConnectorType;
import java.util.ArrayList;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Test management of requests.
 */
@Slf4j
public class RequestsTest extends AtlanLiveTest {

    private static final String PREFIX = AtlanLiveTest.PREFIX + "Requests";
    private static final String API_TOKEN_NAME = PREFIX;
    private static final String GLOSSARY_NAME = PREFIX;
    private static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.AIRFLOW;
    private static final String CLASSIFICATION_NAME = PREFIX;
    private static final String TERM_NAME = PREFIX + " term";

    private static final String ATTR_VALUE_DESCRIPTION = "A new description, as requested.";

    private static final String originalToken = Atlan.getApiToken();
    private static String requestsToken = null;
    private static ApiToken token = null;
    private static Glossary glossary = null;
    private static Connection connection = null;
    private static GlossaryTerm term = null;
    private static Database database = null;
    private static String attributeRequestGuid = null;
    private static String termLinkRequestGuid = null;
    private static String classificationRequestGuid = null;

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

    @Test(groups = {"request.create.glossary"})
    void createGlossary() throws AtlanException {
        Atlan.setApiToken(originalToken);
        glossary = GlossaryTest.createGlossary(GLOSSARY_NAME);
        term = GlossaryTest.createTerm(TERM_NAME, glossary.getGuid());
    }

    @Test(groups = {"request.create.classification"})
    void createClassification() throws AtlanException {
        Atlan.setApiToken(originalToken);
        ClassificationTest.createClassification(CLASSIFICATION_NAME);
    }

    @Test(groups = {"request.create.connection"})
    void createConnection() throws AtlanException {
        Atlan.setApiToken(originalToken);
        connection = ConnectionTest.createConnection(PREFIX, CONNECTOR_TYPE);
        Database toCreate =
                Database.creator(PREFIX, connection.getQualifiedName()).build();
        AssetMutationResponse response = toCreate.upsert();
        assertNotNull(response);
        assertNotNull(response.getCreatedAssets());
        assertEquals(response.getCreatedAssets().size(), 1);
        assertTrue(response.getCreatedAssets().get(0) instanceof Database);
        database = (Database) response.getCreatedAssets().get(0);
    }

    @Test(groups = {"request.create.token"})
    void createToken() throws AtlanException {
        token = createToken(API_TOKEN_NAME);
        assertNotNull(token.getClientId());
        requestsToken = token.getAttributes().getAccessToken();
    }

    @Test(
            groups = {"request.read.token"},
            dependsOnGroups = {"request.create.token"})
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
            groups = {"request.read.token"},
            dependsOnGroups = {"request.create.token"})
    void retrieveTokenByName() throws AtlanException {
        ApiToken retrieved = ApiToken.retrieveByName(API_TOKEN_NAME);
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
        ApiToken updated = revised.update();
        assertNotNull(updated);
        assertNotNull(updated.getAttributes());
        assertEquals(updated.getAttributes().getDescription(), description);
        assertEquals(updated.getDisplayName(), API_TOKEN_NAME);
    }

    @Test(
            groups = {"request.create.request"},
            dependsOnGroups = {"request.read.token", "request.create.glossary"})
    void createAttributeRequest() throws AtlanException {
        Atlan.setApiToken(requestsToken);
        AttributeRequest toCreate = AttributeRequest.creator(
                        term.getGuid(),
                        term.getQualifiedName(),
                        GlossaryTerm.TYPE_NAME,
                        "userDescription",
                        ATTR_VALUE_DESCRIPTION)
                .build();
        toCreate.create();
    }

    @Test(
            groups = {"request.create.request"},
            dependsOnGroups = {"request.read.token", "request.create.glossary", "request.create.connection"})
    void createTermLinkRequest() throws AtlanException {
        Atlan.setApiToken(requestsToken);
        TermLinkRequest toCreate = TermLinkRequest.creator(
                        database.getGuid(),
                        database.getQualifiedName(),
                        Database.TYPE_NAME,
                        term.getGuid(),
                        term.getQualifiedName())
                .build();
        toCreate.create();
    }

    @Test(
            groups = {"request.create.request"},
            dependsOnGroups = {"request.read.token", "request.create.glossary"})
    void createClassificationRequest() throws AtlanException {
        Atlan.setApiToken(requestsToken);
        ClassificationRequest toCreate = ClassificationRequest.creator(
                        term.getGuid(),
                        term.getQualifiedName(),
                        GlossaryTerm.TYPE_NAME,
                        ClassificationPayload.of(CLASSIFICATION_NAME))
                .build();
        toCreate.create();
    }

    @Test(
            groups = {"request.read.requests"},
            dependsOnGroups = {"request.create.request"},
            alwaysRun = true)
    void readRequests() throws AtlanException {
        Atlan.setApiToken(originalToken);
        AtlanRequestResponse response = AtlanRequest.list();
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
            } else if (request instanceof ClassificationRequest) {
                if (term.getGuid().equals(request.getDestinationGuid())) {
                    ClassificationPayload payload = ((ClassificationRequest) request).getPayload();
                    if (CLASSIFICATION_NAME.equals(payload.getTypeName())) {
                        classificationRequestGuid = request.getId();
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
        Atlan.setApiToken(originalToken);
        AtlanRequest response = AtlanRequest.retrieveByGuid(attributeRequestGuid);
        assertTrue(response instanceof AttributeRequest);
        response = AtlanRequest.retrieveByGuid(classificationRequestGuid);
        assertTrue(response instanceof ClassificationRequest);
        response = AtlanRequest.retrieveByGuid(termLinkRequestGuid);
        assertTrue(response instanceof TermLinkRequest);
    }

    @Test(
            groups = {"request.approve.request"},
            dependsOnGroups = {"request.read.request"},
            alwaysRun = true)
    void approveAttributeRequest() throws AtlanException {
        Atlan.setApiToken(originalToken);
        assertTrue(AtlanRequest.approve(attributeRequestGuid, "Description change approved!"));
    }

    @Test(
            groups = {"request.approve.request"},
            dependsOnGroups = {"request.read.request"},
            alwaysRun = true)
    void approveClassificationRequest() throws AtlanException {
        Atlan.setApiToken(originalToken);
        assertTrue(AtlanRequest.approve(classificationRequestGuid, "Classification approved!"));
    }

    @Test(
            groups = {"request.approve.request"},
            dependsOnGroups = {"request.read.request"},
            alwaysRun = true)
    void approveTermLinkRequest() throws AtlanException {
        Atlan.setApiToken(originalToken);
        assertTrue(AtlanRequest.approve(termLinkRequestGuid, "Term link approved!"));
    }

    @Test(
            groups = {"request.read.term"},
            dependsOnGroups = {"request.approve.request"},
            alwaysRun = true)
    void readTerm() throws AtlanException {
        Atlan.setApiToken(originalToken);
        GlossaryTerm revised = GlossaryTerm.retrieveByGuid(term.getGuid());
        assertNotNull(revised);
        assertEquals(revised.getUserDescription(), ATTR_VALUE_DESCRIPTION);
        assertNotNull(revised.getClassifications());
        assertEquals(revised.getClassifications().size(), 1);
        Classification only = new ArrayList<>(revised.getClassifications()).get(0);
        assertEquals(only.getTypeName(), CLASSIFICATION_NAME);
        assertTrue(only.getPropagate());
        assertFalse(only.getRemovePropagationsOnEntityDelete());
        assertEquals(only.getEntityGuid(), term.getGuid());
        Set<Asset> assets = revised.getAssignedEntities();
        assertNotNull(assets);
        assertEquals(assets.size(), 1);
        Asset one = new ArrayList<>(assets).get(0);
        assertTrue(one instanceof Database);
        assertEquals(one.getGuid(), database.getGuid());
    }

    @Test(
            groups = {"request.purge.token"},
            dependsOnGroups = {"request.create.*", "request.read.*", "request.update.*"},
            alwaysRun = true)
    void purgeToken() throws AtlanException {
        Atlan.setApiToken(originalToken);
        deleteToken(token.getId());
    }

    @Test(
            groups = {"request.purge.connection"},
            dependsOnGroups = {"request.create.*", "request.read.*", "request.update.*"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(connection.getQualifiedName(), log);
    }

    @Test(
            groups = {"request.purge.glossary"},
            dependsOnGroups = {"request.create.*", "request.read.*", "request.update.*", "request.purge.connection"},
            alwaysRun = true)
    void purgeGlossary() throws AtlanException {
        Atlan.setApiToken(originalToken);
        GlossaryTest.deleteTerm(term.getGuid());
        GlossaryTest.deleteGlossary(glossary.getGuid());
    }

    @Test(
            groups = {"request.purge.classification"},
            dependsOnGroups = {
                "request.create.*",
                "request.read.*",
                "request.update.*",
                "request.purge.glossary",
                "request.purge.connection"
            },
            alwaysRun = true)
    void purgeClassification() throws AtlanException {
        Atlan.setApiToken(originalToken);
        ClassificationTest.deleteClassification(CLASSIFICATION_NAME);
    }
}
