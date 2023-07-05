/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.lineage;

import com.atlan.Atlan;
import com.atlan.exception.AtlanException;
import com.atlan.model.core.AtlanObject;
import com.atlan.model.enums.AtlanLineageDirection;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
public class LineageRequest extends AtlanObject {

    /** Unique identifier of the asset for which to retrieve lineage. */
    String guid;

    /**
     * Number of degrees of separation (hops) across which lineage should be fetched.
     * A depth of {@code 1} will fetch the immediate upstream and/or downstream assets,
     * while {@code 2} will also fetch the immediate upstream and/or downstream assets
     * of those assets, and so on. A value of {@code 0} will fetch <em>all</em> upstream
     * and/or downstream assets.
     * (BEWARE! This could take a long time and result in a very large response payload.)
     */
    @Builder.Default
    Integer depth = 0;

    /**
     * Indicates whether to fetch upstream lineage only, downstream lineage only, or both.
     */
    @Builder.Default
    AtlanLineageDirection direction = AtlanLineageDirection.BOTH;

    /**
     * When true, will only return non-process relations in the lineage. When false, will return the
     * process and non-process relations in lineage. Note: currently processing a graph representation
     * of lineage relies on this being set to {@code true}. You will need to parse your own graph
     * from the lineage response if you decide to set this to {@code false}.
     */
    @Builder.Default
    Boolean hideProcess = true;

    /**
     * When true, will include deleted processes if {@link #hideProcess} is set to false. Otherwise,
     * will not return deleted processes.
     */
    @Builder.Default
    Boolean allowDeletedProcess = false;

    /**
     * Filters to apply on entities.
     * Note that if the values in the properties of {@code entityFilters} are not valid, then the
     * filter will not be applied and you will receive all values in the response. If your filter
     * seems to be ignored, check that it is valid (if it is invalid it WILL be ignored).
     */
    EntityFilter entityFilters;

    /**
     * List of attributes to be returned for each asset.
     */
    @Singular
    List<String> attributes;

    /** Fetch the lineage defined by this object. */
    public LineageResponse fetch() throws AtlanException {
        return Atlan.getDefaultClient().assets().lineage(this);
    }
}
