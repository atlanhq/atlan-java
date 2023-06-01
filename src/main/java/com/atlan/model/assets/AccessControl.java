/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.SortedSet;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for Atlan access control assets.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Persona.class, name = Persona.TYPE_NAME),
    @JsonSubTypes.Type(value = Purpose.class, name = Purpose.TYPE_NAME),
})
@Slf4j
public abstract class AccessControl extends Asset {

    public static final String TYPE_NAME = "AccessControl";

    /** TBC */
    @Attribute
    Boolean isAccessControlEnabled;

    /** Unique identifiers (GUIDs) of custom metadata that should be hidden from this access control object. */
    @Attribute
    @Singular
    SortedSet<String> denyCustomMetadataGuids;

    /** Asset sidebar tabs that should be hidden from this access control object. */
    @Attribute
    @Singular
    SortedSet<String> denyAssetTabs;

    /** TBC */
    @Attribute
    String channelLink;

    /** Policies associated with this access control object. */
    @Attribute
    @Singular
    SortedSet<AuthPolicy> policies;
}
