/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
import com.atlan.pkg.PackageTest
import com.atlan.pkg.Utils
import com.atlan.pkg.lb.Loader
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * Test import of invalid file formats in the input.
 */
class InvalidFileFormatTest : PackageTest("iff") {
    override val logger = Utils.getLogger(this.javaClass.name)

    private val lineageFile = "invalid-format-lineage.csv"

    private val files =
        listOf(
            lineageFile,
            "debug.log",
        )

    private fun prepFiles() {
        // Prepare a copy of each of the invalid format files
        files.filter { it != "debug.log" }.forEach { name ->
            val input = Paths.get("src", "test", "resources", name).toFile()
            val output = Paths.get(testDirectory, name).toFile()
            input.copyTo(output, true)
        }
    }

    override fun setup() {
        prepFiles()
    }

    @Test
    fun lineageFileFailsWithMeaningfulError() {
        val exception =
            assertFailsWith<IllegalArgumentException> {
                runCustomPackage(
                    LineageBuilderCfg(
                        lineageFile = Paths.get(testDirectory, lineageFile).toString(),
                        lineageUpsertSemantic = "upsert",
                        lineageFailOnErrors = true,
                    ),
                    Loader::main,
                )
            }
        assertEquals(
            """
            Invalid input file received. Input CSV is missing required columns: [Source Type, Source Connector, Source Connection, Source Identity, Source Name, Target Type, Target Connector, Target Connection, Target Identity, Target Name, Transformation Connector, Transformation Connection, Transformation Identity, Transformation Name]
            """.trimIndent(),
            exception.message,
        )
    }

    @Test(dependsOnMethods = ["lineageFileFailsWithMeaningfulError"])
    fun filesCreated() {
        validateFilesExist(files)
    }
}
