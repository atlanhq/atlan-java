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
 * Base class for Sigma assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SigmaDatasetColumn.class, name = SigmaDatasetColumn.TYPE_NAME),
    @JsonSubTypes.Type(value = SigmaDataset.class, name = SigmaDataset.TYPE_NAME),
    @JsonSubTypes.Type(value = SigmaWorkbook.class, name = SigmaWorkbook.TYPE_NAME),
    @JsonSubTypes.Type(value = SigmaDataElementField.class, name = SigmaDataElementField.TYPE_NAME),
    @JsonSubTypes.Type(value = SigmaPage.class, name = SigmaPage.TYPE_NAME),
    @JsonSubTypes.Type(value = SigmaDataElement.class, name = SigmaDataElement.TYPE_NAME),
})
@Slf4j
public abstract class Sigma extends Asset implements ISigma, IBI, ICatalog, IAsset, IReferenceable {

    public static final String TYPE_NAME = "Sigma";

    /** TBC */
    @Attribute
    String sigmaDataElementName;

    /** TBC */
    @Attribute
    String sigmaDataElementQualifiedName;

    /** TBC */
    @Attribute
    String sigmaPageName;

    /** TBC */
    @Attribute
    String sigmaPageQualifiedName;

    /** TBC */
    @Attribute
    String sigmaWorkbookName;

    /** TBC */
    @Attribute
    String sigmaWorkbookQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
