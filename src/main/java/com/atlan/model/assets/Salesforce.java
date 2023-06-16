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
 * Base class for Salesforce assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SalesforceObject.class, name = SalesforceObject.TYPE_NAME),
    @JsonSubTypes.Type(value = SalesforceField.class, name = SalesforceField.TYPE_NAME),
    @JsonSubTypes.Type(value = SalesforceOrganization.class, name = SalesforceOrganization.TYPE_NAME),
    @JsonSubTypes.Type(value = SalesforceDashboard.class, name = SalesforceDashboard.TYPE_NAME),
    @JsonSubTypes.Type(value = SalesforceReport.class, name = SalesforceReport.TYPE_NAME),
})
@Slf4j
public abstract class Salesforce extends Asset implements ISalesforce, ISaaS, ICatalog, IAsset, IReferenceable {

    public static final String TYPE_NAME = "Salesforce";

    /** TBC */
    @Attribute
    String apiName;

    /** TBC */
    @Attribute
    String organizationQualifiedName;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> inputToProcesses;

    /** TBC */
    @Attribute
    @Singular
    SortedSet<ILineageProcess> outputFromProcesses;
}
