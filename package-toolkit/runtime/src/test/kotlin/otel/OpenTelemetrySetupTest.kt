/* SPDX-License-Identifier: Apache-2.0
   Copyright 2023 Atlan Pte. Ltd. */
package otel

import com.atlan.pkg.Utils
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.io.path.createTempDirectory
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Utils.setupOpenTelemetry, run by the Utils class init in every package's `<clinit>` when the OTEL_* variables
 * are set, as they are in each Argo pod. A split OpenTelemetry core on the runtime classpath (API from one release,
 * SDK and autoconfigure from another) throws NoClassDefFoundError there and kills the package before it reads its
 * settings. The class init runs once per JVM and reads the environment, so it runs in a fresh child JVM.
 */
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
        val process = builder.start()
        val exited = process.waitFor(120, TimeUnit.SECONDS)
        if (!exited) process.destroyForcibly().waitFor()
        val output = log.readText()
        assertTrue(exited, "Child JVM did not exit:\n$output")
        assertTrue(output.contains(OpenTelemetrySetupChild.STARTED), "Package did not get past OpenTelemetry setup:\n$output")
        assertEquals(0, process.exitValue(), "Child JVM failed:\n$output")
    }
}

/** Touches Utils the way a package's main class does, which runs its class init and so the OpenTelemetry setup. */
object OpenTelemetrySetupChild {
    const val STARTED = "OPENTELEMETRY_SETUP_DONE"

    @JvmStatic
    fun main(args: Array<String>) {
        Utils.getLogger(OpenTelemetrySetupChild::class.java.name).info { STARTED }
        println(STARTED)
    }
}
