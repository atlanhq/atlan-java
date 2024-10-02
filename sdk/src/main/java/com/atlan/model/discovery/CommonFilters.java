/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.discovery;

import java.util.List;

/**
 * Base class for fields that can be used to filter all assets.
 */
public interface CommonFilters {

    /** When true, filter to assets the use has starred and when false to assets that are not starred by the user. */
    BooleanFilterField STARRED = new BooleanFilterField("starredBy");

    /** When true, filter to assets that have lineage and when false to assets that do not have lineage. */
    BooleanFilterField HAS_LINEAGE = new BooleanFilterField("__hasLineage");

    /** When true, filter to assets that have a README and when false to assets without READMEs. */
    BooleanFilterField HAS_README = new BooleanFilterField("readmeFilter");

    /** When true, filter to assets that have one or more links (resources) and when false to assets without links. */
    BooleanFilterField HAS_RESOURCE = new BooleanFilterField("resourcesFilter");

    /** When true, filter to assets that are soft-deleted (archived) and when false to assets that are still active. */
    BooleanFilterField IS_ARCHIVED = new BooleanFilterField("__state");

    /**
     * Filter assets to those with any one of the provided collection of announcement types.
     * To match assets with no announcements, use a value of "none".
     */
    EnumFilterField ANNOUNCEMENT_TYPE = new EnumFilterField("announcementType");

    /** Filter assets to those whose technical or business name matches. */
    StringFilterField NAME = new StringFilterField(List.of("name.keyword", "displayName.keyword"));

    /** Filter assets to those whose UUID matches. */
    ExactMatchFilterField GUID = new ExactMatchFilterField("__guid");

    /** Filter assets to those whose unique qualifiedName matches. */
    StringFilterField QUALIFIED_NAME = new StringFilterField("qualifiedName");

    /** Filter assets to those whose last update at source matches. */
    DateFilterField SOURCE_UPDATED_AT = new DateFilterField("sourceUpdatedAt");

    /** Filter assets to those whose create time at source matches. */
    DateFilterField SOURCE_CREATED_AT = new DateFilterField("sourceCreatedAt");

    /** Filter assets to those whose last update time (in Atlan) matches. */
    DateFilterField UPDATE_TIME = new DateFilterField("__modificationTimestamp.date");

    /** Filter assets to those whose creation time (in Atlan) matches. */
    DateFilterField CREATE_TIME = new DateFilterField("__timestamp");

    /** Filter assets to those that whose last updating user matches. */
    StrictEqualityFilterField UPDATED_BY = new StrictEqualityFilterField("__modifiedBy");

    /** Filter assets to those that whose creating user matches. */
    StrictEqualityFilterField CREATED_BY = new StrictEqualityFilterField("__createdBy");

    /** Filter assets whose description matches. */
    StringFilterField DESCRIPTION = new StringFilterField(List.of("description.keyword", "userDescription.keyword"));
}
