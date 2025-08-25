package fyi.pauli.solembum.networking.packet.outgoing.configuration

import kotlinx.serialization.Serializable

/**
 * Mods and plugins can use this to send their data.
 * Minecraft itself uses several plugin channels.
 * These internal channels are in the `minecraft` namespace.
 *
 * @param payload The payload to send.
 */
@Serializable
public data class PluginMessage(
	var payload: fyi.pauli.solembum.models.payload.Payload,
) : fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket() {
	override val id: Int
		get() = 0x00
	override val state: fyi.pauli.solembum.networking.packet.State
		get() = _root_ide_package_.fyi.pauli.solembum.networking.packet.State.CONFIGURATION
	override val debugName: String
		get() = "Plugin Message"
}