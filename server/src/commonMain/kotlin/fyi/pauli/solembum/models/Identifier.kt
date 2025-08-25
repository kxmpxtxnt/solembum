package fyi.pauli.solembum.models

import fyi.pauli.solembum.networking.serialization.IdentifierStringSerializer
import kotlinx.serialization.Serializable

@Serializable(with = IdentifierStringSerializer::class)
public data class Identifier(val namespace: String, val value: String) {
	override fun toString(): String {
		return "$namespace:$value"
	}
}