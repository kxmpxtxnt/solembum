package fyi.pauli.solembum.networking.packet.outgoing.configuration

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Packet to update tags
 *
 * @param tags The tags
 */
@Serializable
public data class UpdateTags(var tags: MutableMap<@Contextual fyi.pauli.solembum.models.Identifier, MutableMap<@Contextual fyi.pauli.solembum.models.Identifier, IntArray>>) :
	fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket() {
	override val id: Int
		get() = 0x08
	override val state: fyi.pauli.solembum.networking.packet.State
		get() = _root_ide_package_.fyi.pauli.solembum.networking.packet.State.CONFIGURATION
	override val debugName: String
		get() = "Update Tags"
}