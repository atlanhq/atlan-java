/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.cell

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object TimestampXformer {

    /**
     * Encodes (serializes) an epoch-based numeric timestamp into a human-readable date
     * and time string form.
     *
     * @param ts epoch-based numeric timestamp
     * @return a human-readable date and time
     */
    fun encode(ts: Long?): String {
        return if (ts == null) {
            ""
        } else {
            ZonedDateTime.ofInstant(Instant.ofEpochMilli(ts), ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        }
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
        ts: String,
        fieldName: String,
    ): Long {
        TODO("Not yet implemented")
    }
}
