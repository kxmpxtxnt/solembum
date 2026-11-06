package fyi.pauli.solembum.networking.packet.outgoing.login

import fyi.pauli.solembum.server.entity.player.GameProfile
import fyi.pauli.solembum.networking.packet.State
import fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket
import kotlinx.serialization.Serializable

@Serializable
public class LoginSuccess(
	public val profile: GameProfile
) : OutgoingPacket {

	override val id: Int
		get() = 0x02

	override val state: State
		get() = State.LOGIN

	override val debugName: String
		get() = "Login Success"
}