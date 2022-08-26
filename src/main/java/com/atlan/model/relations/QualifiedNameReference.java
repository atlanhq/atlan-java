/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.relations;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class QualifiedNameReference extends Reference {
    private static final long serialVersionUID = 2L;

    /**
     * Quickly create a new reference to another asset, by its qualifiedName.
     * @param typeName type of the asset to reference
     * @param qualifiedName of the asset to reference
     * @return a reference to another asset
     */
    public static QualifiedNameReference of(String typeName, String qualifiedName) {
        return QualifiedNameReference.builder()
                .typeName(typeName)
                .uniqueAttributes(
                        UniqueAttributes.builder().qualifiedName(qualifiedName).build())
                .build();
    }
}
