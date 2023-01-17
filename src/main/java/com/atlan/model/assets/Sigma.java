/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * TBC
 */
@Getter
@Setter
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
public abstract class Sigma extends BI {

    public static final String TYPE_NAME = "Sigma";

    /** TBC */
    @Attribute
    String sigmaWorkbookQualifiedName;

    /** TBC */
    @Attribute
    String sigmaWorkbookName;

    /** TBC */
    @Attribute
    String sigmaPageQualifiedName;

    /** TBC */
    @Attribute
    String sigmaPageName;

    /** TBC */
    @Attribute
    String sigmaDataElementQualifiedName;

    /** TBC */
    @Attribute
    String sigmaDataElementName;
}
