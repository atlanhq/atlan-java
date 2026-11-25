/* SPDX-License-Identifier: Apache-2.0
   Copyright 2026 Atlan Pte. Ltd. */
package otel

import com.atlan.pkg.Utils
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.io.path.createTempDirectory
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/** Utils' class init sets up OpenTelemetry once per JVM from the OTEL_* environment, so this runs it in a fresh child JVM. */
class OpenTelemetrySetupTest {
    private val workDir = createTempDirectory("otel-setup-").toFile()

    @AfterTest
    fun cleanup() {
        workDir.deleteRecursively()
    }

    @Test
    fun packageStartsWithOpenTelemetryEnabled() {
        val log = File(workDir, "child.log")
        val java = File(System.getProperty("java.home"), "bin/java").path
        val builder =
            ProcessBuilder(java, OpenTelemetrySetupChild::class.java.name)
                .directory(workDir)
                .redirectErrorStream(true)
                .redirectOutput(log)
        val env = builder.environment()
        env["CLASSPATH"] = System.getProperty("java.class.path")
        env["OTEL_EXPORTER_OTLP_ENDPOINT"] = "http://127.0.0.1:1"
        env["OTEL_RESOURCE_ATTRIBUTES"] = "service.name=otel-setup-test"
        env["OTEL_WF_NODE_NAME"] = "otel-setup-test"
        val process = builder.start()
        val exited = process.waitFor(120, TimeUnit.SECONDS)
        if (!exited) process.destroyForcibly().waitFor()
        val output = log.readText()
        assertTrue(exited, "Child JVM did not exit:\n$output")
        assertEquals(0, process.exitValue(), "Child JVM failed:\n$output")
        assertFalse(output.contains("NoClassDefFoundError"), "A class failed to load:\n$output")
    }
}

/** Touches Utils the way a package's main class does, which runs its class init and so the OpenTelemetry setup. */
object OpenTelemetrySetupChild {
    @JvmStatic
    fun main(args: Array<String>) {
        Utils.getLogger(OpenTelemetrySetupChild::class.java.name).info { "OpenTelemetry set up" }
    }
}
