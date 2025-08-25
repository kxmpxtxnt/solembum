package fyi.pauli.solembum.networking.packet.outgoing.configuration

import kotlinx.serialization.Serializable

/**
 * Packet to disconnect a player during the configuration state
 *
 * @param reason The reason, which is displayed to the player's screen
 */
@Serializable
public data class Disconnect(var reason: String) : fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket() {
	override val id: Int
		get() = 0x01
	override val state: fyi.pauli.solembum.networking.packet.State
		get() = _root_ide_package_.fyi.pauli.solembum.networking.packet.State.CONFIGURATION
	override val debugName: String
		get() = "Disconnect"
}