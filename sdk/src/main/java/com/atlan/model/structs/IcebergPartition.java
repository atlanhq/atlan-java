/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Structure representing a partition field in an Iceberg table.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings("serial")
public class IcebergPartition extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "IcebergPartition";

    /** Fixed typeName for IcebergPartition. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Name of the partition field. */
    String icebergFieldName;

    /** Transform function applied to the partition field (e.g., 'identity', 'bucket', 'truncate'). */
    String icebergTransform;

    /** Source identifier for the partition field. */
    Integer icebergSourceId;

    /**
     * Quickly create a new IcebergPartition.
     * @param icebergFieldName Name of the partition field.
     * @param icebergTransform Transform function applied to the partition field (e.g., 'identity', 'bucket', 'truncate').
     * @param icebergSourceId Source identifier for the partition field.
     * @return a IcebergPartition with the provided information
     */
    public static IcebergPartition of(String icebergFieldName, String icebergTransform, Integer icebergSourceId) {
        return IcebergPartition.builder()
                .icebergFieldName(icebergFieldName)
                .icebergTransform(icebergTransform)
                .icebergSourceId(icebergSourceId)
                .build();
    }

    public abstract static class IcebergPartitionBuilder<
                    C extends IcebergPartition, B extends IcebergPartitionBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
