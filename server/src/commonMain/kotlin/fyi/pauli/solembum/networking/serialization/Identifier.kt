package fyi.pauli.solembum.networking.serialization

import fyi.pauli.solembum.models.Identifier
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public object IdentifierStringSerializer : KSerializer<Identifier> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("identifier", PrimitiveKind.STRING)

	override fun deserialize(decoder: Decoder): Identifier {
		val (namespace, value) = decoder.decodeString().split(":")
		return Identifier(namespace, value)
	}

	override fun serialize(encoder: Encoder, value: Identifier) {
		encoder.encodeString(value.toString())
	}
}