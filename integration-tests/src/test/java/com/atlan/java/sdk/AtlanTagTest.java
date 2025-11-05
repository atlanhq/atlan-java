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
import com.atlan.model.assets.SourceTag;
import com.atlan.model.core.AssetMutationResponse;
import com.atlan.model.core.AtlanAsyncMutator;
import com.atlan.model.core.AtlanTag;
import com.atlan.model.enums.*;
import com.atlan.model.relations.Reference;
import com.atlan.model.search.IndexSearchRequest;
import com.atlan.model.search.IndexSearchResponse;
import com.atlan.model.structs.SourceTagAttachment;
import com.atlan.model.structs.SourceTagAttachmentValue;
import com.atlan.model.tasks.AtlanTask;
import com.atlan.model.tasks.TaskSearchRequest;
import com.atlan.model.typedefs.AtlanTagDef;
import com.atlan.model.typedefs.AtlanTagOptions;
import com.atlan.model.typedefs.AttributeDef;
import com.atlan.model.typedefs.TypeDefResponse;
import com.atlan.net.HttpClient;
import com.atlan.net.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    private static SourceTag sourceTag;
    private static Database database;

    private static List<String> taggedAssetGuids = new ArrayList<>();

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
        connection = createConnection(client, PREFIX, AtlanConnectorType.TREASURE_DATA);
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
    void createSyncedTag() throws AtlanException, InterruptedException {
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
        SourceTag toCreate = SourceTag.creator(
                        SOURCE_SYNCED, connection.getQualifiedName(), SOURCE_SYNCED, "42", List.of("A", "B", "C"))
                .build();
        AssetMutationResponse resp = toCreate.save(client);
        assertNotNull(resp);
        assertNotNull(resp.getCreatedAssets());
        assertEquals(resp.getCreatedAssets().size(), 1);
        Asset one = resp.getCreatedAssets().get(0);
        assertTrue(one instanceof SourceTag);
        sourceTag = (SourceTag) one;
        int retryCount = 0;
        Asset src = null;
        while (src == null && retryCount < (client.getMaxNetworkRetries() * 2)) {
            Thread.sleep(HttpClient.waitTime(retryCount).toMillis());
            try {
                src = client.getSourceTagCache().getByName(new SourceTagCache.SourceTagName(client, sourceTag), true);
            } catch (AtlanException e) {
                log.info("Source tag '{}' not yet found to cache, retrying...", sourceTag.getName());
            }
            retryCount++;
        }
    }

    @Test(
            groups = {"tag.create.asset"},
            dependsOnGroups = {"tag.create.synced"})
    void createAssetWithSourceTag() throws AtlanException, InterruptedException {
        Database db = Database.creator(PREFIX, connection.getQualifiedName())
                .atlanTag(AtlanTag.of(
                        SOURCE_SYNCED,
                        SourceTagAttachment.byName(
                                client,
                                new SourceTagCache.SourceTagName(client, sourceTag),
                                List.of(SourceTagAttachmentValue.of(null, "A")))))
                .build();
        AssetMutationResponse response = db.save(client);
        Asset one = validateSingleCreate(response);
        assertTrue(one instanceof Database);
        database = (Database) one;
        assertNotNull(db.getGuid());
        assertNotNull(db.getQualifiedName());
        assertEquals(db.getName(), PREFIX);
        taggedAssetGuids.add(database.getGuid());
    }

    @Test(
            groups = {"tag.read.asset"},
            dependsOnGroups = {"tag.create.asset"})
    void readAssetWithSourceTag() throws AtlanException, InterruptedException {
        validateSingleTag("A");
    }

    @Test(
            groups = {"tag.manage.append1"},
            dependsOnGroups = {"tag.read.asset"})
    void appendTagWithOtherValueToAsset() throws AtlanException {
        Database db = Database.updater(database.getQualifiedName(), database.getName())
                .atlanTag(AtlanTag.of(
                        SOURCE_SYNCED,
                        Reference.SaveSemantic.APPEND,
                        SourceTagAttachment.byName(
                                client,
                                new SourceTagCache.SourceTagName(client, sourceTag),
                                List.of(SourceTagAttachmentValue.of(null, "B")))))
                .build();
        AssetMutationResponse response = db.save(client);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Database);
        db = (Database) one;
        assertEquals(db.getGuid(), database.getGuid());
    }

    @Test(
            groups = {"tag.read.asset2"},
            dependsOnGroups = {"tag.manage.append1"})
    void readAssetWithReplacedValue() throws AtlanException, InterruptedException {
        validateSingleTag("B");
    }

    @Test(
            groups = {"tag.manage.append2"},
            dependsOnGroups = {"tag.read.asset2"})
    void appendTagToAsset() throws AtlanException {
        Database db = Database.updater(database.getQualifiedName(), database.getName())
                .atlanTag(AtlanTag.of(TAG_WITH_ICON, Reference.SaveSemantic.APPEND))
                .build();
        AssetMutationResponse response = db.save(client);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Database);
        db = (Database) one;
        assertEquals(db.getGuid(), database.getGuid());
    }

    @Test(
            groups = {"tag.read.asset3"},
            dependsOnGroups = {"tag.manage.append2"})
    void readAssetWithTwoTags() throws AtlanException, InterruptedException {
        waitForTagsToSync(taggedAssetGuids, log);
        IndexSearchRequest request = Database.select(client)
                .tagged(List.of(TAG_WITH_ICON))
                .includeOnResults(Asset.ATLAN_TAGS)
                .toRequest();
        IndexSearchResponse response = retrySearchUntil(request, 1L);
        assertNotNull(response);
        assertEquals(response.getApproximateCount(), 1);
        assertEquals(response.getAssets().size(), 1);
        Asset db = response.getAssets().get(0);
        assertTrue(db instanceof Database);
        assertNotNull(db.getAtlanTags());
        assertEquals(db.getAtlanTags().size(), 2);
        assertEquals(
                db.getAtlanTags().stream().map(AtlanTag::getTypeName).collect(Collectors.toSet()),
                Set.of(SOURCE_SYNCED, TAG_WITH_ICON));
        db.getAtlanTags().forEach(atlanTag -> {
            if (atlanTag.getTypeName().equals(SOURCE_SYNCED)) {
                assertEquals(atlanTag.getTypeName(), SOURCE_SYNCED);
                assertNotNull(atlanTag.getSourceTagAttachments());
                assertEquals(atlanTag.getSourceTagAttachments().size(), 1);
                SourceTagAttachment sta = atlanTag.getSourceTagAttachments().get(0);
                assertNotNull(sta);
                assertEquals(sta.getSourceTagName(), SOURCE_SYNCED);
                assertEquals(sta.getSourceTagQualifiedName(), sourceTag.getQualifiedName());
                assertEquals(sta.getSourceTagGuid(), sourceTag.getGuid());
                assertEquals(sta.getSourceTagConnectorName(), AtlanConnectorType.TREASURE_DATA.getValue());
                assertNotNull(sta.getSourceTagValues());
                assertEquals(sta.getSourceTagValues().size(), 1);
                assertEquals(sta.getSourceTagValues().get(0).getTagAttachmentValue(), "B");
                assertNull(sta.getSourceTagValues().get(0).getTagAttachmentKey());
            }
        });
    }

    @Test(
            groups = {"tag.manage.remove"},
            dependsOnGroups = {"tag.read.asset3"})
    void removeTagFromAsset() throws AtlanException {
        Database db = Database.updater(database.getQualifiedName(), database.getName())
                .atlanTag(AtlanTag.of(TAG_WITH_ICON, Reference.SaveSemantic.REMOVE))
                .build();
        AssetMutationResponse response = db.save(client);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Database);
        db = (Database) one;
        assertEquals(db.getGuid(), database.getGuid());
    }

    @Test(
            groups = {"tag.read.asset4"},
            dependsOnGroups = {"tag.manage.remove"})
    void readAssetWithoutTag() throws AtlanException, InterruptedException {
        validateSingleTag("B");
    }

    @Test(
            groups = {"tag.manage.replace"},
            dependsOnGroups = {"tag.read.asset4"})
    void replaceTagOnAsset() throws AtlanException {
        Database db = Database.updater(database.getQualifiedName(), database.getName())
                .atlanTag(AtlanTag.of(TAG_WITH_EMOJI))
                .build();
        AssetMutationResponse response = db.save(client);
        Asset one = validateSingleUpdate(response);
        assertTrue(one instanceof Database);
        db = (Database) one;
        assertEquals(db.getGuid(), database.getGuid());
    }

    @Test(
            groups = {"tag.read.asset5"},
            dependsOnGroups = {"tag.manage.replace"})
    void readAssetWithReplacedTag() throws AtlanException, InterruptedException {
        waitForTagsToSync(taggedAssetGuids, log);
        IndexSearchRequest request = Database.select(client)
                .tagged(List.of(TAG_WITH_EMOJI))
                .includeOnResults(Asset.ATLAN_TAGS)
                .toRequest();
        // For some reason this can take a very long time to become consistent in the index
        IndexSearchResponse response = retrySearchUntil(request, 1L, client.getMaxNetworkRetries() * 4);
        assertNotNull(response);
        assertEquals(response.getApproximateCount(), 1);
        assertEquals(response.getAssets().size(), 1);
        Asset db = response.getAssets().get(0);
        assertTrue(db instanceof Database);
        assertNotNull(db.getAtlanTags());
        assertEquals(db.getAtlanTags().size(), 1);
        AtlanTag one = db.getAtlanTags().first();
        assertNotNull(one);
        assertEquals(one.getTypeName(), TAG_WITH_EMOJI);
    }

    private void validateSingleTag(String value) throws AtlanException, InterruptedException {
        waitForTagsToSync(taggedAssetGuids, log);
        IndexSearchRequest request = Database.select(client)
                .taggedWithValue(SOURCE_SYNCED, value, true)
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
        assertEquals(sta.getSourceTagQualifiedName(), sourceTag.getQualifiedName());
        assertEquals(sta.getSourceTagGuid(), sourceTag.getGuid());
        assertEquals(sta.getSourceTagConnectorName(), AtlanConnectorType.TREASURE_DATA.getValue());
        assertNotNull(sta.getSourceTagValues());
        assertEquals(sta.getSourceTagValues().size(), 1);
        assertEquals(sta.getSourceTagValues().get(0).getTagAttachmentValue(), value);
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
