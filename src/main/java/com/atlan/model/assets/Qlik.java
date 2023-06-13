/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for Qlik assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
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

    /** Unique identifier of the Qlik asset in Qlik. */
    @Attribute
    String qlikId;

    /** QRI of the Qlik object. */
    @Attribute
    String qlikQRI;

    /** Unique identifier (in Qlik) of the space where the asset exists. */
    @Attribute
    String qlikSpaceId;

    /** Unique name of the space where the Qlik asset exists. */
    @Attribute
    String qlikSpaceQualifiedName;

    /** Unique identifier (in Qlik) of the app where the asset exists. */
    @Attribute
    String qlikAppId;

    /** Unique name of the app where the Qlik asset exists. */
    @Attribute
    String qlikAppQualifiedName;

    /** Unique identifier (in Qlik) of the owner of the asset. */
    @Attribute
    String qlikOwnerId;

    /** Whether the asset is published in Qlik (true) or not (false). */
    @Attribute
    Boolean qlikIsPublished;
}
