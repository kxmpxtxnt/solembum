package fyi.pauli.solembum.networking.packet.outgoing.login

import fyi.pauli.solembum.networking.packet.State
import fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket
import kotlinx.serialization.Serializable

@Serializable
public data class EncryptionRequest(
	val serverId: String,
	val publicKey: ByteArray,
	val verifyToken: ByteArray,
	val shouldAuthenticate: Boolean
) : OutgoingPacket {

	override val id: Int
		get() = 0x01

	override val state: State
		get() = State.LOGIN

	override val debugName: String
		get() = "Encryption Request"

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as EncryptionRequest

		if (shouldAuthenticate != other.shouldAuthenticate) return false
		if (serverId != other.serverId) return false
		if (!publicKey.contentEquals(other.publicKey)) return false
		if (!verifyToken.contentEquals(other.verifyToken)) return false
		if (id != other.id) return false
		if (state != other.state) return false
		if (debugName != other.debugName) return false

		return true
	}

	override fun hashCode(): Int {
		var result = shouldAuthenticate.hashCode()
		result = 31 * result + serverId.hashCode()
		result = 31 * result + publicKey.contentHashCode()
		result = 31 * result + verifyToken.contentHashCode()
		result = 31 * result + id
		result = 31 * result + state.hashCode()
		result = 31 * result + debugName.hashCode()
		return result
	}
}