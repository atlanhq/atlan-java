/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.core.Entity;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import com.atlan.model.relations.Reference;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public abstract class Asset extends Entity {

    public static final String TYPE_NAME = "Asset";

    /**
     * Unique name for this entity. This is typically a concatenation of the asset's name onto its
     * parent's qualifiedName.
     */
    @Attribute
    String qualifiedName;

    /** Human-readable name of the asset. */
    @Attribute
    String name;

    /** Name used for display purposes (in user interfaces). */
    @Attribute
    String displayName;

    /** Description of the asset, as crawled from a source. */
    @Attribute
    String description;

    /**
     * Description of the asset, as provided by a user. If present, this will be used for the
     * description in user interfaces. If not present, the description will be used.
     */
    @Attribute
    String userDescription;

    /** Name of the Atlan workspace in which the asset exists. */
    @Attribute
    String tenantId;

    /** Status of the asset's certification. */
    @Attribute
    AtlanCertificateStatus certificateStatus;

    /**
     * Human-readable descriptive message that can optionally be submitted when the
     * `certificateStatus` is changed.
     */
    @Attribute
    String certificateStatusMessage;

    /** Name of the user who last updated the `certificateStatus`. */
    @Attribute
    final String certificateUpdatedBy;

    /** Time (epoch) at which the `certificateStatus` was last updated, in milliseconds. */
    @Attribute
    final Long certificateUpdatedAt;

    /**
     * Brief title for the announcement on this asset. Required when `announcementType` is specified.
     */
    @Attribute
    String announcementTitle;

    /** Detailed message to include in the announcement on this asset. */
    @Attribute
    String announcementMessage;

    /** Time (epoch) at which the announcement was last updated, in milliseconds. */
    @Attribute
    final Long announcementUpdatedAt;

    /** User who last updated the announcement. */
    @Attribute
    final String announcementUpdatedBy;

    /** Type of announcement on the asset. */
    @Attribute
    AtlanAnnouncementType announcementType;

    /** List of users who own the asset. */
    @Singular
    @Attribute
    List<String> ownerUsers;

    /** List of groups who own the asset. */
    @Singular
    @Attribute
    List<String> ownerGroups;

    /** List of users who administer the asset. (This is only used for Connection assets.) */
    @Singular
    @Attribute
    List<String> adminUsers;

    /** List of groups who administer the asset. (This is only used for Connection assets.) */
    @Singular
    @Attribute
    List<String> adminGroups;

    /** Unused. */
    @Singular
    @Attribute
    List<String> viewerUsers;

    /** Unused. */
    @Singular
    @Attribute
    List<String> viewerGroups;

    /** Name of the connector through which this asset is accessible. */
    @Attribute
    String connectorName;

    /** Unused. */
    @Attribute
    String connectionName;

    /** Unique name of the connection through which this asset is accessible. */
    @Attribute
    String connectionQualifiedName;

    /** Unused. */
    @Attribute
    Boolean isDiscoverable;

    /** Unused. */
    @Attribute
    Boolean isEditable;

    /** Unused. */
    @Attribute
    Object subType;

    /** Unused. */
    @Attribute
    Double viewScore;

    /** Unused. */
    @Attribute
    Double popularityScore;

    /** Unused. */
    @Attribute
    String sourceOwners;

    /** URL to the resource within the source application. */
    @Attribute
    String sourceURL;

    /** Name of the crawler that last synchronized this asset. */
    @Attribute
    String lastSyncWorkflowName;

    /** Time (epoch) at which the asset was last crawled, in milliseconds. */
    @Attribute
    Long lastSyncRunAt;

    /** Name of the last run of the crawler that last synchronized this asset. */
    @Attribute
    String lastSyncRun;

    /** Who created the asset. */
    @Attribute
    final String sourceCreatedBy;

    /** Time (epoch) at which the asset was created, in milliseconds. */
    @Attribute
    final Long sourceCreatedAt;

    /** Time (epoch) at which the asset was last updated, in milliseconds. */
    @Attribute
    final Long sourceUpdatedAt;

    /** Who last updated the asset. */
    @Attribute
    final String sourceUpdatedBy;

    /** Resources that are linked to this asset. */
    @Singular
    @Attribute
    List<Reference> links;

    /** Readme that is linked to this asset. */
    @Attribute
    Reference readme;

    /** Terms that are linked to this asset. */
    @Singular
    @Attribute
    List<Reference> meanings;

    /** Remove the certificate from the asset, if any is set on the asset. */
    public void removeCertificate() {
        addNullField("certificateStatus");
        addNullField("certificateStatusMessage");
    }

    /** Remove the announcement from the asset, if any is set on the asset. */
    public void removeAnnouncement() {
        addNullField("announcementType");
        addNullField("announcementTitle");
        addNullField("announcementMessage");
    }
}
