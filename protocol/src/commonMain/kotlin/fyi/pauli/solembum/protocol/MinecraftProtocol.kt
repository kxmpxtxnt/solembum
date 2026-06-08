package fyi.pauli.solembum.protocol

import fyi.pauli.solembum.protocol.serialization.MinecraftProtocolDecoder
import fyi.pauli.solembum.protocol.serialization.MinecraftProtocolEncoder
import fyi.pauli.solembum.protocol.serialization.types.UuidSerializer
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlin.uuid.Uuid

public class MinecraftProtocol(
	public override var serializersModule: SerializersModule = EmptySerializersModule(),
) : BinaryFormat {

	init {
		serializersModule = serializersModule + SerializersModule {
			contextual(Uuid::class, UuidSerializer)
		}
	}

	public override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
		val decoder = MinecraftProtocolDecoder(Buffer().apply { write(bytes) }, serializersModule)

		return decoder.decodeSerializableValue(deserializer)
	}

	public override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
		val buffer = Buffer()
		val encoder = MinecraftProtocolEncoder(buffer, serializersModule)
		encoder.encodeSerializableValue(serializer, value)

		return buffer.readByteArray()
	}
}