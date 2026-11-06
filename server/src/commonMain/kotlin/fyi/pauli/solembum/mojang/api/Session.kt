package fyi.pauli.solembum.mojang.api

import fyi.pauli.solembum.networking.packet.PacketHandle
import fyi.pauli.solembum.server.entity.player.GameProfile
import io.ktor.client.call.*
import io.ktor.client.request.*

public object Session {

	public suspend fun hasJoined(handle: PacketHandle, serverId: String, ip: Boolean = false): GameProfile =
		handle.server.httpClient.get {
			url("https://sessionserver.mojang.com/session/minecraft/hasJoined")

			parameter("username", handle.packetContext.username)
			parameter("serverId", serverId)
			parameter("ip", if (ip) handle.connection.socket.remoteAddress.toString() else null)
		}.body()
}
