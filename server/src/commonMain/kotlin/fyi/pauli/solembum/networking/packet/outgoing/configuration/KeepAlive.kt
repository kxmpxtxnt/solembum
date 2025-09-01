package fyi.pauli.solembum.networking.packet.outgoing.configuration

import fyi.pauli.solembum.protocol.serialization.types.NumberType
import kotlinx.serialization.Serializable

/**
 * The server will frequently send out a keep-alive, each containing a random ID.
 * The client must respond with the same payload.
 * If the client does not respond to them for over 30 seconds, the server kicks the client.
 * Vice versa, if the server does not send any keep-alives for 20 seconds, the client will disconnect and yields a "Timed out" exception.
 *
 * @param keepAliveId ID to check the response of the client
 */
@Serializable
public data class KeepAlive(@NumberType var keepAliveId: Long) :
	fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket() {
	override val id: Int
		get() = 0x03
	override val state: fyi.pauli.solembum.networking.packet.State
		get() = _root_ide_package_.fyi.pauli.solembum.networking.packet.State.CONFIGURATION
	override val debugName: String
		get() = "Keep Alive"
}