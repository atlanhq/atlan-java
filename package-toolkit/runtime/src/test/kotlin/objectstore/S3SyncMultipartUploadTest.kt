/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package objectstore

import java.io.File
import java.security.MessageDigest
import java.util.concurrent.TimeUnit
import kotlin.io.path.createTempDirectory
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Behaviour of `S3Sync.uploadTo` (its private MultipartUploader) against a local fake S3 endpoint, through a real
 * AWS SDK S3Client. Each upload runs in a fresh child JVM ([S3UploadChild]) because the properties under test are
 * process-level: how much heap the upload needs and whether the process can exit after a failed upload.
 */
class S3SyncMultipartUploadTest {
    private val workDir = createTempDirectory("s3sync-multipart-").toFile()
    private val partDir = File(workDir, "parts").apply { mkdirs() }

    @AfterTest
    fun cleanup() {
        workDir.deleteRecursively()
    }

    /**
     * MultipartUploader.uploadPartWithRetry, on the upload pool threads: each in-flight part used to hold a
     * 100 MB array plus the copy made by RequestBody.fromBytes, so a few parts in parallel exhausted the heap.
     */
    @Test
    fun uploadsSeveralFullPartsWithSmallHeap() {
        val source = randomFile(3 * PART_SIZE + PART_SIZE / 2)
        FakeS3MultipartServer(partDir).use { server ->
            val run = runChild(server, source, heap = "256m", timeoutSeconds = 180)
            assertTrue(run.output.contains(S3UploadChild.UPLOADED), "Upload did not complete:\n${run.tail()}")
            assertEquals(listOf(1, 2, 3, 4), server.completedPartNumbers)
            assertEquals(sha256(source), server.completedSha256)
            assertTrue(run.exited, "Child JVM did not exit:\n${run.tail()}")
        }
    }

    /** Control: MultipartUploader.uploadFile splits a file with a short last part and S3 receives identical bytes. */
    @Test
    fun uploadsShortLastPartWithIdenticalBytes() {
        val source = randomFile(PART_SIZE + 1_234_567)
        FakeS3MultipartServer(partDir).use { server ->
            val run = runChild(server, source)
            assertTrue(run.output.contains(S3UploadChild.UPLOADED), "Upload did not complete:\n${run.tail()}")
            assertEquals(listOf(1, 2), server.completedPartNumbers)
            assertEquals(sha256(source), server.completedSha256)
            assertTrue(run.exited, "Child JVM did not exit:\n${run.tail()}")
        }
    }

    /**
     * MultipartUploader.uploadFile, on the main thread: when a part fails for good, the upload is aborted and the
     * exception reaches the caller, which (like Utils.updateConnectionCache) logs it and returns from main. The
     * upload pool's non-daemon threads used to stay alive on this path, so the JVM never exited.
     */
    @Test
    fun exitsAfterPartFailsOnEveryAttempt() {
        val source = randomFile(PART_SIZE + 1_234_567)
        FakeS3MultipartServer(partDir, failPart = 2).use { server ->
            val run = runChild(server, source, timeoutSeconds = 60)
            assertTrue(run.exited, "Child JVM still alive 60 s after the failed upload:\n${run.tail()}")
            assertTrue(run.output.contains(S3UploadChild.FAILED), "Upload did not fail as expected:\n${run.tail()}")
            assertTrue(server.aborts.get() >= 1, "No AbortMultipartUpload was sent.")
            assertEquals(null, server.completedPartNumbers)
        }
    }

    /**
     * MultipartUploader.uploadPartWithRetry, on an upload pool thread: a part that fails once is sent again, so its
     * request body must be readable a second time from the start of the part.
     */
    @Test
    fun retriesPartThatFailsOnce() {
        val source = randomFile(PART_SIZE + 1_234_567)
        FakeS3MultipartServer(partDir, failPart = 1, failOnce = true).use { server ->
            val run = runChild(server, source)
            assertTrue(run.output.contains(S3UploadChild.UPLOADED), "Upload did not complete:\n${run.tail()}")
            assertTrue(server.attempts.getValue(1).get() >= 2, "Part 1 was not retried.")
            assertEquals(listOf(1, 2), server.completedPartNumbers)
            assertEquals(sha256(source), server.completedSha256)
            assertTrue(run.exited, "Child JVM did not exit:\n${run.tail()}")
        }
    }

    private data class ChildRun(
        val exited: Boolean,
        val output: String,
    ) {
        fun tail() = output.lines().takeLast(60).joinToString("\n")
    }

    private fun runChild(
        server: FakeS3MultipartServer,
        source: File,
        heap: String = "768m",
        timeoutSeconds: Long = 120,
    ): ChildRun {
        val missing = File(workDir, "no-such-aws-file").path
        val log = File(workDir, "child-${System.nanoTime()}.log")
        val java = File(System.getProperty("java.home"), "bin/java").path
        val builder =
            ProcessBuilder(
                java,
                "-Xmx$heap",
                "-Daws.endpointUrlS3=${server.endpoint}",
                "-Daws.configFile=$missing",
                "-Daws.sharedCredentialsFile=$missing",
                "-Daws.disableEc2Metadata=true",
                S3UploadChild::class.java.name,
                source.path,
                "connection-cache/test.sqlite",
            ).redirectErrorStream(true)
                .redirectOutput(log)
        builder.environment()["CLASSPATH"] = System.getProperty("java.class.path")
        val process = builder.start()
        val exited = process.waitFor(timeoutSeconds, TimeUnit.SECONDS)
        if (!exited) process.destroyForcibly().waitFor()
        return ChildRun(exited, log.readText())
    }

    private fun randomFile(size: Long): File {
        val file = File(workDir, "source-$size.bin")
        val random = Random(size)
        val buffer = ByteArray(1024 * 1024)
        file.outputStream().use { out ->
            var remaining = size
            while (remaining > 0) {
                random.nextBytes(buffer)
                val n = minOf(buffer.size.toLong(), remaining).toInt()
                out.write(buffer, 0, n)
                remaining -= n
            }
        }
        return file
    }

    private fun sha256(file: File): String {
        val digest = MessageDigest.getInstance("SHA-256")
        file.inputStream().use { input ->
            val buffer = ByteArray(1024 * 1024)
            while (true) {
                val n = input.read(buffer)
                if (n < 0) break
                digest.update(buffer, 0, n)
            }
        }
        return FakeS3MultipartServer.hex(digest.digest())
    }

    companion object {
        private const val PART_SIZE = 100 * 1024 * 1024L
    }
}
