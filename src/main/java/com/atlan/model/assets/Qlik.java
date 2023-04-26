/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * TBC
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = QlikSpace.class, name = QlikSpace.TYPE_NAME),
    @JsonSubTypes.Type(value = QlikApp.class, name = QlikApp.TYPE_NAME),
    @JsonSubTypes.Type(value = QlikChart.class, name = QlikChart.TYPE_NAME),
    @JsonSubTypes.Type(value = QlikDataset.class, name = QlikDataset.TYPE_NAME),
    @JsonSubTypes.Type(value = QlikSheet.class, name = QlikSheet.TYPE_NAME),
})
@Slf4j
public abstract class Qlik extends BI {

    public static final String TYPE_NAME = "Qlik";

    /** TBC */
    @Attribute
    String qlikId;

    /** TBC */
    @Attribute
    String qlikQRI;

    /** TBC */
    @Attribute
    String qlikSpaceId;

    /** TBC */
    @Attribute
    String qlikSpaceQualifiedName;

    /** TBC */
    @Attribute
    String qlikAppId;

    /** TBC */
    @Attribute
    String qlikAppQualifiedName;

    /** TBC */
    @Attribute
    String qlikOwnerId;

    /** TBC */
    @Attribute
    Boolean qlikIsPublished;
}
