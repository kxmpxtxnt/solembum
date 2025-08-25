package fyi.pauli.solembum.networking.packet.outgoing.configuration

import kotlinx.serialization.Serializable

/**
 * Sent by the server to notify the client that the configuration process has finished.
 * The client answers with its own Finish Configuration whenever it is ready to continue.
 *
 * In Vanilla, this packet switches the connection state to play.
 */
@Serializable
public class FinishConfiguration : fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket() {
	override val id: Int
		get() = 0x02
	override val state: fyi.pauli.solembum.networking.packet.State
		get() = _root_ide_package_.fyi.pauli.solembum.networking.packet.State.CONFIGURATION
	override val debugName: String
		get() = "Finish Configuration"
}