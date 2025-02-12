/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.java.sdk;

import static com.atlan.java.sdk.ConnectionTest.createConnection;
import static com.atlan.java.sdk.ConnectionTest.deleteConnection;
import static org.testng.Assert.*;

import com.atlan.AtlanClient;
import com.atlan.cache.SourceTagCache;
import com.atlan.exception.AtlanException;
import com.atlan.model.assets.Asset;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.Database;
import com.atlan.model.assets.SnowflakeTag;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.enums.*;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.model.structs.SourceTagAttachment;
import com.atlan.model.structs.SourceTagAttachmentValue;
import com.atlan.model.typedefs.AtlanTagDef;
import com.atlan.model.typedefs.AtlanTagOptions;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.TypeDefResponse;
import com.atlan.net.RequestOptions;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;

/**
 * Utility methods for managing Atlan tags during testing.
 */
@Slf4j
public class AtlanTagTest extends AtlanLiveTest {

    private static final int MAX_TAG_RETRIES = 30;
    private static final String PREFIX = makeUnique("tag");
    private static final String TAG_WITH_IMAGE = PREFIX + "_image";
    private static final String TAG_WITH_ICON = PREFIX + "_icon";
    private static final String TAG_WITH_EMOJI = PREFIX + "_emoji";
    private static final String SOURCE_SYNCED = PREFIX + "_synced";

    private static Connection connection;
    private static SnowflakeTag sourceTag;

    /**
     * Create a new Atlan tag with a unique name.
     *
     * @param client connectivity to the Atlan tenant in which to create the tag
     * @param name to make the Atlan tag unique
     * @throws AtlanException on any error creating or reading-back the Atlan tag
     */
    static void createAtlanTag(AtlanClient client, String name) throws AtlanException {
        AtlanTagDef atlanTagDef = AtlanTagDef.creator(name, AtlanTagColor.GREEN).build();
        TypeDefResponse created = client.typeDefs.create(
                atlanTagDef,
                RequestOptions.from(client).maxNetworkRetries(MAX_TAG_RETRIES).build());
        assertNotNull(created);
        assertNotNull(created.getAtlanTagDefs());
        assertEquals(created.getAtlanTagDefs().size(), 1);
        AtlanTagDef response = created.getAtlanTagDefs().get(0);
        assertNotNull(response);
        assertEquals(response.getCategory(), AtlanTypeCategory.ATLAN_TAG);
        String uniqueName = response.getName();
        assertNotNull(uniqueName);
        assertNotEquals(uniqueName, name);
        assertNotNull(response.getGuid());
        assertEquals(response.getDisplayName(), name);
    }

    /**
     * Delete the Atlan tag with the provided name.
     *
     * @param client connectivity to the Atlan tenant
     * @param name of the Atlan tag to delete
     * @throws AtlanException on any error deleting the Atlan tag
     */
    static void deleteAtlanTag(AtlanClient client, String name) throws AtlanException {
        String internalName = client.getAtlanTagCache().getSidForName(name);
        client.typeDefs.purge(
                internalName,
                RequestOptions.from(client).maxNetworkRetries(MAX_TAG_RETRIES).build());
    }

    @Test(groups = {"tag.create.connection"})
    void createSyncedConnection() throws AtlanException, InterruptedException {
        connection = createConnection(client, PREFIX, AtlanConnectorType.SNOWFLAKE);
    }

    @Test(groups = {"tag.create.image"})
    void createTagWithImage() throws AtlanException {
        AtlanTagDef tag = AtlanTagDef.creator(
                        client,
                        TAG_WITH_IMAGE,
                        "https://github.com/great-expectations/great_expectations/raw/develop/docs/docusaurus/static/img/gx-mark-160.png")
                .build();
        AtlanTagDef response = tag.create(client);
        assertNotNull(response);
        assertEquals(response.getCategory(), AtlanTypeCategory.ATLAN_TAG);
        String uniqueName = response.getName();
        assertNotNull(uniqueName);
        assertNotEquals(uniqueName, TAG_WITH_IMAGE);
        assertNotNull(response.getGuid());
        assertEquals(response.getDisplayName(), TAG_WITH_IMAGE);
        assertNotNull(response.getOptions());
        assertNotNull(response.getOptions().getImageID());
        assertEquals(response.getOptions().getIconType(), TagIconType.IMAGE);
    }

    @Test(groups = {"tag.create.icon"})
    void createTagWithIcon() throws AtlanException {
        AtlanTagDef tag = AtlanTagDef.creator(TAG_WITH_ICON, AtlanIcon.BOOK_BOOKMARK, AtlanTagColor.YELLOW)
                .build();
        AtlanTagDef response = tag.create(client);
        assertNotNull(response);
        assertEquals(response.getCategory(), AtlanTypeCategory.ATLAN_TAG);
        String uniqueName = response.getName();
        assertNotNull(uniqueName);
        assertNotEquals(uniqueName, TAG_WITH_ICON);
        assertNotNull(response.getGuid());
        assertEquals(response.getDisplayName(), TAG_WITH_ICON);
        assertEquals(response.getOptions().getIconType(), TagIconType.ICON);
    }

    @Test(groups = {"tag.create.emoji"})
    void createTagWithEmoji() throws AtlanException {
        AtlanTagDef tag = AtlanTagDef.creator(TAG_WITH_EMOJI, AtlanTagOptions.withEmoji("\uD83D\uDC4D"))
                .build();
        AtlanTagDef response = tag.create(client);
        assertNotNull(response);
        assertEquals(response.getCategory(), AtlanTypeCategory.ATLAN_TAG);
        String uniqueName = response.getName();
        assertNotNull(uniqueName);
        assertNotEquals(uniqueName, TAG_WITH_EMOJI);
        assertNotNull(response.getGuid());
        assertEquals(response.getDisplayName(), TAG_WITH_EMOJI);
        assertEquals(response.getOptions().getIconType(), TagIconType.EMOJI);
    }

    @Test(
            groups = {"tag.create.synced"},
            dependsOnGroups = {"tag.create.connection"})
    void createSyncedTag() throws AtlanException {
        // Tag itself
        AtlanTagDef tag = AtlanTagDef.creator(SOURCE_SYNCED, AtlanIcon.RECYCLE, AtlanTagColor.GREEN, true)
                .build();
        AtlanTagDef response = tag.create(client);
        assertNotNull(response);
        assertEquals(response.getCategory(), AtlanTypeCategory.ATLAN_TAG);
        String uniqueName = response.getName();
        assertNotNull(uniqueName);
        assertNotEquals(uniqueName, SOURCE_SYNCED);
        assertNotNull(response.getGuid());
        assertEquals(response.getDisplayName(), SOURCE_SYNCED);
        assertEquals(response.getOptions().getIconType(), TagIconType.ICON);
        assertEquals(response.getOptions().getHasTag(), true);
        List<AttributeDef> attrs = response.getAttributeDefs();
        assertNotNull(attrs);
        assertEquals(attrs.size(), 1);
        AttributeDef attr = attrs.get(0);
        assertNotNull(attr);
        assertEquals(attr.getDisplayName(), "sourceTagAttachment");
        assertEquals(attr.getTypeName(), "array<SourceTagAttachment>");
        assertEquals(attr.getCardinality(), AtlanCustomAttributeCardinality.SET);

        // Source tag asset
        // TODO: replace with generic SourceTag (once available)
        SnowflakeTag toCreate = SnowflakeTag._internal()
                .guid("-" + ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE - 1))
                .name(SOURCE_SYNCED)
                .qualifiedName(connection.getQualifiedName() + "/" + SOURCE_SYNCED)
                .connectorType(AtlanConnectorType.SNOWFLAKE)
                .connectionQualifiedName(connection.getQualifiedName())
                .connectionName(connection.getName())
                .mappedAtlanTagName(tag.getName())
                .tagAllowedValue("A")
                .tagAllowedValue("B")
                .tagAllowedValue("C")
                .build();
        AssetMutationResponse resp = toCreate.save(client);
        assertNotNull(resp);
        assertNotNull(resp.getCreatedAssets());
        assertEquals(resp.getCreatedAssets().size(), 1);
        Asset one = resp.getCreatedAssets().get(0);
        assertTrue(one instanceof SnowflakeTag);
        sourceTag = (SnowflakeTag) one;
    }

    @Test(
            groups = {"tag.create.asset"},
            dependsOnGroups = {"tag.create.synced"})
    void createAssetWithSourceTag() throws AtlanException {
        Database database = Database.creator(PREFIX, connection.getQualifiedName())
                .atlanTag(AtlanTag.of(
                        SOURCE_SYNCED,
                        SourceTagAttachment.byName(
                                client,
                                new SourceTagCache.SourceTagName(client, sourceTag),
                                List.of(SourceTagAttachmentValue.of(null, "A")))))
                .build();
        AssetMutationResponse response = database.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof Database);
        database = (Database) one;
        assertNotNull(database.getGuid());
        assertNotNull(database.getQualifiedName());
        assertEquals(database.getName(), PREFIX);
    }

    @Test(
            groups = {"tag.read.asset"},
            dependsOnGroups = {"tag.create.asset"})
    void readAssetWithSourceTag() throws AtlanException, InterruptedException {
        IndexSearchRequest request = Database.select(client)
                .taggedWithValue(SOURCE_SYNCED, "A", true)
                .includeOnResults(Asset.ATLAN_TAGS)
                .toRequest();
        IndexSearchResponse response = retrySearchUntil(request, 1L);
        assertNotNull(response);
        assertEquals(response.getApproximateCount(), 1);
        assertEquals(response.getAssets().size(), 1);
        Asset db = response.getAssets().get(0);
        assertTrue(db instanceof Database);
        assertNotNull(db.getAtlanTags());
        assertEquals(db.getAtlanTags().size(), 1);
        AtlanTag one = db.getAtlanTags().first();
        assertNotNull(one);
        assertEquals(one.getTypeName(), SOURCE_SYNCED);
        assertNotNull(one.getSourceTagAttachments());
        assertEquals(one.getSourceTagAttachments().size(), 1);
        SourceTagAttachment sta = one.getSourceTagAttachments().get(0);
        assertNotNull(sta);
        assertEquals(sta.getSourceTagName(), SOURCE_SYNCED);
        assertEquals(sta.getSourceTagQualifiedName(), connection.getQualifiedName() + "/" + SOURCE_SYNCED);
        assertEquals(sta.getSourceTagGuid(), sourceTag.getGuid());
        assertEquals(sta.getSourceTagConnectorName(), AtlanConnectorType.SNOWFLAKE.getValue());
        assertNotNull(sta.getSourceTagValues());
        assertEquals(sta.getSourceTagValues().size(), 1);
        assertEquals(sta.getSourceTagValues().get(0).getTagAttachmentValue(), "A");
        assertNull(sta.getSourceTagValues().get(0).getTagAttachmentKey());
    }

    @Test(
            groups = {"tag.purge.connection"},
            dependsOnGroups = {"tag.create.*", "tag.read.*"},
            alwaysRun = true)
    void purgeConnection() throws AtlanException, InterruptedException {
        deleteConnection(client, connection.getQualifiedName(), log);
    }

    @Test(
            groups = {"tag.purge.tags"},
            dependsOnGroups = {"tag.create.*", "tag.purge.connection", "tag.read.*"},
            alwaysRun = true)
    void purgeTags() throws AtlanException {
        deleteAtlanTag(client, TAG_WITH_ICON);
        deleteAtlanTag(client, TAG_WITH_EMOJI);
        deleteAtlanTag(client, TAG_WITH_IMAGE);
        deleteAtlanTag(client, SOURCE_SYNCED);
    }
}
