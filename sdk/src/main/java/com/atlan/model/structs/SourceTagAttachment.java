/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.exception.ErrorCode;
import com.atlan.exception.NotFoundException;
import com.atlan.model.assets.Connection;
import com.atlan.model.assets.ITag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Detailed information about the attachment of a tag to an Atlan asset, synced from source.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SourceTagAttachment extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "SourceTagAttachment";

    /** Fixed typeName for SourceTagAttachment. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Simple name of the source tag. */
    String sourceTagName;

    /** Unique name of the source tag, in Atlan. */
    String sourceTagQualifiedName;

    /** Unique identifier (GUID) of the source tag, in Atlan. */
    String sourceTagGuid;

    /** Connector that is the source of the tag. */
    String sourceTagConnectorName;

    /** Value of the tag attachment, from the source. */
    @Singular
    @JsonProperty("sourceTagValue")
    List<SourceTagAttachmentValue> sourceTagValues;

    /** Whether the tag attachment has been synced at the source (true) or not (false). */
    Boolean isSourceTagSynced;

    /** Time (epoch) when the tag attachment was synced at the source, in milliseconds. */
    Long sourceTagSyncTimestamp;

    /** Error message if the tag attachment sync at the source failed. */
    String sourceTagSyncError;

    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * synced to the source.
     *
     * @param sourceTagQualifiedName Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @param sourceTagSyncTimestamp Time (epoch) when the tag attachment was synced at the source, in milliseconds.
     * @param sourceTagSyncError Error message if the tag attachment sync at the source failed.
     * @return a SourceTagAttachment with the provided information
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    public static SourceTagAttachment of(
        String sourceTagQualifiedName,
        List<SourceTagAttachmentValue> sourceTagValues,
        Long sourceTagSyncTimestamp,
        String sourceTagSyncError) throws NotFoundException {
        return of(sourceTagQualifiedName, sourceTagValues, true, sourceTagSyncTimestamp, sourceTagSyncError);
    }

    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * not synced to the source.
     *
     * @param sourceTagQualifiedName Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @return a SourceTagAttachment with the provided information
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    public static SourceTagAttachment of(
        String sourceTagQualifiedName,
        List<SourceTagAttachmentValue> sourceTagValues) throws NotFoundException {
        return of(sourceTagQualifiedName, sourceTagValues, false, null, null);
    }

    /**
     * Create a source-synced tag attachment with a particular value, when the attachment is
     * synced to the source.
     *
     * @param sourceTagQualifiedName Unique name of the source tag, in Atlan.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @param isSourceTagSynced Whether the tag attachment has been synced at the source (true) or not (false).
     * @param sourceTagSyncTimestamp Time (epoch) when the tag attachment was synced at the source, in milliseconds.
     * @param sourceTagSyncError Error message if the tag attachment sync at the source failed.
     * @return a SourceTagAttachment with the provided information
     * @throws NotFoundException if the source-synced tag cannot be resolved
     */
    private static SourceTagAttachment of(
        String sourceTagQualifiedName,
        List<SourceTagAttachmentValue> sourceTagValues,
        Boolean isSourceTagSynced,
        Long sourceTagSyncTimestamp,
        String sourceTagSyncError) throws NotFoundException {
        try {
            ITag tag = Atlan.getDefaultClient().getSourceTagCache().getSourceTagById(sourceTagQualifiedName);
            String sourceTagName = tag.getName();
            String sourceTagGuid = tag.getGuid();
            String sourceTagConnectorName = Connection.getConnectorTypeFromQualifiedName(sourceTagQualifiedName).getValue();
            return of(sourceTagName,
                sourceTagQualifiedName,
                sourceTagGuid,
                sourceTagConnectorName,
                sourceTagValues,
                isSourceTagSynced,
                sourceTagSyncTimestamp,
                sourceTagSyncError);
        } catch (AtlanException e) {
            throw new NotFoundException(ErrorCode.SOURCE_TAG_NOT_FOUND_BY_ID, e, sourceTagQualifiedName);
        }
    }

    /**
     * Quickly create a new SourceTagAttachment.
     *
     * @param sourceTagName Simple name of the source tag.
     * @param sourceTagQualifiedName Unique name of the source tag, in Atlan.
     * @param sourceTagGuid Unique identifier (GUID) of the source tag, in Atlan.
     * @param sourceTagConnectorName Connector that is the source of the tag.
     * @param sourceTagValues Value of the tag attachment, from the source.
     * @param isSourceTagSynced Whether the tag attachment has been synced at the source (true) or not (false).
     * @param sourceTagSyncTimestamp Time (epoch) when the tag attachment was synced at the source, in milliseconds.
     * @param sourceTagSyncError Error message if the tag attachment sync at the source failed.
     * @return a SourceTagAttachment with the provided information
     */
    public static SourceTagAttachment of(
        String sourceTagName,
        String sourceTagQualifiedName,
        String sourceTagGuid,
        String sourceTagConnectorName,
        List<SourceTagAttachmentValue> sourceTagValues,
        Boolean isSourceTagSynced,
        Long sourceTagSyncTimestamp,
        String sourceTagSyncError) {
        return SourceTagAttachment.builder()
            .sourceTagName(sourceTagName)
            .sourceTagQualifiedName(sourceTagQualifiedName)
            .sourceTagGuid(sourceTagGuid)
            .sourceTagConnectorName(sourceTagConnectorName)
            .sourceTagValues(sourceTagValues)
            .isSourceTagSynced(isSourceTagSynced)
            .sourceTagSyncTimestamp(sourceTagSyncTimestamp)
            .sourceTagSyncError(sourceTagSyncError)
            .build();
    }
}
