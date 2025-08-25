package fyi.pauli.solembum.networking.packet

public interface PacketReceiver<P : fyi.pauli.solembum.networking.packet.incoming.IncomingPacket> {

	public suspend fun onReceive(
		packet: P,
		packetHandle: fyi.pauli.solembum.networking.packet.PacketHandle,
		server: fyi.pauli.solembum.server.Server,
	)
}