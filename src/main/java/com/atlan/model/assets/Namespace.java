/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Base class for query collections and folders.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AtlanCollection.class, name = AtlanCollection.TYPE_NAME),
    @JsonSubTypes.Type(value = Folder.class, name = Folder.TYPE_NAME),
})
public abstract class Namespace extends Asset {

    public static final String TYPE_NAME = "Namespace";

    /** TBC */
    @Attribute
    @Singular
    SortedSet<AtlanQuery> childrenQueries;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<Folder> childrenFolders;
}
