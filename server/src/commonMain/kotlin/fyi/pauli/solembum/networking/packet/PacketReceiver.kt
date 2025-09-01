package fyi.pauli.solembum.networking.packet

import fyi.pauli.solembum.networking.packet.incoming.IncomingPacket
import fyi.pauli.solembum.server.Server

public interface PacketReceiver<P : IncomingPacket> {

	public suspend fun onReceive(
		packet: P,
		packetHandle: PacketHandle,
		server: Server,
	)
}