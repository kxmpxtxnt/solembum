package fyi.pauli.solembum.protocol

import fyi.pauli.solembum.protocol.serialization.MinecraftProtocolDecoder
import fyi.pauli.solembum.protocol.serialization.MinecraftProtocolEncoder
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

public class MinecraftProtocol(
	public override var serializersModule: SerializersModule = EmptySerializersModule(),
) : BinaryFormat {

	public override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
		val decoder = MinecraftProtocolDecoder(Buffer().also { it.write(bytes) })

		return decoder.decodeSerializableValue(deserializer)
	}

	public override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
		val buffer = Buffer()
		val encoder = MinecraftProtocolEncoder(buffer)
		encoder.encodeSerializableValue(serializer, value)

		return buffer.readByteArray()
	}
}