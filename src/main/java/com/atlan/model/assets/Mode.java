/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for Mode assets.
 */
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ModeReport.class, name = ModeReport.TYPE_NAME),
    @JsonSubTypes.Type(value = ModeQuery.class, name = ModeQuery.TYPE_NAME),
    @JsonSubTypes.Type(value = ModeChart.class, name = ModeChart.TYPE_NAME),
    @JsonSubTypes.Type(value = ModeWorkspace.class, name = ModeWorkspace.TYPE_NAME),
    @JsonSubTypes.Type(value = ModeCollection.class, name = ModeCollection.TYPE_NAME),
})
@Slf4j
public abstract class Mode extends BI {

    public static final String TYPE_NAME = "Mode";

    /** TBC */
    @Attribute
    String modeId;

    /** TBC */
    @Attribute
    String modeToken;

    /** TBC */
    @Attribute
    String modeWorkspaceName;

    /** TBC */
    @Attribute
    String modeWorkspaceUsername;

    /** TBC */
    @Attribute
    String modeWorkspaceQualifiedName;

    /** TBC */
    @Attribute
    String modeReportName;

    /** TBC */
    @Attribute
    String modeReportQualifiedName;

    /** TBC */
    @Attribute
    String modeQueryName;

    /** TBC */
    @Attribute
    String modeQueryQualifiedName;
}
