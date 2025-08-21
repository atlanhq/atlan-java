/* SPDX-License-Identifier: Apache-2.0 */
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.client.WireMock.*
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters

abstract class WireMockService : BuildService<WireMockService.Params>, AutoCloseable {

    interface Params : BuildServiceParameters {
        val wireMockResourcesDir: DirectoryProperty
        val port: Property<Int>
    }

    private var wireMockServer: WireMockServer? = null

    fun startServer() {
        if (wireMockServer?.isRunning != true) {
            val resourcesDir = parameters.wireMockResourcesDir.get().asFile
            val port = parameters.port.get()

            println("Starting WireMock server on port $port...")

            wireMockServer = WireMockServer(
                options()
                    .port(port)
                    .withRootDirectory(resourcesDir.absolutePath)
                    .globalTemplating(true)
            )

            wireMockServer?.start()
            configureWireMock(wireMockServer!!)

            println("WireMock server started successfully on http://localhost:$port")
        }
    }

    fun stopServer() {
        wireMockServer?.let { server ->
            if (server.isRunning) {
                println("Stopping WireMock server...")
                server.stop()
                println("WireMock server stopped")
            }
        }
        wireMockServer = null
    }

    private fun configureWireMock(server: WireMockServer) {
        server.stubFor(
            get(urlEqualTo("/api/health"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{"status": "UP"}""")
                )
        )
    }

    override fun close() {
        stopServer()
    }
}
