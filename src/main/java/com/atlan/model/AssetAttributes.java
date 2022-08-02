/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model;

import com.atlan.model.core.Attributes;
import com.atlan.model.enums.AtlanAnnouncementType;
import com.atlan.model.enums.AtlanCertificateStatus;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class AssetAttributes extends Attributes {

  /** Name used for display purposes (in user interfaces). */
  String displayName;

  /** Description of the asset, as crawled from a source. */
  String description;

  /**
   * Description of the asset, as provided by a user. If present, this will be used for the
   * description in user interfaces. If not present, the description will be used.
   */
  String userDescription;

  /** Name of the Atlan workspace in which the asset exists. */
  String tenantId;

  /** Status of the asset's certification. */
  AtlanCertificateStatus certificateStatus;

  /**
   * Human-readable descriptive message that can optionally be submitted when the
   * `certificateStatus` is changed.
   */
  String certificateStatusMessage;

  /** Name of the user who last updated the `certificateStatus`. */
  final String certificateUpdatedBy;

  /** Time (epoch) at which the `certificateStatus` was last updated, in milliseconds. */
  final Long certificateUpdatedAt;

  /**
   * Brief title for the announcement on this asset. Required when `announcementType` is specified.
   */
  String announcementTitle;

  /** Detailed message to include in the announcement on this asset. */
  String announcementMessage;

  /** Time (epoch) at which the announcement was last updated, in milliseconds. */
  final Long announcementUpdatedAt;

  /** User who last updated the announcement. */
  final String announcementUpdatedBy;

  /** Type of announcement on the asset. */
  AtlanAnnouncementType announcementType;

  /** List of users who own the asset. */
  List<String> ownerUsers;

  /** List of groups who own the asset. */
  List<String> ownerGroups;

  /** List of users who administer the asset. (This is only used for Connection assets.) */
  List<String> adminUsers;

  /** List of groups who administer the asset. (This is only used for Connection assets.) */
  List<String> adminGroups;

  /** Unused. */
  List<String> viewerUsers;

  /** Unused. */
  List<String> viewerGroups;

  /** Name of the connector through which this asset is accessible. */
  String connectorName;

  /** Unused. */
  String connectionName;

  /** Unique name of the connection through which this asset is accessible. */
  String connectionQualifiedName;

  /** Unused. */
  Boolean isDiscoverable;

  /** Unused. */
  Boolean isEditable;

  /** Unused. */
  Object subType;

  /** Unused. */
  Number viewScore;

  /** Unused. */
  Number popularityScore;

  /** Unused. */
  List<String> sourceOwners;

  /** URL to the resource within the source application. */
  String sourceURL;

  /** Name of the crawler that last synchronized this asset. */
  String lastSyncWorkflowName;

  /** Time (epoch) at which the asset was last crawled, in milliseconds. */
  Long lastSyncRunAt;

  /** Name of the last run of the crawler that last synchronized this asset. */
  String lastSyncRun;

  /** Who created the asset. */
  final String sourceCreatedBy;

  /** Time (epoch) at which the asset was created, in milliseconds. */
  final Long sourceCreatedAt;

  /** Time (epoch) at which the asset was last updated, in milliseconds. */
  final Long sourceUpdatedAt;

  /** Who last updated the asset. */
  final String sourceUpdatedBy;

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof AssetAttributes;
  }
}
