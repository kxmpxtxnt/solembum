package fyi.pauli.solembum.networking.packet.outgoing

import kotlinx.serialization.Transient

public abstract class OutgoingPacket {
	public abstract val id: Int

	@Transient
	public abstract val state: fyi.pauli.solembum.networking.packet.State

	@Transient
	public abstract val debugName: String
}