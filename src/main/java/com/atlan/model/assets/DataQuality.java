/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;

/**
 * Data Quality Assets
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@Jacksonized
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MonteCarlo.class, name = MonteCarlo.TYPE_NAME),
    @JsonSubTypes.Type(value = Metric.class, name = Metric.TYPE_NAME),
})
@Slf4j
public class DataQuality extends Catalog {
    private static final long serialVersionUID = 2L;

    public static final String TYPE_NAME = "DataQuality";
}
