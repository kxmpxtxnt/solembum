package fyi.pauli.solembum.network.receivers.status

import fyi.pauli.solembum.Werecat.json
import fyi.pauli.solembum.config.ServerConfig
import fyi.pauli.solembum.entity.player.Player
import fyi.pauli.solembum.network.PacketReceivers
import fyi.pauli.solembum.networking.packet.PacketHandle
import fyi.pauli.solembum.networking.packet.PacketReceiver
import fyi.pauli.solembum.networking.packet.incoming.status.StatusRequest
import fyi.pauli.solembum.networking.packet.outgoing.status.StatusResponse
import fyi.pauli.solembum.server.Server
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.uuid.Uuid

public object StatusRequestReceiver : PacketReceiver<StatusRequest>, KoinComponent {

	private val serverConfig: ServerConfig by inject()

	override suspend fun onReceive(
		packet: StatusRequest,
		packetHandle: PacketHandle,
		server: Server,
	) {


		packetHandle.sendPacket(StatusResponse(json.encodeToString(ServerPreview())))

		server.logger.debug { "Status Receiver" }
	}

	@Serializable
	public class ServerPreview(
		public var version: Version = Version(),
		public var players: Players = Players(),
		public var description: Description = Description(),
		public var favicon: String = "data:image/png;base64,${serverConfig.server.base64FavIcon()}",
		public var enforceSecureChat: Boolean = true,
		public var previewsChat: Boolean = true,
	) {

		@Serializable
		public class Version(
			public var name: String = "1.20.2",
			public var protocol: Int = 764,
		)

		@Serializable
		public class Players(
			public var max: Int = 100,
			public var online: Int = 0,
			public var sample: List<PreviewPlayer> = listOf(),
		) {

			@Serializable
			public class PreviewPlayer(
				public var name: String = "btwonion",
				public var id: Uuid = Uuid.parse("84c7eef5-ae2c-4ebb-a006-c3ee07643d79"),
			) {

				public companion object {
					public fun fromPlayer(player: Player): PreviewPlayer =
						PreviewPlayer(player.profile.username, player.profile.uuid)
				}
			}
		}

		@Serializable
		public class Description(
			public var text: String = "",
		)
	}
}