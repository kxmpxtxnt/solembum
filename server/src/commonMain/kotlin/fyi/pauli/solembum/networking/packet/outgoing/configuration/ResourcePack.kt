package fyi.pauli.solembum.networking.packet.outgoing.configuration

import kotlinx.serialization.Serializable

/**
 * Packet used to prompt players a resource pack.
 *
 * @param url The URL to the resource pack.
 * @param hash A 40-character hexadecimal and lowercase SHA-1 hash of the resource pack file.
 * If it's not a 40-character hexadecimal string, the client will not use it for hash verification and likely waste bandwidth — but it will still treat it as a unique id
 * @param forced The Notchian client will be forced to use the resource pack from the server. If they decline they will be kicked from the server.
 * @param hasPromptMessage true If the next field will be sent false otherwise. When false, this is the end of the packet
 * @param promptMessage This is shown in the prompt making the client accept or decline the resource pack.
 */
@Serializable
public data class ResourcePack(
	var url: String, var hash: String, var forced: Boolean, var hasPromptMessage: Boolean, var promptMessage: String?,
) : fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket() {
	override val id: Int
		get() = 0x06
	override val state: fyi.pauli.solembum.networking.packet.State
		get() = _root_ide_package_.fyi.pauli.solembum.networking.packet.State.CONFIGURATION
	override val debugName: String
		get() = "Resource Pack"
}