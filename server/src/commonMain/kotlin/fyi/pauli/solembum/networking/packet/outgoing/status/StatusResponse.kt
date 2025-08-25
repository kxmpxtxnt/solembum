package fyi.pauli.solembum.networking.packet.outgoing.status

import kotlinx.serialization.Serializable

/**
 * The response packet for StatusRequest.
 *
 * @param status The status response
 */
@Serializable
public data class StatusResponse(
	var status: String,
) : fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket() {
	override val id: Int
		get() = 0x00

	override val state: fyi.pauli.solembum.networking.packet.State
		get() = _root_ide_package_.fyi.pauli.solembum.networking.packet.State.STATUS

	override val debugName: String
		get() = "Status Response"
}
