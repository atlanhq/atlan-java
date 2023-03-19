/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * Instance of an asset where we cannot determine (have not yet modeled) its detailed information.
 * In the meanwhile, this provides a catch-all case where at least the basic asset information is
 * available.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class IndistinctAsset extends Asset {
    private static final long serialVersionUID = 2L;

    /** Create a non-transient typeName to ensure it is included in serde. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Builds the minimal object necessary to update an asset that is not yet strongly typed.
     *
     * @param qualifiedName of the asset
     * @param name of the asset
     * @return the minimal request necessary to update the asset, as a builder
     */
    public static IndistinctAssetBuilder<?, ?> updater(String qualifiedName, String name) {
        return IndistinctAsset.builder().qualifiedName(qualifiedName).name(name);
    }

    /**
     * Builds the minimal object necessary to apply an update to an asset that is not yet strongly typed,
     * from a potentially more-complete asset object.
     *
     * @return the minimal object necessary to update the asset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties are not found in the initial object
     */
    @Override
    public IndistinctAssetBuilder<?, ?> trimToRequired() throws InvalidRequestException {
        List<String> missing = new ArrayList<>();
        if (this.getQualifiedName() == null || this.getQualifiedName().length() == 0) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().length() == 0) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Asset", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }
}
