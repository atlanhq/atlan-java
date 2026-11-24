/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package objectstore

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.URLDecoder
import java.security.DigestOutputStream
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

/**
 * Local stand-in for the four S3 calls a multipart upload makes (create, upload part, complete, abort).
 * Each part body is stored on disk (decoding aws-chunked bodies), so the server holds no part in memory.
 * On complete, the SHA-256 of the stored parts, in the order the client listed them, is recorded.
 *
 * @param partDir directory in which to store the received parts
 * @param failPart part number for which to answer HTTP 500 (0 for none)
 * @param failOnce when true, fail only the first attempt of [failPart]; otherwise fail every attempt
 */
class FakeS3MultipartServer(
    private val partDir: File,
    private val failPart: Int = 0,
    private val failOnce: Boolean = false,
) : AutoCloseable {
    val attempts = ConcurrentHashMap<Int, AtomicInteger>()
    val aborts = AtomicInteger()

    @Volatile var completedPartNumbers: List<Int>? = null

    @Volatile var completedSha256: String? = null

    private val pool = Executors.newCachedThreadPool()
    private val server =
        HttpServer.create(InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0).apply {
            executor = pool
            createContext("/") { exchange -> exchange.use { handle(it) } }
            start()
        }

    val endpoint: String get() = "http://127.0.0.1:${server.address.port}"

    override fun close() {
        server.stop(0)
        pool.shutdownNow()
    }

    private fun handle(exchange: HttpExchange) {
        val query = parseQuery(exchange.requestURI.rawQuery)
        val method = exchange.requestMethod
        when {
            method == "POST" && "uploads" in query ->
                xml(exchange, 200, "<InitiateMultipartUploadResult><Bucket>b</Bucket><Key>k</Key><UploadId>$UPLOAD_ID</UploadId></InitiateMultipartUploadResult>")
            method == "PUT" && "partNumber" in query -> uploadPart(exchange, query.getValue("partNumber").toInt())
            method == "POST" && "uploadId" in query -> complete(exchange)
            method == "DELETE" && "uploadId" in query -> {
                aborts.incrementAndGet()
                exchange.sendResponseHeaders(204, -1)
            }
            else -> xml(exchange, 400, "<Error><Code>Unsupported</Code><Message>$method ${exchange.requestURI}</Message></Error>")
        }
    }

    private fun uploadPart(
        exchange: HttpExchange,
        partNumber: Int,
    ) {
        val attempt = attempts.computeIfAbsent(partNumber) { AtomicInteger() }.incrementAndGet()
        val target = partFile(partNumber)
        val md5 = MessageDigest.getInstance("MD5")
        DigestOutputStream(target.outputStream(), md5).use { copyBody(exchange, it) }
        val fail = partNumber == failPart && (!failOnce || attempt == 1)
        if (fail) {
            target.delete()
            xml(exchange, 500, "<Error><Code>InternalError</Code><Message>injected failure</Message></Error>")
        } else {
            exchange.responseHeaders.add("ETag", "\"${hex(md5.digest())}\"")
            exchange.sendResponseHeaders(200, -1)
        }
    }

    private fun complete(exchange: HttpExchange) {
        val body = exchange.requestBody.readBytes().toString(Charsets.UTF_8)
        val partNumbers = PART_NUMBER.findAll(body).map { it.groupValues[1].toInt() }.toList()
        val sha = MessageDigest.getInstance("SHA-256")
        partNumbers.forEach { n -> partFile(n).inputStream().use { input -> input.copyTo(DigestOutputStream(OutputStream.nullOutputStream(), sha)) } }
        completedPartNumbers = partNumbers
        completedSha256 = hex(sha.digest())
        xml(exchange, 200, "<CompleteMultipartUploadResult><Bucket>b</Bucket><Key>k</Key><ETag>\"done-${partNumbers.size}\"</ETag></CompleteMultipartUploadResult>")
    }

    private fun partFile(partNumber: Int) = File(partDir, "part-$partNumber")

    /** Copy the request body, decoding the aws-chunked framing the SDK uses when it streams a trailing checksum. */
    private fun copyBody(
        exchange: HttpExchange,
        out: OutputStream,
    ) {
        val headers = exchange.requestHeaders
        val chunked =
            headers.getFirst("x-amz-content-sha256")?.startsWith("STREAMING") == true ||
                headers.getFirst("Content-Encoding")?.contains("aws-chunked") == true
        val input = BufferedInputStream(exchange.requestBody)
        if (!chunked) {
            input.copyTo(out)
            return
        }
        while (true) {
            val size = readLine(input).substringBefore(';').trim().toLong(16)
            if (size == 0L) break
            var remaining = size
            val buffer = ByteArray(64 * 1024)
            while (remaining > 0) {
                val read = input.read(buffer, 0, minOf(buffer.size.toLong(), remaining).toInt())
                check(read > 0) { "Body ended inside a chunk." }
                out.write(buffer, 0, read)
                remaining -= read
            }
            readLine(input)
        }
        input.readAllBytes()
    }

    private fun readLine(input: InputStream): String {
        val line = StringBuilder()
        while (true) {
            val b = input.read()
            check(b >= 0) { "Body ended inside a chunk header." }
            if (b == '\n'.code) return line.toString().trimEnd('\r')
            line.append(b.toChar())
        }
    }

    private fun xml(
        exchange: HttpExchange,
        status: Int,
        body: String,
    ) {
        val bytes = body.toByteArray()
        exchange.responseHeaders.add("Content-Type", "application/xml")
        exchange.sendResponseHeaders(status, bytes.size.toLong())
        exchange.responseBody.write(bytes)
    }

    private fun parseQuery(raw: String?): Map<String, String> =
        raw
            .orEmpty()
            .split('&')
            .filter { it.isNotEmpty() }
            .associate { pair ->
                val name = URLDecoder.decode(pair.substringBefore('='), Charsets.UTF_8)
                val value = URLDecoder.decode(pair.substringAfter('=', ""), Charsets.UTF_8)
                name to value
            }

    companion object {
        const val UPLOAD_ID = "fake-upload-id"
        private val PART_NUMBER = Regex("<PartNumber>(\\d+)</PartNumber>")

        fun hex(bytes: ByteArray) = bytes.joinToString("") { "%02x".format(it) }
    }
}
