/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.exception.ErrorCode;
import com.atlan.exception.InvalidRequestException;
import com.atlan.model.relations.UniqueAttributes;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Instance of an asset where we cannot determine (have not yet modeled) its detailed information.
 * In the meanwhile, this provides a catch-all case where at least the basic asset information is
 * available.
 */
@Getter
@SuperBuilder(toBuilder = true, builderMethodName = "_internal")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class IndistinctAsset extends Asset {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "Asset";

    /** Create a non-transient typeName to ensure it is included in serde. */
    @Getter(onMethod_ = {@Override})
    @Builder.Default
    String typeName = TYPE_NAME;

    /**
     * Reference to an asset that is not yet strongly typed by GUID.
     *
     * @param guid the GUID of the asset that is not yet strongly typed to reference
     * @return reference to an asset that is not yet strongly typed that can be used for defining a relationship
     */
    public static IndistinctAsset refByGuid(String guid) {
        return IndistinctAsset._internal().guid(guid).build();
    }

    /**
     * Reference to an asset that is not yet strongly typed by qualifiedName.
     *
     * @param qualifiedName the qualifiedName of the asset that is not yet strongly typed to reference
     * @return reference to an asset that is not yet strongly typed that can be used for defining a relationship
     */
    public static IndistinctAsset refByQualifiedName(String qualifiedName) {
        return IndistinctAsset._internal()
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }

    /**
     * Builds the minimal object necessary to update an asset that is not yet strongly typed.
     *
     * @param qualifiedName of the asset
     * @param name of the asset
     * @return the minimal request necessary to update the asset, as a builder
     */
    public static IndistinctAssetBuilder<?, ?> updater(String qualifiedName, String name) {
        return IndistinctAsset._internal().qualifiedName(qualifiedName).name(name);
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
        if (this.getQualifiedName() == null || this.getQualifiedName().isEmpty()) {
            missing.add("qualifiedName");
        }
        if (this.getName() == null || this.getName().isEmpty()) {
            missing.add("name");
        }
        if (!missing.isEmpty()) {
            throw new InvalidRequestException(
                    ErrorCode.MISSING_REQUIRED_UPDATE_PARAM, "Asset", String.join(",", missing));
        }
        return updater(this.getQualifiedName(), this.getName());
    }

    /**
     * Builds the minimal object necessary to apply an update to an asset that is not yet strongly typed,
     * from a potentially more-complete asset object.
     *
     * @return the minimal object necessary to update the asset, as a builder
     * @throws InvalidRequestException if any of the minimal set of required properties are not found in the initial object
     */
    @Override
    public IndistinctAsset trimToReference() throws InvalidRequestException {
        if (this.getGuid() != null && !this.getGuid().isEmpty()) {
            return refByGuid(this.getGuid());
        }
        if (this.getQualifiedName() != null && !this.getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getQualifiedName());
        }
        if (this.getUniqueAttributes() != null
                && this.getUniqueAttributes().getQualifiedName() != null
                && !this.getUniqueAttributes().getQualifiedName().isEmpty()) {
            return refByQualifiedName(this.getUniqueAttributes().getQualifiedName());
        }
        throw new InvalidRequestException(
                ErrorCode.MISSING_REQUIRED_RELATIONSHIP_PARAM, "Asset", "guid, qualifiedName");
    }
}
