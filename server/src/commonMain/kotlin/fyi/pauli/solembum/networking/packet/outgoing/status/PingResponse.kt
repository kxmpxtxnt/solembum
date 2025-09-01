package fyi.pauli.solembum.networking.packet.outgoing.status

import fyi.pauli.solembum.networking.packet.State
import fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket
import fyi.pauli.solembum.protocol.serialization.types.NumberType

/**
 * The response packet for PingRequest.
 *
 * @param timestamp Should be the same as sent by the client.
 */
public data class PingResponse(
	@NumberType var timestamp: Long,
) : OutgoingPacket() {

	override val id: Int
		get() = 0x01

	override val state: State
		get() = State.STATUS

	override val debugName: String
		get() = "Ping Response"
}