/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.nr;

import com.atlan.model.assets.Date;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for all assets.
 */
@AllArgsConstructor
@NoArgsConstructor(force = true)
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "typeName")
@ToString(callSuper = true)
@Slf4j
@SuppressWarnings({"cast", "unchecked"})
public abstract class Ass extends Ref {
    private static final long serialVersionUID = 2L;

    @JsonIgnore
    public SortedSet<String> getAdminGroups() {
        return getAttribute("adminGroups", new TypeReference<>() {});
    }

    /** Type of announcement on this asset. */
    @JsonIgnore
    public AtlanAnnouncementType getAnnouncementType() {
        return getEnumAttribute("announcementType", AtlanAnnouncementType.class);
    }

    @JsonIgnore
    public String getName() {
        return getAttribute("name", String.class);
    }

    /** Internal tracking of fields that should be serialized with null values. */
    @JsonIgnore
    @Singular
    transient Set<String> nullFields;

    /** Retrieve the list of fields to be serialized with null values. */
    @JsonIgnore
    public Set<String> getNullFields() {
        if (nullFields == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(nullFields);
    }

    /** Status of the asset. */
    AtlanStatus status;

    /** User or account that created the asset. */
    final String createdBy;

    /** User or account that last updated the asset. */
    final String updatedBy;

    /** Time (epoch) at which the asset was created, in milliseconds. */
    @Date
    final Long createTime;

    /** Time (epoch) at which the asset was last updated, in milliseconds. */
    @Date
    final Long updateTime;

    /** Details on the handler used for deletion of the asset. */
    final String deleteHandler;

    /**
     * Depth of this asset within lineage.
     * Note: this will only available in assets retrieved via lineage, and will vary even for
     * the same asset depending on the starting point of the lineage requested.
     */
    final Integer depth;

    /** Unused. */
    Boolean isIncomplete;

    /** Names of terms that have been linked to this asset. */
    @Singular
    SortedSet<String> meaningNames;

    /** Unique identifiers (GUIDs) for any background tasks that are yet to operate on this asset. */
    @Singular
    final SortedSet<String> pendingTasks;

    public abstract static class AssBuilder<C extends Ass, B extends Ass.AssBuilder<C, B>>
        extends Ref.RefBuilder<C, B> {
        /** Remove the announcement from the asset, if any is set on the asset. */
        public B removeAnnouncement() {
            nullField("announcementType");
            nullField("announcementTitle");
            nullField("announcementMessage");
            return self();
        }

        /** Remove the system description from the asset, if any is set on the asset. */
        public B removeDescription() {
            nullField("description");
            return self();
        }

        /** Remove the user's description from the asset, if any is set on the asset. */
        public B removeUserDescription() {
            nullField("userDescription");
            return self();
        }

        /** Remove the owners from the asset, if any are set on the asset. */
        public B removeOwners() {
            nullField("ownerUsers");
            nullField("ownerGroups");
            return self();
        }

        /** Remove the certificate from the asset, if any is set on the asset. */
        public B removeCertificate() {
            nullField("certificateStatus");
            nullField("certificateStatusMessage");
            return self();
        }

        /** Remove the linked terms from the asset, if any are set on the asset. */
        public B removeAssignedTerms() {
            nullField("assignedTerms");
            return self();
        }
    }
}
