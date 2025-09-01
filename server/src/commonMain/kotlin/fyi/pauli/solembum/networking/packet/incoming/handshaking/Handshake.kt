package fyi.pauli.solembum.networking.packet.incoming.handshaking

import fyi.pauli.solembum.networking.packet.State
import fyi.pauli.solembum.networking.packet.incoming.IncomingPacket
import fyi.pauli.solembum.protocol.serialization.types.NumberType
import fyi.pauli.solembum.protocol.serialization.types.primitives.MinecraftNumberType
import kotlinx.serialization.Serializable

@Serializable
public data class Handshake(
	val protocolVersion: Int,
	val serverAddress: String,
	@NumberType(MinecraftNumberType.UNSIGNED) val serverPort: Short,
	val nextState: State,
) : IncomingPacket