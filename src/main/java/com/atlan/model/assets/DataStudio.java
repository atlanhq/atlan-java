/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.structs.GoogleLabel;
import com.atlan.model.structs.GoogleTag;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.List;
import java.util.SortedSet;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * DataStudio Assets
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = DataStudioAsset.class, name = DataStudioAsset.TYPE_NAME),
})
@Slf4j
public abstract class DataStudio extends Asset
        implements IDataStudio, IGoogle, IBI, ICloud, IAsset, IReferenceable, ICatalog {

    public static final String TYPE_NAME = "DataStudio";

    /** TBC */
    @Attribute
    @Singular
    List<GoogleLabel> googleLabels;

    /** TBC */
    @Attribute
    String googleLocation;

    /** TBC */
    @Attribute
    String googleLocationType;

    /** TBC */
    @Attribute
    String googleProjectId;

    /** TBC */
    @Attribute
    String googleProjectName;

    /** TBC */
    @Attribute
    Long googleProjectNumber;

    /** TBC */
    @Attribute
    String googleService;

    /** TBC */
    @Attribute
    @Singular
    List<GoogleTag> googleTags;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
