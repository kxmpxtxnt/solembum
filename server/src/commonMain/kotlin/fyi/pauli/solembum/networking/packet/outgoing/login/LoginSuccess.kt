package fyi.pauli.solembum.networking.packet.outgoing.login

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * This packet switches the state to Configuration.
 *
 * @param userProfile The userprofile of the joining player.
 */
@Serializable
public data class LoginSuccess(
	var userProfile: @Contextual fyi.pauli.solembum.entity.player.UserProfile,
) : fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket() {
	override val id: Int
		get() = 0x02
	override val state: fyi.pauli.solembum.networking.packet.State
		get() = _root_ide_package_.fyi.pauli.solembum.networking.packet.State.LOGIN
	override val debugName: String
		get() = "Login Success"
}