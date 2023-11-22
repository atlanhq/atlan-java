/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.atlan.Atlan
import com.atlan.AtlanClient
import com.atlan.model.assets.Connection
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanWorkflowPhase
import com.atlan.model.packages.ConnectionDelete
import com.atlan.serde.Serde
import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import mu.KotlinLogging
import org.testng.Assert.assertEquals
import org.testng.Assert.assertFalse
import org.testng.Assert.assertNotNull
import org.testng.Assert.assertTrue
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables
import java.io.File
import java.util.Random
import kotlin.math.min

/**
 * Base class that all package integration tests should extend.
 */
abstract class PackageTest(val testDirectory: String = makeUnique("dir")) {
    private val vars = EnvironmentVariables()

    init {
        System.setProperty("logDirectory", testDirectory)
        File(testDirectory).mkdirs()
    }

    /**
     * Validate (through assertions) that there are no errors recorded in the log file.
     *
     * @param filename for the log file
     * @param relativeTo (optional) path under which the log file should be present
     */
    fun validateErrorFreeLog(filename: String = "debug.log", relativeTo: String = testDirectory) {
        val file = getFile(filename, relativeTo)
        file.useLines { lines ->
            lines.forEach { line ->
                assertFalse(line.substring(0, min(line.length, 80)).contains("ERROR"), "Found an ERROR in log file.")
            }
        }
    }

    /**
     * Check whether the message appears in the log at the level indicated.
     *
     * @param level of the log entry (INFO, WARN, etc)
     * @param message body of the log entry (without timestamps, etc, of course)
     * @param filename for the log file
     * @param relativeTo (optional) path under which the log file should be present
     * @return true if the line appears in the log, and false otherwise
     */
    fun logHasMessage(level: String, message: String, filename: String = "debug.log", relativeTo: String = testDirectory): Boolean {
        val file = getFile(filename, relativeTo)
        file.useLines { lines ->
            lines.forEach { line ->
                if (line.contains(message) && line.contains(level)) {
                    // short-circuit
                    return true
                }
            }
        }
        return false
    }

    /**
     * Validate (through assertions) that these file exist and are non-empty files.
     *
     * @param files list of filenames
     * @param relativeTo (optional) path under which the files should be present
     */
    fun validateFilesExist(files: List<String>, relativeTo: String = testDirectory) {
        files.forEach {
            val file = getFile(it, relativeTo)
            assertNotNull(file, "File not found.")
            assertTrue(file.exists(), "File does not exist.")
            assertTrue(file.isFile, "Is not a file.")
            assertTrue(file.length() > 0, "File is empty.")
        }
    }

    companion object {

        init {
            // Note that this must be set here to allow us to do any
            // initial env retrieval before setting the config for a particular test
            Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"))
            Atlan.setApiToken(System.getenv("ATLAN_API_KEY"))
        }

        protected val client: AtlanClient = Atlan.getDefaultClient()
        private val logger = KotlinLogging.logger {}
        private val ALPHABET = charArrayOf(
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        )
        private val NANO_ID = NanoIdUtils.randomNanoId(Random(), ALPHABET, 5)
        private const val PREFIX = "jpkg_"

        // Necessary combination to both (de)serialize Atlan objects (like connections)
        // and use the JsonProperty annotations inherent in the configuration data classes
        private val mapper = Serde.createMapper(client).registerKotlinModule()

        /**
         * Make a unique string from the provided input.
         *
         * @param input the string to make unique
         * @return the string with a unique suffix
         */
        fun makeUnique(input: String): String {
            return "$PREFIX${input}_$NANO_ID"
        }

        /**
         * Remove these files.
         *
         * @param files list of filenames to be removed
         * @param relativeTo (optional) path under which the files exist
         */
        fun removeFiles(files: List<String>, relativeTo: String = "") {
            files.forEach {
                val file = getFile(it, relativeTo)
                if (file.exists() && file.isFile) {
                    assertTrue(file.delete(), "Could not delete file.")
                }
            }
        }

        /**
         * Remove the specified directory.
         *
         * @param directory path to the directory to be removed
         */
        fun removeDirectory(directory: String) {
            val file = File(directory)
            if (file.exists() && file.isDirectory) {
                assertTrue(file.deleteRecursively(), "Could not delete directory.")
            }
        }

        /**
         * Remove the provided connection, if it exists.
         *
         * @param name of the connection
         * @param type of the connector
         */
        fun removeConnection(name: String, type: AtlanConnectorType) {
            val results = Connection.findByName(name, type)
            if (!results.isNullOrEmpty()) {
                results.forEach {
                    val deleteWorkflow = ConnectionDelete.creator(it.qualifiedName, true)
                    val response = deleteWorkflow.run(client)
                    assertNotNull(response)
                    val workflowName = response.metadata.name
                    val state = response.monitorStatus(logger)
                    assertNotNull(state)
                    assertEquals(state, AtlanWorkflowPhase.SUCCESS)
                    client.workflows.archive(workflowName)
                }
            }
        }

        private fun getFile(filename: String, relativeTo: String): File {
            return if (relativeTo.isBlank()) File(filename) else File("$relativeTo${File.separator}$filename")
        }
    }

    /**
     * Set up a custom package with the provided configuration.
     *
     * @param cfg for the custom package to run with
     */
    fun setup(cfg: CustomConfig) {
        cfg.runtime = RuntimeConfig(
            userId = null,
            agent = "test",
            agentId = null,
            agentPackageName = null,
            agentWorkflowId = null,
        )
        vars.set("ATLAN_BASE_URL", System.getenv("ATLAN_BASE_URL"))
        vars.set("ATLAN_API_KEY", System.getenv("ATLAN_API_KEY"))
        vars.set("NESTED_CONFIG", mapper.writeValueAsString(cfg))
        vars.setup()
    }

    /**
     * Teardown the custom package.
     *
     * @param keepLogs (optional) whether to retain the logs generated by the test (true) or automatically remove them (default, false)
     */
    fun teardown(keepLogs: Boolean = false) {
        if (!keepLogs) {
            removeDirectory(testDirectory)
        }
        vars.teardown()
    }
}
