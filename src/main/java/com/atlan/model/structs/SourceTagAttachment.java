/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.model.core.AtlanObject;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the attachment of a classification on an Atlan asset, synced from source.
 */
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class SourceTagAttachment extends AtlanObject {

    /**
     * Quickly create a new SourceTagAttachment.
     * @param sourceTagName Name of the tag asset in Atlan.
     * @param sourceTagQualifiedName Unique name of the tag asset in Atlan.
     * @param sourceTagGuid Unique identifier of the tag asset in Atlan.
     * @param sourceTagConnectorName Name of the connector that is the source of the tag.
     * @param sourceTagValue Value of the attached tag within the source system.
     * @param isSourceTagSynced Whether the tag has been synced with the source (true) or not (false).
     * @param sourceTagSyncTimestamp Time at which the tag was sycned with the source.
     * @param sourceTagSyncError Error message if the tag sync with the source failed.
     * @return a SourceTagAttachment with the provided information
     */
    public static SourceTagAttachment of(
            String sourceTagName,
            String sourceTagQualifiedName,
            String sourceTagGuid,
            String sourceTagConnectorName,
            List<SourceTagAttachmentValue> sourceTagValue,
            Boolean isSourceTagSynced,
            Long sourceTagSyncTimestamp,
            String sourceTagSyncError) {
        return SourceTagAttachment.builder()
                .sourceTagName(sourceTagName)
                .sourceTagQualifiedName(sourceTagQualifiedName)
                .sourceTagGuid(sourceTagGuid)
                .sourceTagConnectorName(sourceTagConnectorName)
                .sourceTagValue(sourceTagValue)
                .isSourceTagSynced(isSourceTagSynced)
                .sourceTagSyncTimestamp(sourceTagSyncTimestamp)
                .sourceTagSyncError(sourceTagSyncError)
                .build();
    }

    /** Name of the tag asset in Atlan. */
    String sourceTagName;

    /** Unique name of the tag asset in Atlan. */
    String sourceTagQualifiedName;

    /** Unique identifier of the tag asset in Atlan. */
    String sourceTagGuid;

    /** Name of the connector that is the source of the tag. */
    String sourceTagConnectorName;

    /** Value of the attached tag within the source system. */
    List<SourceTagAttachmentValue> sourceTagValue;

    /** Whether the tag has been synced with the source (true) or not (false). */
    Boolean isSourceTagSynced;

    /** Time at which the tag was sycned with the source. */
    Long sourceTagSyncTimestamp;

    /** Error message if the tag sync with the source failed. */
    String sourceTagSyncError;
}
