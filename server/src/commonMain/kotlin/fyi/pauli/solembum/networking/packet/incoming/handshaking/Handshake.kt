package fyi.pauli.solembum.networking.packet.incoming.handshaking

import fyi.pauli.prolialize.serialization.types.NumberType
import fyi.pauli.prolialize.serialization.types.primitives.MinecraftNumberType
import kotlinx.serialization.Serializable

@Serializable
public data class Handshake(
	val protocolVersion: Int,
	val serverAddress: String,
	@NumberType(MinecraftNumberType.UNSIGNED) val serverPort: Short,
	val nextState: fyi.pauli.solembum.networking.packet.State,
) : fyi.pauli.solembum.networking.packet.incoming.IncomingPacket