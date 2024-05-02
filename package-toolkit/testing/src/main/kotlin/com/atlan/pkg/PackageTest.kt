/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import com.atlan.Atlan
import com.atlan.AtlanClient
import com.atlan.exception.ConflictException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.search.IndexSearchRequest
import com.atlan.model.search.IndexSearchResponse
import com.atlan.model.typedefs.AtlanTagDef
import com.atlan.net.HttpClient
import com.atlan.serde.Serde
import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import mu.KotlinLogging
import org.testng.Assert.assertEquals
import org.testng.Assert.assertFalse
import org.testng.Assert.assertNotNull
import org.testng.Assert.assertTrue
import org.testng.annotations.BeforeClass
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables
import uk.org.webcompere.systemstubs.properties.SystemProperties
import uk.org.webcompere.systemstubs.security.SystemExit
import java.io.File
import java.util.Random
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.min
import kotlin.math.round

/**
 * Base class that all package integration tests should extend.
 */
abstract class PackageTest {
    private val nanoId = NanoIdUtils.randomNanoId(Random(), ALPHABET, 5)
    private val vars = EnvironmentVariables()
    private val properties = SystemProperties()
    private val sysExit = SystemExit()
    protected val testDirectory: String

    /**
     * Create the class-unique directory before doing anything else,
     * since a given test class may want to create files here even just to
     * configure how a custom package runs.
     */
    init {
        testDirectory = makeUnique("dir")
        File(testDirectory).mkdirs()
    }

    /**
     * Then, ensure that the logging is configured.
     * This MUST run before anything else, to ensure that logs are captured in the
     * unique directory created above and not overlapping across test in the same module
     */
    @BeforeClass
    fun logSetup() {
        properties.set("logDirectory", testDirectory)
        properties.setup()
        sysExit.setup()
    }

    /**
     * Make a unique string from the provided input.
     *
     * @param input the string to make unique
     * @return the string with a unique suffix
     */
    fun makeUnique(input: String): String {
        return "$PREFIX${input}_$nanoId"
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
     * Validate (through assertions) that these files exist and are non-empty files.
     *
     * @param files list of filenames
     * @param relativeTo (optional) path under which the files should be present
     */
    fun validateFilesExist(files: List<String>, relativeTo: String = testDirectory) {
        files.forEach {
            val file = validateFile(it, relativeTo)
            assertTrue(file.length() > 0, "File is empty.")
        }
    }

    /**
     * Validate (through assertions) that these files exist, but are empty.
     *
     * @param files list of filenames
     * @param relativeTo (optional) path under whic hthe files should be present
     */
    fun validateFileExistsButEmpty(files: List<String>, relativeTo: String = testDirectory) {
        files.forEach {
            val file = validateFile(it, relativeTo)
            assertEquals(0, file.length(), "File is empty.")
        }
    }

    /**
     * Since search is eventually consistent, retry it until we arrive at the number of results
     * we expect (or hit the retry limit).
     *
     * @param request search request to run
     * @param expectedSize expected number of results from the search
     * @return the response, either with the expected number of results or after exceeding the retry limit
     */
    fun retrySearchUntil(request: IndexSearchRequest, expectedSize: Long): IndexSearchResponse {
        var count = 1
        var response = request.search()
        while (response.approximateCount < expectedSize && count < Atlan.getMaxNetworkRetries()) {
            Thread.sleep(HttpClient.waitTime(count).toMillis())
            response = request.search()
            count++
        }
        return response
    }

    private fun validateFile(filename: String, relativeTo: String): File {
        val file = getFile(filename, relativeTo)
        assertNotNull(file, "File not found.")
        assertTrue(file.exists(), "File does not exist.")
        assertTrue(file.isFile, "Is not a file.")
        return file
    }

    companion object {
        private val logger = KotlinLogging.logger {}

        init {
            // Note that this must be set here to allow us to do any
            // initial env retrieval before setting the config for a particular test
            Atlan.setBaseUrl(System.getenv("ATLAN_BASE_URL"))
            Atlan.setApiToken(System.getenv("ATLAN_API_KEY"))
        }

        protected val client: AtlanClient = Atlan.getDefaultClient()
        private val ALPHABET = charArrayOf(
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        )
        private const val PREFIX = "jpkg_"
        private const val TAG_REMOVAL_RETRIES = 30

        // Necessary combination to both (de)serialize Atlan objects (like connections)
        // and use the JsonProperty annotations inherent in the configuration data classes
        private val mapper = Serde.createMapper(client).registerKotlinModule()

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
                    val assets = client.assets.select(true)
                        .where(Asset.QUALIFIED_NAME.startsWith(it.qualifiedName))
                        .whereNot(Asset.TYPE_NAME.eq(Connection.TYPE_NAME))
                        .pageSize(50)
                        .stream()
                        .map(Asset::getGuid)
                        .toList()
                    if (assets.isNotEmpty()) {
                        val deletionType = AtlanDeleteType.PURGE
                        val guidList = assets.toList()
                        val totalToDelete = guidList.size
                        logger.info { " --- Purging $totalToDelete assets from ${it.qualifiedName}... ---" }
                        if (totalToDelete < 20) {
                            client.assets.delete(guidList, deletionType).block()
                        } else {
                            val currentCount = AtomicLong(0)
                            guidList
                                .asSequence()
                                .chunked(20)
                                .toList()
                                .parallelStream()
                                .forEach { batch ->
                                    val i = currentCount.getAndAdd(20)
                                    logger.info { " ... next batch of 20 (${round((i.toDouble() / totalToDelete) * 100)}%)" }
                                    if (batch.isNotEmpty()) {
                                        client.assets.delete(batch, deletionType).block()
                                    }
                                }
                        }
                    }
                    // Purge the connection itself, now that all assets are purged
                    logger.info { " --- Purging connection: ${it.qualifiedName}... ---" }
                    client.assets.delete(it.guid, AtlanDeleteType.PURGE).block()
                }
            }
        }

        /**
         * Remove the specified tag.
         *
         * @param displayName human-readable display name of the tag to remove
         * @throws ConflictException if the tag cannot be removed because there are still references to it
         */
        @Throws(ConflictException::class)
        fun removeTag(displayName: String, retryCount: Int = 0) {
            try {
                AtlanTagDef.purge(displayName)
            } catch (e: ConflictException) {
                if (retryCount < TAG_REMOVAL_RETRIES) {
                    Thread.sleep(HttpClient.waitTime(retryCount).toMillis())
                    removeTag(displayName, retryCount + 1)
                } else {
                    throw e
                }
            }
        }

        /**
         * Remove the provided glossary, if it exists.
         *
         * @param name of the glossary
         */
        fun removeGlossary(name: String) {
            val glossary = Glossary.findByName(name)
            val terms = GlossaryTerm.select()
                .where(GlossaryTerm.ANCHOR.eq(glossary.qualifiedName))
                .stream()
                .map { it.guid }
                .toList()
            if (terms.isNotEmpty()) client.assets.delete(terms, AtlanDeleteType.HARD)
            Glossary.purge(glossary.guid)
        }

        private fun getFile(filename: String, relativeTo: String): File {
            return if (relativeTo.isBlank()) File(filename) else File("$relativeTo${File.separator}$filename")
        }

        /**
         * Remove the provided domain, if it exists
         *
         * @param name of the domain
         */
        fun removeDomain(name: String) {
            val domainGuids = DataDomain.select()
                .where(DataDomain.NAME.eq(name))
                .stream()
                .map { it.guid }
                .toList()
            if (domainGuids.isNotEmpty()) client.assets.delete(domainGuids, AtlanDeleteType.HARD)
        }

        /**
         * Remove the provided products, if it exists
         *
         * @param name of the domain
         */
        fun removeProduct(name: String) {
            val domainGuids = DataProduct.select()
                .where(DataProduct.NAME.eq(name))
                .stream()
                .map { it.guid }
                .toList()
            if (domainGuids.isNotEmpty()) client.assets.delete(domainGuids, AtlanDeleteType.HARD)
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
        properties.teardown()
        vars.teardown()
        sysExit.teardown()
    }
}
