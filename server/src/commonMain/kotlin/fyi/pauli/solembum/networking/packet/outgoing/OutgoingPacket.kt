package fyi.pauli.solembum.networking.packet.outgoing

import fyi.pauli.solembum.networking.packet.State
import kotlinx.serialization.Transient

public abstract class OutgoingPacket {
	public abstract val id: Int

	@Transient
	public abstract val state: State

	@Transient
	public abstract val debugName: String
}