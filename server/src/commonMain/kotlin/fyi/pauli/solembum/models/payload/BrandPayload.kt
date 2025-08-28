package fyi.pauli.solembum.models.payload

import fyi.pauli.solembum.models.Identifier
import kotlinx.serialization.Serializable

@Serializable
public data class BrandPayload(val brand: String) : Payload {
	override val identifier: Identifier
		get() = Identifier("minecraft", brand)
}