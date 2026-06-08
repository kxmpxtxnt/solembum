package fyi.pauli.solembum.networking.packet.incoming.login

import fyi.pauli.solembum.networking.packet.incoming.IncomingPacket
import kotlinx.serialization.Serializable

@Serializable
public data class EncryptionResponse(
	val sharedSecret: ByteArray,
	val verifyToken: ByteArray
) : IncomingPacket {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as EncryptionResponse

		if (!sharedSecret.contentEquals(other.sharedSecret)) return false
		if (!verifyToken.contentEquals(other.verifyToken)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = sharedSecret.contentHashCode()
		result = 31 * result + verifyToken.contentHashCode()
		return result
	}
}