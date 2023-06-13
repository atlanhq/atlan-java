/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for query collections and folders.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AtlanCollection.class, name = AtlanCollection.TYPE_NAME),
    @JsonSubTypes.Type(value = Folder.class, name = Folder.TYPE_NAME),
})
@Slf4j
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
