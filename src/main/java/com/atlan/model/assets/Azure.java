/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.assets;

import com.atlan.model.structs.AzureTag;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import java.util.List;
import javax.annotation.processing.Generated;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * Base class for Azure assets.
 */
@Generated(value = "com.atlan.generators.ModelGeneratorV2")
@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ADLS.class, name = ADLS.TYPE_NAME),
})
@Slf4j
public abstract class Azure extends Catalog {

    public static final String TYPE_NAME = "Azure";

    /** TBC */
    @Attribute
    String azureResourceId;

    /** TBC */
    @Attribute
    String azureLocation;

    /** TBC */
    @Attribute
    String adlsAccountSecondaryLocation;

    /** Tags that have been applied to this Azure asset. */
    @Attribute
    @Singular
    List<AzureTag> azureTags;
}
