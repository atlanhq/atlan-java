/* SPDX-License-Identifier: Apache-2.0
   Copyright 2025 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static org.testng.Assert.*;

import com.atlan.exception.AtlanException;
import com.atlan.model.assets.*;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.enums.AtlanConnectorType;
import java.util.SortedSet;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

@Slf4j
public class LinkTest extends AtlanLiveTest {
    private static final String PREFIX = makeUnique("Link");

    public static final AtlanConnectorType CONNECTOR_TYPE = AtlanConnectorType.POSTGRES;
    public static final String CONNECTION_NAME = PREFIX;
    private static final String DATABASE_NAME = PREFIX + "_db";
    public static final String LINK_TITLE = "hp";
    public static final String LINK_URL = "https://hp.com";
    private static Connection connection = null;
    private static Database database = null;
    private static String nonIdempotentLinkGuid = null;

    @Test(groups = {"link.create.connection"})
    void createConnection() throws AtlanException, InterruptedException {
        connection = ConnectionTest.createConnection(client, CONNECTION_NAME, CONNECTOR_TYPE);
        database = SQLAssetTest.createDatabase(client, DATABASE_NAME, connection.getQualifiedName());
        assertNotNull(database);
    }

    @Test(
            groups = {"link.create.idempotent"},
            dependsOnGroups = {"link.create.connection"})
    void whenIdempotentLinkCreatedAssociatedWithAssetThenQualifiedNameStartsWithAssetQualifiedName()
            throws AtlanException {
        Link link = Link.creator(Database.refByQualifiedName(database.getQualifiedName()), LINK_TITLE, LINK_URL, true)
                .build();
        link = createLink(link);

        assertTrue(link.getQualifiedName().startsWith(database.getQualifiedName()));
        database = Database.get(client, database.getGuid(), true);
        SortedSet<ILink> links = database.getLinks();
        assertEquals(1, links.size());
    }

    private Link createLink(Link link) throws AtlanException {
        AssetMutationResponse response = link.save(client);
        assertNotNull(response);
        assertEquals(response.getCreatedAssets().size(), 1);
        Asset asset = response.getCreatedAssets().get(0);
        assertTrue(asset instanceof Link);
        link = (Link) asset;
        return link;
    }

    @Test(
            groups = {"link.create.nonidempotent"},
            dependsOnGroups = {"link.create.idempotent"})
    void whenNonIdempotentLinkCreatedAssociatedWithAssetThenQualifiedNameIsUUID() throws AtlanException {
        Link link = Link.creator(Database.refByQualifiedName(database.getQualifiedName()), LINK_TITLE, LINK_URL, false)
                .build();
        link = createLink(link);

        nonIdempotentLinkGuid = link.getGuid();
        String qualifiedName = link.getQualifiedName();
        assertFalse(qualifiedName.startsWith(database.getQualifiedName()));
        assertEquals(UUID.fromString(qualifiedName).toString(), qualifiedName);
        database = Database.get(client, database.getGuid(), true);
        SortedSet<ILink> links = database.getLinks();
        assertEquals(2, links.size());
    }

    @Test(
            groups = {"link.purge.connection"},
            dependsOnGroups = {"link.create.*"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        ConnectionTest.deleteConnection(client, connection.getQualifiedName(), log);
        Link.purge(client, nonIdempotentLinkGuid);
    }
}
