package fyi.pauli.solembum.network.receivers.status

import fyi.pauli.solembum.Vanilla.json
import fyi.pauli.solembum.config.ServerConfig
import fyi.pauli.solembum.server.entity.player.Player
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

		packetHandle.sendPacket(
			StatusResponse(
				json.encodeToString(
					ServerPreview(
						ServerPreview.Version("26.2", 776),
						ServerPreview.Players(
							serverConfig.server.players.maxPlayers,
							server.players.size,
							server.players.map(ServerPreview.Players.PreviewPlayer::fromPlayer)
						),
						ServerPreview.Description(serverConfig.server.description),
						serverConfig.server.base64FavIcon(),
						enforceSecureChat = true,
						previewsChat = true
					)
				)
			)
		)
	}

	@Serializable
	public class ServerPreview(
		public var version: Version,
		public var players: Players,
		public var description: Description,
		public var favicon: String,
		public var enforceSecureChat: Boolean,
		public var previewsChat: Boolean,
	) {

		@Serializable
		public class Version(
			public var name: String,
			public var protocol: Int,
		)

		@Serializable
		public class Players(
			public var max: Int,
			public var online: Int,
			public var sample: List<PreviewPlayer>,
		) {

			@Serializable
			public class PreviewPlayer(
				public var name: String,
				public var id: Uuid,
			) {

				public companion object {
					public fun fromPlayer(player: Player): PreviewPlayer =
						PreviewPlayer(player.profile.username, player.profile.uuid)
				}
			}
		}

		@Serializable
		public class Description(
			public var text: String,
		)
	}
}