package fyi.pauli.solembum.models.payload

import kotlinx.serialization.Serializable

@Serializable
public data class BrandPayload(val brand: String) : fyi.pauli.solembum.models.payload.Payload {
	override val identifier: fyi.pauli.solembum.models.Identifier
		get() = _root_ide_package_.fyi.pauli.solembum.models.Identifier("minecraft", "brand")
}