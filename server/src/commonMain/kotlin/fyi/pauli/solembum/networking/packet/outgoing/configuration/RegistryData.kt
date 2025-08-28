package fyi.pauli.solembum.networking.packet.outgoing.configuration

import fyi.pauli.solembum.nbt.serialization.types.CompoundTag
import fyi.pauli.solembum.networking.packet.State
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
	override val state: State
		get() = State.CONFIGURATION
	override val debugName: String
		get() = "Registry Data"
}