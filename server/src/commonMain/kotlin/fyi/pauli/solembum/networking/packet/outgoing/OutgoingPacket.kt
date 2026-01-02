package fyi.pauli.solembum.networking.packet.outgoing

import fyi.pauli.solembum.networking.packet.State

public interface OutgoingPacket {
	public val id: Int

	public val state: State

	public val debugName: String
}