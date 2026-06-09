package fyi.pauli.solembum.network.receivers.status

import fyi.pauli.solembum.networking.packet.PacketHandle
import fyi.pauli.solembum.networking.packet.PacketReceiver
import fyi.pauli.solembum.networking.packet.incoming.status.PingRequest
import fyi.pauli.solembum.networking.packet.outgoing.status.PongResponse
import fyi.pauli.solembum.server.Server

public object PingRequestReceiver : PacketReceiver<PingRequest> {
	override suspend fun onReceive(packet: PingRequest, packetHandle: PacketHandle, server: Server) {

		packetHandle.sendPacket(PongResponse(packet.timestamp))
		packetHandle.connection.socket.close()
	}
}