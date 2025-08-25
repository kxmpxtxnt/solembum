package fyi.pauli.solembum.networking.packet.outgoing.login

import kotlinx.serialization.Serializable

/**
 * Packet, which disconnects the player during the login state.
 *
 * @param reason The reason why the player was disconnected.
 */
@Serializable
public data class Disconnect(var reason: String) : fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket() {
	override val id: Int
		get() = 0x00
	override val state: fyi.pauli.solembum.networking.packet.State
		get() = _root_ide_package_.fyi.pauli.solembum.networking.packet.State.LOGIN
	override val debugName: String
		get() = "Disconnect"
}