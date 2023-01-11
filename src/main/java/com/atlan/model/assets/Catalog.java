/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Detailed information about Catalog-related assets. Only types that inherit from Catalog can participate in lineage.
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Azure.class, name = Azure.TYPE_NAME),
    @JsonSubTypes.Type(value = API.class, name = API.TYPE_NAME),
    @JsonSubTypes.Type(value = DataQuality.class, name = DataQuality.TYPE_NAME),
    @JsonSubTypes.Type(value = Dbt.class, name = Dbt.TYPE_NAME),
    @JsonSubTypes.Type(value = Resource.class, name = Resource.TYPE_NAME),
    @JsonSubTypes.Type(value = ObjectStore.class, name = ObjectStore.TYPE_NAME),
    @JsonSubTypes.Type(value = Insight.class, name = Insight.TYPE_NAME),
    @JsonSubTypes.Type(value = SQL.class, name = SQL.TYPE_NAME),
    @JsonSubTypes.Type(value = BI.class, name = BI.TYPE_NAME),
    @JsonSubTypes.Type(value = SaaS.class, name = SaaS.TYPE_NAME),
})
public abstract class Catalog extends Asset {

    public static final String TYPE_NAME = "Catalog";

    /** Processes that use this object as input. */
    @Attribute
    @Singular
    List<AbstractProcess> inputToProcesses;

    /** Processes that produce this object as output. */
    @Attribute
    @Singular
    List<AbstractProcess> outputFromProcesses;
}
