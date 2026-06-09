package fyi.pauli.solembum.network.receivers.handshake

import fyi.pauli.solembum.networking.packet.PacketHandle
import fyi.pauli.solembum.networking.packet.PacketReceiver
import fyi.pauli.solembum.networking.packet.incoming.handshaking.Handshake
import fyi.pauli.solembum.server.Server

public object HandshakeReceiver : PacketReceiver<Handshake> {
	override suspend fun onReceive(
		packet: Handshake,
		packetHandle: PacketHandle,
		server: Server,
	) {
		server.logger.debug {
			"SWITCHED state ${packetHandle.state} to ${packet.intent} (Socket: ${packetHandle.connection.socket.remoteAddress})"
		}

		packetHandle.state = packet.intent
	}
}