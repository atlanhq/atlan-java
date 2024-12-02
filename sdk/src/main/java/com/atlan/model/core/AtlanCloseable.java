/* SPDX-License-Identifier: Apache-2.0
   Copyright 2022 Atlan Pte. Ltd. */
package com.atlan.model.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface for closeable resources that can be used in try-with clauses or Kotlin
 * use clauses. These will log any problems with closing their resources, rather than
 * throwing exceptions that need to be handled (as generally any left-behind resources will
 * be cleaned by the containerization in place, anyway).
 */
public interface AtlanCloseable extends AutoCloseable {
    Logger log = LoggerFactory.getLogger(AtlanCloseable.class);

    /** {@inheritDoc} */
    @Override
    void close();

    static void close(AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            log.info("Unable to close resource -- leaving it behind.");
            log.debug("Full details: ", e);
        }
    }
}
