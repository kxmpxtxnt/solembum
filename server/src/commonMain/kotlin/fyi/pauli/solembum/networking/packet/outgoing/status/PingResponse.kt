package fyi.pauli.solembum.networking.packet.outgoing.status

/**
 * The response packet for PingRequest.
 *
 * @param payload Should be the same as sent by the client.
 */
public data class PingResponse(
	@NumberType var payload: Long,
) : fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket() {

	override val id: Int
		get() = 0x01

	override val state: fyi.pauli.solembum.networking.packet.State
		get() = _root_ide_package_.fyi.pauli.solembum.networking.packet.State.STATUS

	override val debugName: String
		get() = "Ping Response"
}