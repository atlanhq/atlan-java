/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.packages;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public abstract class AbstractMiner {

    /**
     * Generate an epoch timestamp for now.
     *
     * @return the string value of the epoch timestamp
     */
    public static String getEpoch() {
        return "" + System.currentTimeMillis() / 1000;
    }

    /**
     * Generate a starting point for the mining.
     *
     * @param days number of days prior to today for the starting point
     * @return the string value for the epoch timestamp starting point
     */
    public static long getStartEpoch(int days) {
        return Instant.now()
                        .minus(days, ChronoUnit.DAYS)
                        .truncatedTo(ChronoUnit.DAYS)
                        .toEpochMilli()
                / 1000;
    }
}
