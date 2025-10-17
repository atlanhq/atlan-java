/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import java.time.Instant
import java.time.format.DateTimeFormatter
import kotlin.math.roundToLong

object TimestampXformer {
    private val FORMATTER = DateTimeFormatter.ISO_INSTANT

    /**
     * Encodes (serializes) an epoch-based numeric timestamp into a human-readable date
     * and time string form.
     *
     * @param ts epoch-based numeric timestamp
     * @return a human-readable date and time
     */
    fun encode(ts: Long?): String =
        if (ts == null || ts == 0L) {
            ""
        } else {
            FORMATTER.format(Instant.ofEpochMilli(ts))
        }

    /**
     * Decodes (deserializes) a human-readable date and time string form into an epoch-based
     * numeric timestamp.
     *
     * @param ts human-readable date time string
     * @param fieldName name of the field where the string appears
     * @return an epoch-based numeric timestamp
     */
    fun decode(
        ts: String?,
        fieldName: String,
    ): Long? =
        if (ts.isNullOrBlank()) {
            null
        } else if (ts.toLongOrNull() != null) {
            ts.toLong()
        } else if (ts.toDoubleOrNull() != null) {
            val double = ts.toDouble()
            double.roundToLong()
        } else {
            FORMATTER.parse(ts, Instant::from).toEpochMilli()
        }

    /**
     * Decodes (deserializes) a numeric timestamp into an epoch-based numeric timestamp.
     *
     * @param ts epoch-based numeric timestamp
     * @param fieldName name of the field where the timestamp appears
     * @return the epoch-based numeric timestamp
     */
    fun decode(
        ts: Long?,
        fieldName: String,
    ): Long? = ts
}
