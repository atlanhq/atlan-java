/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import java.util.Map;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * Aspect metadata linked to the given assets and associated fields info.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuppressWarnings({"cast", "serial"})
public class AssetGCPDataplexAspectMetadata extends AtlanStruct {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "AssetGCPDataplexAspectMetadata";

    /** Fixed typeName for AssetGCPDataplexAspectMetadata. */
    @JsonIgnore
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /** Aspect name including the project and region references. */
    String assetGCPDataplexAspectFullName;

    /** Aspect Display name. */
    String assetGCPDataplexAspectDisplayName;

    /** Path of the Aspect Type associated with this Aspect in the project. */
    String assetGCPDataplexAspectType;

    /** Labels associated with the Aspect Type. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Map<String, String> assetGCPDataplexAspectTypeLabels;

    /** Aspect creation timestamp. */
    Long assetGCPDataplexAspectCreatedAt;

    /** Aspect Last-Updated timestamp. */
    Long assetGCPDataplexAspectUpdatedAt;

    /** Set of metadata key-value pairs associated with the aspect. */
    @Singular
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    Map<String, String> assetGCPDataplexAspectFields;

    /**
     * Quickly create a new AssetGCPDataplexAspectMetadata.
     * @param assetGCPDataplexAspectFullName Aspect name including the project and region references.
     * @param assetGCPDataplexAspectDisplayName Aspect Display name.
     * @param assetGCPDataplexAspectType Path of the Aspect Type associated with this Aspect in the project.
     * @param assetGCPDataplexAspectTypeLabels Labels associated with the Aspect Type.
     * @param assetGCPDataplexAspectCreatedAt Aspect creation timestamp.
     * @param assetGCPDataplexAspectUpdatedAt Aspect Last-Updated timestamp.
     * @param assetGCPDataplexAspectFields Set of metadata key-value pairs associated with the aspect.
     * @return a AssetGCPDataplexAspectMetadata with the provided information
     */
    public static AssetGCPDataplexAspectMetadata of(
            String assetGCPDataplexAspectFullName,
            String assetGCPDataplexAspectDisplayName,
            String assetGCPDataplexAspectType,
            Map<String, String> assetGCPDataplexAspectTypeLabels,
            Long assetGCPDataplexAspectCreatedAt,
            Long assetGCPDataplexAspectUpdatedAt,
            Map<String, String> assetGCPDataplexAspectFields) {
        return AssetGCPDataplexAspectMetadata.builder()
                .assetGCPDataplexAspectFullName(assetGCPDataplexAspectFullName)
                .assetGCPDataplexAspectDisplayName(assetGCPDataplexAspectDisplayName)
                .assetGCPDataplexAspectType(assetGCPDataplexAspectType)
                .assetGCPDataplexAspectTypeLabels(assetGCPDataplexAspectTypeLabels)
                .assetGCPDataplexAspectCreatedAt(assetGCPDataplexAspectCreatedAt)
                .assetGCPDataplexAspectUpdatedAt(assetGCPDataplexAspectUpdatedAt)
                .assetGCPDataplexAspectFields(assetGCPDataplexAspectFields)
                .build();
    }

    public abstract static class AssetGCPDataplexAspectMetadataBuilder<
                    C extends AssetGCPDataplexAspectMetadata, B extends AssetGCPDataplexAspectMetadataBuilder<C, B>>
            extends AtlanStruct.AtlanStructBuilder<C, B> {}
}
