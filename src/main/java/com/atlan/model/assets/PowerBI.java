/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.enums.PowerBIEndorsementType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for Power BI assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PowerBIReport.class, name = PowerBIReport.TYPE_NAME),
    @JsonSubTypes.Type(value = PowerBIMeasure.class, name = PowerBIMeasure.TYPE_NAME),
    @JsonSubTypes.Type(value = PowerBIColumn.class, name = PowerBIColumn.TYPE_NAME),
    @JsonSubTypes.Type(value = PowerBITable.class, name = PowerBITable.TYPE_NAME),
    @JsonSubTypes.Type(value = PowerBITile.class, name = PowerBITile.TYPE_NAME),
    @JsonSubTypes.Type(value = PowerBIDatasource.class, name = PowerBIDatasource.TYPE_NAME),
    @JsonSubTypes.Type(value = PowerBIWorkspace.class, name = PowerBIWorkspace.TYPE_NAME),
    @JsonSubTypes.Type(value = PowerBIDataset.class, name = PowerBIDataset.TYPE_NAME),
    @JsonSubTypes.Type(value = PowerBIDashboard.class, name = PowerBIDashboard.TYPE_NAME),
    @JsonSubTypes.Type(value = PowerBIDataflow.class, name = PowerBIDataflow.TYPE_NAME),
    @JsonSubTypes.Type(value = PowerBIPage.class, name = PowerBIPage.TYPE_NAME),
})
@Slf4j
public abstract class PowerBI extends BI {

    public static final String TYPE_NAME = "PowerBI";

    /** TBC */
    @Attribute
    Boolean powerBIIsHidden;

    /** TBC */
    @Attribute
    String powerBITableQualifiedName;

    /** TBC */
    @Attribute
    String powerBIFormatString;

    /** TBC */
    @Attribute
    PowerBIEndorsementType powerBIEndorsement;
}
