/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Base class for Salesforce assets.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = SalesforceObject.class, name = SalesforceObject.TYPE_NAME),
    @JsonSubTypes.Type(value = SalesforceField.class, name = SalesforceField.TYPE_NAME),
    @JsonSubTypes.Type(value = SalesforceOrganization.class, name = SalesforceOrganization.TYPE_NAME),
    @JsonSubTypes.Type(value = SalesforceDashboard.class, name = SalesforceDashboard.TYPE_NAME),
    @JsonSubTypes.Type(value = SalesforceReport.class, name = SalesforceReport.TYPE_NAME),
})
public abstract class Salesforce extends SaaS {

    public static final String TYPE_NAME = "Salesforce";

    /** TBC */
    @Attribute
    String organizationQualifiedName;

    /** TBC */
    @Attribute
    String apiName;
}
