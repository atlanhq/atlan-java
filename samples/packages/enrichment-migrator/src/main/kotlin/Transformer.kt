/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.serde.csv.CSVXformer
import mu.KLogger

class Transformer(
    private val ctx: EnrichmentMigrator.MigratorContext,
    private val inputFile: String,
    private val header: List<String>,
    private val logger: KLogger,
    private val fieldSeparator: Char,
) : CSVXformer(
    inputFile,
    header,
    logger,
    fieldSeparator,
) {
    /** {@inheritDoc} */
    override fun mapRow(inputRow: Map<String, String>): List<List<String>> {
        // Pick the fields to include in the output based on the header,
        // and replace the source connection with the target connection
        val values = header.map {
            val raw = inputRow[it] ?: ""
            raw.replace(ctx.sourceConnectionQN, ctx.targetConnectionQN)
        }.toList()
        // Wrap them all up in a list (one-to-one row output from input)
        return listOf(values)
    }

    /** {@inheritDoc} */
    override fun includeRow(inputRow: Map<String, String>): Boolean {
        // Rows will be limited by the extract, so everything extracted should be imported
        return true
    }
}
