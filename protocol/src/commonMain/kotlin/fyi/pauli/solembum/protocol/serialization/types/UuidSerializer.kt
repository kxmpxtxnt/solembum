package fyi.pauli.solembum.protocol.serialization.types

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlin.uuid.Uuid

public object UuidSerializer : KSerializer<Uuid> {

	override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Uuid") {
		element<Long>("msb")
		element<Long>("lsb")
	}

	override fun serialize(encoder: Encoder, value: Uuid) {
		encoder.encodeStructure(descriptor) {
			value.toLongs { msb, lsb ->
				encodeLongElement(descriptor, 0, msb)
				encodeLongElement(descriptor, 1, lsb)
			}
		}
	}

	override fun deserialize(decoder: Decoder): Uuid {
		return decoder.decodeStructure(descriptor) {
			Uuid.fromLongs(decodeLongElement(descriptor, 0), decodeLongElement(descriptor, 1))
		}
	}
}