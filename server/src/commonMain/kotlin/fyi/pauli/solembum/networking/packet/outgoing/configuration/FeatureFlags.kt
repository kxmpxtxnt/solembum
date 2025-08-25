package fyi.pauli.solembum.networking.packet.outgoing.configuration

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Used to enable and disable features, generally experimental ones, on the client.
 *
 * @param featureFlags The identifiers of the features.
 */
@Serializable
public data class FeatureFlags(var featureFlags: MutableList<@Contextual fyi.pauli.solembum.models.Identifier>) :
	fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket() {
	override val id: Int
		get() = 0x07
	override val state: fyi.pauli.solembum.networking.packet.State
		get() = _root_ide_package_.fyi.pauli.solembum.networking.packet.State.CONFIGURATION
	override val debugName: String
		get() = "Feature Flags"
}