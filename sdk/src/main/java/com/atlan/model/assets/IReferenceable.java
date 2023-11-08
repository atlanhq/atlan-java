/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.core.AtlanTag;
import com.atlan.model.core.CustomMetadataAttributes;
import com.atlan.model.enums.AtlanStatus;
import com.atlan.model.fields.*;
import java.util.Map;
import java.util.SortedSet;

/**
 * Base class for all entities.
 */
public interface IReferenceable {

    /** Type of the asset. For example Table, Column, and so on. */
    InternalKeywordTextField TYPE_NAME =
            new InternalKeywordTextField("typeName", "__typeName", "__typeName.keyword", "__typeName");

    /** Globally unique identifier (GUID) of any object in Atlan. */
    InternalKeywordField GUID = new InternalKeywordField("guid", "__guid", "__guid");

    /** Atlan user who created this asset. */
    InternalKeywordField CREATED_BY = new InternalKeywordField("createdBy", "__createdBy", "__createdBy");

    /** Atlan user who last updated the asset. */
    InternalKeywordField UPDATED_BY = new InternalKeywordField("updatedBy", "__modifiedBy", "__modifiedBy");

    /** Asset status in Atlan (active vs deleted). */
    InternalKeywordField STATUS = new InternalKeywordField("status", "__state", "__state");

    /** All directly-assigned Atlan tags that exist on an asset, searchable by internal hashed-string ID of the Atlan tag. */
    InternalKeywordTextField ATLAN_TAGS = new InternalKeywordTextField(
            "classifications", "__classificationNames", "__traitNames", "__classificationsText");

    /** All propagated Atlan tags that exist on an asset, searchable by internal hashed-string ID of the Atlan tag. */
    InternalKeywordTextField PROPAGATED_ATLAN_TAGS = new InternalKeywordTextField(
            "classifications", "__propagatedClassificationNames", "__propagatedTraitNames", "__classificationsText");

    /** All terms attached to an asset, searchable by the term's qualifiedName. */
    InternalKeywordTextField ASSIGNED_TERMS =
            new InternalKeywordTextField("meanings", "__meanings", "__meanings", "__meaningsText");

    /** All super types of an asset. */
    InternalKeywordTextField SUPER_TYPE_NAMES = new InternalKeywordTextField(
            "typeName", "__superTypeNames", "__superTypeNames.keyword", "__superTypeNames");

    /** Time (in milliseconds) when the asset was created. */
    InternalNumericField CREATE_TIME = new InternalNumericField("createTime", "__timestamp", "__timestamp");

    /** Time (in milliseconds) when the asset was last updated. */
    InternalNumericField UPDATE_TIME =
            new InternalNumericField("updateTime", "__modificationTimestamp", "__modificationTimestamp");

    /** Name of the type that defines the entity. */
    String getTypeName();

    /** Globally-unique identifier for the entity. */
    String getGuid();

    /**
     * Unique name for this asset. This is typically a concatenation of the asset's name onto its
     * parent's qualifiedName.
     */
    String getQualifiedName();

    /** Atlan tags assigned to the asset. */
    SortedSet<AtlanTag> getAtlanTags();

    /**
     * Map of custom metadata attributes and values defined on the asset. The map is keyed by the human-readable
     * name of the custom metadata set, and the values are a further mapping from human-readable attribute name
     * to the value for that attribute on this asset.
     */
    Map<String, CustomMetadataAttributes> getCustomMetadataSets();

    /** Status of the asset. */
    AtlanStatus getStatus();

    /** User or account that created the asset. */
    String getCreatedBy();

    /** User or account that last updated the asset. */
    String getUpdatedBy();

    /** Time (epoch) at which the asset was created, in milliseconds. */
    Long getCreateTime();

    /** Time (epoch) at which the asset was last updated, in milliseconds. */
    Long getUpdateTime();

    /** Details on the handler used for deletion of the asset. */
    String getDeleteHandler();

    /**
     * The names of the Atlan tags that exist on the asset. This is not always returned, even by
     * full retrieval operations. It is better to depend on the detailed values in the Atlan tags
     * property.
     * @deprecated see {@link #getAtlanTags} instead
     */
    @Deprecated
    SortedSet<String> getAtlanTagNames();

    /** Unused. */
    Boolean getIsIncomplete();

    /** Names of terms that have been linked to this asset. */
    SortedSet<String> getMeaningNames();

    /**
     * Details of terms that have been linked to this asset. This is not set by all API endpoints, so cannot
     * be relied upon in general, even when there are terms assigned to an asset.
     * @deprecated see {@link Asset#getAssignedTerms} instead
     */
    @Deprecated
    SortedSet<Meaning> getMeanings();

    /** Unique identifiers (GUIDs) for any background tasks that are yet to operate on this asset. */
    SortedSet<String> getPendingTasks();
}
