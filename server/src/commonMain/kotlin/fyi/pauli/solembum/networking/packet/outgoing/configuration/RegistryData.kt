package fyi.pauli.solembum.networking.packet.outgoing.configuration

import kotlinx.serialization.Serializable

/**
 * Represents certain registries that are sent from the server and are applied on the client.
 *
 * @param registryCodec CompoundTag, which contains the registries mentioned here:
 * @see "https://wiki.vg/Pre-release_protocol#Registry_Data"
 */
@Serializable
public data class RegistryData(var registryCodec: CompoundTag) :
	fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket() {
	override val id: Int
		get() = 0x05
	override val state: fyi.pauli.solembum.networking.packet.State
		get() = _root_ide_package_.fyi.pauli.solembum.networking.packet.State.CONFIGURATION
	override val debugName: String
		get() = "Registry Data"
}