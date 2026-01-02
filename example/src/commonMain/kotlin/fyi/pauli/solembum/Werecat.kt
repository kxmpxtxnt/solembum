package fyi.pauli.solembum

import fyi.pauli.solembum.config.ServerConfig
import fyi.pauli.solembum.network.PacketReceivers
import fyi.pauli.solembum.networking.packet.incoming.IncomingPacketHandler
import fyi.pauli.solembum.server.Server
import fyi.pauli.solembum.server.serve
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.io.files.Path
import kotlinx.serialization.json.Json
import org.koin.core.component.inject

public object Werecat : Server("Werecat") {

	public val serverConfig: ServerConfig by inject()
	public val exampleConfig: ExampleConfig by inject()

	public val json: Json = Json {
		encodeDefaults = true
	}

	override val httpClient: HttpClient = HttpClient(CIO) {
		install(ContentNegotiation) {
			json(json)
		}
	}

	override suspend fun startup() {
		logger.info { exampleConfig.exampleConfigValue }
		IncomingPacketHandler.registerJoinPackets()
		PacketReceivers.registerVanillaReceivers()
	}

	override suspend fun shutdown() {

	}
}

public suspend fun main(): Unit = serve(Werecat) {
	config(Path("./subpath/custom_config.toml"), ExampleConfig("Example"))
}