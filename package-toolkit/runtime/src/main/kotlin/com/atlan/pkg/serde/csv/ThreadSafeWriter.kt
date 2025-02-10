/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg.serde.csv

import java.io.Closeable
import java.io.IOException
import java.io.Writer
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

/**
 * To allow us to safely parallel-write files across threads.
 */
class ThreadSafeWriter(
    filePath: String,
) : Writer(),
    Closeable {
    private val writer = Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8)

    /** {@inheritDoc}  */
    @Synchronized
    @Throws(IOException::class)
    override fun write(
        cbuf: CharArray,
        off: Int,
        len: Int,
    ) {
        writer.write(cbuf, off, len)
    }

    /** {@inheritDoc}  */
    @Synchronized
    @Throws(IOException::class)
    override fun flush() {
        writer.flush()
    }

    /** {@inheritDoc}  */
    @Synchronized
    @Throws(IOException::class)
    override fun close() {
        writer.close()
    }
}
