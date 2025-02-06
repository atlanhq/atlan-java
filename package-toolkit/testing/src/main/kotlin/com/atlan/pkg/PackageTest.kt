/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package com.atlan.pkg

import co.elastic.clients.elasticsearch._types.SortOrder
import com.atlan.AtlanClient
import com.atlan.exception.ConflictException
import com.atlan.model.assets.Asset
import com.atlan.model.assets.Connection
import com.atlan.model.assets.DataDomain
import com.atlan.model.assets.DataProduct
import com.atlan.model.assets.Glossary
import com.atlan.model.assets.GlossaryCategory
import com.atlan.model.assets.GlossaryTerm
import com.atlan.model.enums.AtlanConnectorType
import com.atlan.model.enums.AtlanDeleteType
import com.atlan.model.enums.AtlanStatus
import com.atlan.model.search.IndexSearchRequest
import com.atlan.model.search.IndexSearchResponse
import com.atlan.model.typedefs.AtlanTagDef
import com.atlan.net.HttpClient
import com.atlan.pkg.serde.WidgetSerde
import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import mu.KLogger
import org.testng.Assert.assertEquals
import org.testng.Assert.assertFalse
import org.testng.Assert.assertNotNull
import org.testng.Assert.assertTrue
import org.testng.ITestContext
import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import uk.org.webcompere.systemstubs.SystemStubs.withEnvironmentVariable
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
abstract class PackageTest(
    val tag: String,
) {
    protected abstract val logger: KLogger

    private val nanoId = NanoIdUtils.randomNanoId(Random(), ALPHABET, 5)
    protected val sysExit = SystemExit()
    protected val client = AtlanClient()
    protected val testDirectory = makeUnique("")

    /** Implement any logic necessary for setting up the test to be run. */
    abstract fun setup()

    /** Implement any logic necessary to clean up any objects created by your tests. */
    open fun teardown() {
        // By default, do nothing
    }

    /**
     * Then, ensure that the logging is configured.
     * This MUST run before anything else, to ensure that logs are captured in the
     * unique directory created above and not overlapping across test in the same module
     */
    @BeforeClass
    fun testsSetup() {
        File(testDirectory).mkdirs()
        // TODO: if we move to passing a set logger everywhere, this could be an option...
        // val testClassName = this::class.simpleName ?: "UnknownTestClass"
        // val context = LogManager.getContext(false)
        // val layout = PatternLayout.newBuilder()
        //     .withPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n")
        //     .build()
        // val appender = FileAppender.Builder()
        //     .withFileName(Paths.get(testDirectory, "debug.log").toString())
        //     .withAppend(false)
        //     .setName("???")
        //     .setLayout(layout)
        //     .setImmediateFlush(true)
        //     .build()
        // appender.start()
        // context.getLogger(testClassName)
        System.setProperty("logDirectory", testDirectory)
        sysExit.setup()
        setup()
    }

    /**
     * Make a unique string from the provided input.
     *
     * @param input the string to make unique
     * @return the string with a unique suffix
     */
    fun makeUnique(input: String): String = "${PREFIX}_${tag}_${input}_$nanoId"

    /**
     * Validate (through assertions) that there are no errors recorded in the log file.
     *
     * @param filename for the log file
     * @param relativeTo (optional) path under which the log file should be present
     */
    fun validateErrorFreeLog(
        filename: String = "debug.log",
        relativeTo: String = testDirectory,
    ) {
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
    fun logHasMessage(
        level: String,
        message: String,
        filename: String = "debug.log",
        relativeTo: String = testDirectory,
    ): Boolean {
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
     * Check whether the provided line appears in the specified file.
     *
     * @param filename for the file
     * @param line the line to check the presence of
     * @param relativeTo (optional) path under which the log file should be present
     */
    fun fileHasLine(
        filename: String,
        line: String,
        relativeTo: String = testDirectory,
    ) {
        val file = validateFile(filename, relativeTo)
        file.useLines { lines ->
            lines.forEach { candidate ->
                if (candidate == line) {
                    // short-circuit
                    return
                }
            }
        }
        assertEquals("Transformed file does not contain expected details.", line)
    }

    /**
     * Check whether the provided line appears as the start of any line in the specified file.
     *
     * @param filename for the file
     * @param line the line to check any line in the file starts with
     * @param relativeTo (optional) path under which the log file should be present
     */
    fun fileHasLineStartingWith(
        filename: String,
        line: String,
        relativeTo: String = testDirectory,
    ) {
        val file = validateFile(filename, relativeTo)
        file.useLines { lines ->
            lines.forEach { candidate ->
                if (candidate.startsWith(line)) {
                    // short-circuit
                    return
                }
            }
        }
        assertEquals("Transformed file does not contain any line starting with expected details.", line)
    }

    /**
     * Validate (through assertions) that these files exist and are non-empty files.
     *
     * @param files list of filenames
     * @param relativeTo (optional) path under which the files should be present
     */
    fun validateFilesExist(
        files: List<String>,
        relativeTo: String = testDirectory,
    ) {
        files.forEach {
            val file = validateFile(it, relativeTo)
            assertTrue(file.length() > 0, "File is empty.")
        }
    }

    /**
     * Validate (through assertions) that these files exist, but are empty.
     *
     * @param files list of filenames
     * @param relativeTo (optional) path under which the files should be present
     */
    fun validateFileExistsButEmpty(
        files: List<String>,
        relativeTo: String = testDirectory,
    ) {
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
    fun retrySearchUntil(
        request: IndexSearchRequest,
        expectedSize: Long,
        isDeleteQuery: Boolean = false,
    ): IndexSearchResponse {
        var count = 1
        var response = request.search(client)
        var remainingActive = false
        if (isDeleteQuery) {
            remainingActive = response.assets
                ?.filter { it.status != AtlanStatus.DELETED }
                ?.toList()
                ?.isNotEmpty() ?: false
        }
        while ((response.approximateCount < expectedSize || remainingActive) && count < (client.maxNetworkRetries * 2)) {
            Thread.sleep(HttpClient.waitTime(count).toMillis())
            response = request.search(client)
            if (isDeleteQuery) {
                remainingActive = response.assets
                    ?.filter { it.status != AtlanStatus.DELETED }
                    ?.toList()
                    ?.isNotEmpty() ?: false
            }
            count++
        }
        assertFalse(response.approximateCount < expectedSize, "Search retries overran - found ${response.approximateCount} results when expecting $expectedSize.")
        return response
    }

    private fun validateFile(
        filename: String,
        relativeTo: String,
    ): File {
        val file = getFile(filename, relativeTo)
        assertNotNull(file, "File not found: ${file.path}.")
        assertTrue(file.exists(), "File does not exist: ${file.path}.")
        assertTrue(file.isFile, "Is not a file: ${file.path}.")
        return file
    }

    companion object {
        private val ALPHABET =
            charArrayOf(
                '1',
                '2',
                '3',
                '4',
                '5',
                '6',
                '7',
                '8',
                '9',
                '0',
                'a',
                'b',
                'c',
                'd',
                'e',
                'f',
                'g',
                'h',
                'i',
                'j',
                'k',
                'l',
                'm',
                'n',
                'o',
                'p',
                'q',
                'r',
                's',
                't',
                'u',
                'v',
                'w',
                'x',
                'y',
                'z',
                'A',
                'B',
                'C',
                'D',
                'E',
                'F',
                'G',
                'H',
                'I',
                'J',
                'K',
                'L',
                'M',
                'N',
                'O',
                'P',
                'Q',
                'R',
                'S',
                'T',
                'U',
                'V',
                'W',
                'X',
                'Y',
                'Z',
            )
        private const val PREFIX = "jpkg"
        private const val TAG_REMOVAL_RETRIES = 30
    }

    /**
     * Remove these files.
     *
     * @param files list of filenames to be removed
     * @param relativeTo (optional) path under which the files exist
     */
    fun removeFiles(
        files: List<String>,
        relativeTo: String = "",
    ) {
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
    fun removeConnection(
        name: String,
        type: AtlanConnectorType,
    ) {
        val results = Connection.findByName(client, name, type)
        if (!results.isNullOrEmpty()) {
            val deletionType = AtlanDeleteType.PURGE
            results.forEach {
                val assets =
                    client.assets
                        .select(true)
                        .where(Asset.QUALIFIED_NAME.startsWith(it.qualifiedName))
                        .whereNot(Asset.TYPE_NAME.eq(Connection.TYPE_NAME))
                        .pageSize(50)
                        .stream()
                        .map(Asset::getGuid)
                        .toList()
                if (assets.isNotEmpty()) {
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
                                val i = currentCount.getAndAdd(batch.size.toLong())
                                logger.info { " ... next batch of 20 (${round((i.toDouble() / totalToDelete) * 100)}%)" }
                                if (batch.isNotEmpty()) {
                                    client.assets.delete(batch, deletionType).block()
                                }
                            }
                    }
                }
                // Purge the connection itself, now that all assets are purged
                logger.info { " --- Purging connection: ${it.qualifiedName}... ---" }
                try {
                    client.assets.delete(it.guid, deletionType).block()
                } catch (e: Exception) {
                    logger.error(e) { "Unable to purge connection: ${it.qualifiedName}" }
                }
            }
        }
    }

    /**
     * Remove the specified tag.
     *
     * @param displayName human-readable display name of the tag to remove
     * @param retryCount number of retries attempted so far
     */
    fun removeTag(
        displayName: String,
        retryCount: Int = 0,
    ) {
        try {
            AtlanTagDef.purge(client, displayName)
        } catch (e: ConflictException) {
            if (retryCount < TAG_REMOVAL_RETRIES) {
                Thread.sleep(HttpClient.waitTime(retryCount).toMillis())
                removeTag(displayName, retryCount + 1)
            } else {
                logger.error { "Unable to remove tag after $retryCount attempts: $displayName" }
                logger.debug(e) { "Full details:" }
            }
        }
    }

    /**
     * Remove the provided glossary, if it exists.
     *
     * @param name of the glossary
     */
    fun removeGlossary(name: String) {
        val glossary = Glossary.findByName(client, name)
        val terms =
            GlossaryTerm
                .select(client)
                .where(GlossaryTerm.ANCHOR.eq(glossary.qualifiedName))
                .stream()
                .map { it.guid }
                .toList()
        val categories =
            GlossaryCategory
                .select(client)
                .where(GlossaryCategory.ANCHOR.eq(glossary.qualifiedName))
                .stream()
                .map { it.guid }
                .toList()
        logger.info { " --- Purging glossary $name, ${categories.size} categories, and ${terms.size} terms... ---" }
        try {
            if (terms.isNotEmpty()) client.assets.delete(terms, AtlanDeleteType.HARD)
            if (categories.isNotEmpty()) client.assets.delete(categories, AtlanDeleteType.HARD)
            Glossary.purge(client, glossary.guid)
        } catch (e: Exception) {
            logger.error(e) { "Unable to purge glossary or its contents: $name" }
        }
    }

    private fun getFile(
        filename: String,
        relativeTo: String,
    ): File = if (relativeTo.isBlank()) File(filename) else File("$relativeTo${File.separator}$filename")

    /**
     * Remove the provided domain, if it exists
     *
     * @param name of the domain
     */
    fun removeDomain(name: String) {
        val domainGuids =
            DataDomain
                .select(client)
                .where(DataDomain.NAME.eq(name))
                .stream()
                .map { it.guid }
                .toList()
        try {
            if (domainGuids.isNotEmpty()) client.assets.delete(domainGuids, AtlanDeleteType.HARD)
        } catch (e: Exception) {
            logger.error(e) { "Unable to remove domain: $name" }
        }
    }

    /**
     * Remove the provided products, if it exists
     *
     * @param name of the domain
     */
    fun removeProduct(name: String) {
        val domainGuids =
            DataProduct
                .select(client)
                .where(DataProduct.NAME.eq(name))
                .stream()
                .map { it.guid }
                .toList()
        try {
            if (domainGuids.isNotEmpty()) client.assets.delete(domainGuids, AtlanDeleteType.HARD)
        } catch (e: Exception) {
            logger.error(e) { "Unable to remove product: $name" }
        }
    }

    /**
     * Remove the provided domain and all its subdomains and products, if they exist
     *
     * @param domainName of the domain
     */
    fun removeDomainAndChildren(domainName: String) {
        val domain =
            DataDomain
                .select(client)
                .where(DataDomain.NAME.eq(domainName))
                .whereNot(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.hasAnyValue())
                .stream()
                .toList()
                .firstOrNull()
        try {
            if (domain != null) {
                // find all the products under the domain
                val productGuids =
                    DataProduct
                        .select(client)
                        .where(DataProduct.PARENT_DOMAIN_QUALIFIED_NAME.startsWith(domain.qualifiedName))
                        .stream()
                        .map { it.guid }
                        .toList()
                if (productGuids.isNotEmpty()) {
                    client.assets.delete(productGuids, AtlanDeleteType.HARD)
                }

                // find all subdomains under the domain
                val subdomains =
                    DataDomain
                        .select(client)
                        .where(DataDomain.PARENT_DOMAIN_QUALIFIED_NAME.startsWith(domain.qualifiedName))
                        .sort(DataDomain.QUALIFIED_NAME.order(SortOrder.Desc))
                        .stream()
                        .map { it.guid }
                        .toList()

                subdomains.forEach { subdomain ->
                    client.assets.delete(subdomain, AtlanDeleteType.HARD)
                }
                client.assets.delete(domain.guid, AtlanDeleteType.HARD)
            }
        } catch (e: Exception) {
            logger.error(e) { "Unable to remove domain: $domainName" }
        }
    }

    /**
     * Set up and run a custom package, using the provided configuration.
     *
     * @param cfg for the custom package
     */
    fun runCustomPackage(
        cfg: CustomConfig,
        mainMethod: (Array<String>) -> Unit,
    ) {
        cfg.runtime =
            RuntimeConfig(
                userId = null,
                agent = "test",
                agentId = null,
                agentPackageName = null,
                agentWorkflowId = null,
            )
        withEnvironmentVariable("NESTED_CONFIG", WidgetSerde.mapper.writeValueAsString(cfg)).execute {
            SystemProperties("logDirectory", testDirectory).execute {
                mainMethod(arrayOf(testDirectory))
            }
        }
    }

    /**
     * Teardown the custom package.
     *
     * @param context details of the test results, where if any have failed logs will be retained
     */
    @AfterClass(alwaysRun = true)
    fun testsTeardown(context: ITestContext) {
        try {
            logger.info { "Tearing down..." }
            teardown()
            client.close()
            val keepLogs = context.failedTests.size() > 0 || context.passedTests.size() == 0
            if (!keepLogs) {
                removeDirectory(testDirectory)
            }
        } catch (e: Exception) {
            logger.error(e) { "Failed to teardown." }
        }
        sysExit.teardown()
    }
}
