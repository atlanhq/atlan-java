/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

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
    @JsonSubTypes.Type(value = TableauWorkbook.class, name = TableauWorkbook.TYPE_NAME),
    @JsonSubTypes.Type(value = TableauDatasourceField.class, name = TableauDatasourceField.TYPE_NAME),
    @JsonSubTypes.Type(value = TableauCalculatedField.class, name = TableauCalculatedField.TYPE_NAME),
    @JsonSubTypes.Type(value = TableauProject.class, name = TableauProject.TYPE_NAME),
    @JsonSubTypes.Type(value = TableauMetric.class, name = TableauMetric.TYPE_NAME),
    @JsonSubTypes.Type(value = TableauDatasource.class, name = TableauDatasource.TYPE_NAME),
    @JsonSubTypes.Type(value = TableauSite.class, name = TableauSite.TYPE_NAME),
    @JsonSubTypes.Type(value = TableauDashboard.class, name = TableauDashboard.TYPE_NAME),
    @JsonSubTypes.Type(value = TableauFlow.class, name = TableauFlow.TYPE_NAME),
    @JsonSubTypes.Type(value = TableauWorksheet.class, name = TableauWorksheet.TYPE_NAME),
})
public abstract class Tableau extends BI {

    public static final String TYPE_NAME = "Tableau";
}
