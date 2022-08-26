package com.atlan.model.relations;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class QualifiedNameReferenceJ extends ReferenceJ {
    private static final long serialVersionUID = 2L;

    /**
     * Quickly create a new reference to another asset, by its qualifiedName.
     * @param typeName type of the asset to reference
     * @param qualifiedName of the asset to reference
     * @return a reference to another asset
     */
    public static QualifiedNameReferenceJ of(String typeName, String qualifiedName) {
        return QualifiedNameReferenceJ.builder()
                .typeName(typeName)
                .uniqueAttributes(
                        UniqueAttributesJ.builder().qualifiedName(qualifiedName).build())
                .build();
    }
}
