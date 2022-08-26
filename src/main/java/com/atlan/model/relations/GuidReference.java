/* SPDX-License-Identifier: Apache-2.0 */
package com.atlan.model.relations;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class GuidReference extends Reference {
    private static final long serialVersionUID = 2L;

    /**
     * Quickly create a new reference to another asset, by its GUID.
     *
     * @param typeName type of the asset to reference
     * @param guid GUID of the asset to reference
     * @return a reference to another asset
     */
    public static GuidReference of(String typeName, String guid) {
        return GuidReference.builder().typeName(typeName).guid(guid).build();
    }
}
