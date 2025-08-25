package fyi.pauli.solembum.nbt

import fyi.pauli.solembum.nbt.serialization.NbtDecoder
import fyi.pauli.solembum.nbt.serialization.NbtEncoder
import fyi.pauli.solembum.nbt.serialization.types.Tag
import fyi.pauli.solembum.nbt.serialization.types.TagType
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

public object Nbt : BinaryFormat {
	override val serializersModule: SerializersModule = EmptySerializersModule()

	@InternalSerializationApi
	public override fun <T> decodeFromByteArray(deserializer: DeserializationStrategy<T>, bytes: ByteArray): T {
		return decodeFromNbt(deserializer, decodeToNbt(bytes))
	}

	public fun <T> decodeFromNbt(
		deserializer: DeserializationStrategy<T>,
		value: fyi.pauli.solembum.nbt.extensions.AnyTag,
	): T {
		val decoder = NbtDecoder(value)
		return decoder.decodeSerializableValue(deserializer)
	}

	public fun decodeToNbt(value: ByteArray): fyi.pauli.solembum.nbt.extensions.AnyTag {
		val buffer = Buffer().also { it.write(value) }
		val id = buffer.readByte().toInt()
		val type = TagType.entries.first { it.id == id }

		return Tag.read(type, buffer, null)
	}

	override fun <T> encodeToByteArray(serializer: SerializationStrategy<T>, value: T): ByteArray {
		val tag = encodeToNbt(serializer, value)
		return encodeFromNbt(tag)
	}

	public fun <T> encodeToNbt(serializer: SerializationStrategy<T>, value: T): fyi.pauli.solembum.nbt.extensions.AnyTag {
		lateinit var tag: fyi.pauli.solembum.nbt.extensions.AnyTag
		val encoder = NbtEncoder { tag = it }
		encoder.encodeSerializableValue(serializer, value)
		return tag
	}

	public fun encodeFromNbt(tag: fyi.pauli.solembum.nbt.extensions.AnyTag): ByteArray {
		return if (tag is fyi.pauli.solembum.nbt.serialization.types.CompoundTag) {
			val buffer = Buffer()
			tag.writeRoot(buffer)
			buffer.readByteArray()
		} else byteArrayOf()
	}
}