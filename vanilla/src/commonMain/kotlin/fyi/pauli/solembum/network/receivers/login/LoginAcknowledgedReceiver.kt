package fyi.pauli.solembum.network.receivers.login

import fyi.pauli.solembum.networking.packet.PacketHandle
import fyi.pauli.solembum.networking.packet.PacketReceiver
import fyi.pauli.solembum.networking.packet.State
import fyi.pauli.solembum.networking.packet.incoming.login.LoginAcknowledged
import fyi.pauli.solembum.server.Server

public object LoginAcknowledgedReceiver : PacketReceiver<LoginAcknowledged> {

	override suspend fun onReceive(
		packet: LoginAcknowledged,
		packetHandle: PacketHandle,
		server: Server,
	) {
		server.logger.debug {
			"SWITCHED state ${packetHandle.state} to ${State.CONFIGURATION} (Socket: ${packetHandle.connection.socket.remoteAddress})"
		}

		packetHandle.state = State.CONFIGURATION
	}
}