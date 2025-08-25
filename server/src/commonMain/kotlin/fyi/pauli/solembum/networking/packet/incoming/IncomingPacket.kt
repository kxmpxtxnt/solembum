package fyi.pauli.solembum.networking.packet.incoming

public data class PacketIdentifier(
	val id: Int,
	val state: fyi.pauli.solembum.networking.packet.State,
	val debuggingName: String = "unnamed packet",
)

public interface IncomingPacket