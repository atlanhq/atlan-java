/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package parsing

import de.siegmar.fastcsv.reader.CsvReader
import de.siegmar.fastcsv.reader.CsvRecord
import java.nio.file.Paths

object RosterReader {
    fun parse(rosterInput: String): Fellowship.Roster {
        val scholars = mutableListOf<Fellowship.Scholar>()
        val inputFile = Paths.get(rosterInput)
        val builder =
            CsvReader.builder()
                .fieldSeparator(',')
                .quoteCharacter('"')
                .skipEmptyLines(true)
                .ignoreDifferentFieldCount(false)
        builder.ofCsvRecord(inputFile).use { reader ->
            reader.stream().skip(1).forEach { r: CsvRecord ->
                if (r.fieldCount >= 3 && !r.fields[0].isNullOrBlank() && !r.fields[1].isNullOrBlank() && !r.fields[2].isNullOrBlank()) {
                    scholars.add(
                        Fellowship.Scholar(
                            firstName = r.fields[0].trim(),
                            lastName = r.fields[1].trim(),
                            emailAddress = r.fields[2].trim(),
                        ),
                    )
                }
            }
        }
        return Fellowship.Roster(scholars.toSet())
    }
}
