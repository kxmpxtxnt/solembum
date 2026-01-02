package fyi.pauli.solembum.protocol.serialization

import fyi.pauli.solembum.protocol.desc.ProtocolDesc
import fyi.pauli.solembum.protocol.desc.extractEnumDescriptor
import fyi.pauli.solembum.protocol.desc.extractProtocolDescriptor
import fyi.pauli.solembum.protocol.desc.findEnumIndexByTag
import fyi.pauli.solembum.protocol.exceptions.MinecraftProtocolDecodingException
import fyi.pauli.solembum.protocol.serialization.types.Unprefixed
import fyi.pauli.solembum.protocol.serialization.types.primitives.MinecraftEnumType
import fyi.pauli.solembum.protocol.serialization.types.primitives.MinecraftNumberType
import fyi.pauli.solembum.protocol.serialization.types.primitives.MinecraftString.read
import fyi.pauli.solembum.protocol.serialization.types.primitives.VarInt
import fyi.pauli.solembum.protocol.serialization.types.primitives.VarLong
import kotlinx.io.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.internal.TaggedDecoder
import kotlinx.serialization.modules.SerializersModule


@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
internal class MinecraftProtocolDecoder(private val input: Buffer, override val serializersModule: SerializersModule) :
	TaggedDecoder<ProtocolDesc>() {
	private var currentIndex = 0
	override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
		return if (descriptor.elementsCount == currentIndex) DECODE_DONE
		else currentIndex++
	}

	override fun decodeCollectionSize(descriptor: SerialDescriptor): Int {
		return if (descriptor.annotations.map { it::class }.contains(Unprefixed::class)) -1
		else VarInt.read { input.readByte() }
	}

	override fun decodeSequentially(): Boolean {
		return true
	}

	override fun decodeTaggedBoolean(
		tag: ProtocolDesc,
	): Boolean {
		return when (val i = input.readByte()) {
			0x00.toByte() -> false
			0x01.toByte() -> true
			else -> throw MinecraftProtocolDecodingException("Expected boolean value (0 or 1), found $i")
		}
	}

	override fun decodeTaggedByte(tag: ProtocolDesc): Byte {
		return when (tag.type) {
			MinecraftNumberType.UNSIGNED -> input.readUByte().toByte()
			else -> input.readByte()
		}
	}

	override fun decodeTaggedShort(tag: ProtocolDesc): Short {
		return when (tag.type) {
			MinecraftNumberType.UNSIGNED -> input.readUShort().toShort()
			else -> input.readShort()
		}
	}

	override fun decodeTaggedInt(tag: ProtocolDesc): Int {
		return when (tag.type) {
			MinecraftNumberType.DEFAULT -> input.readInt()
			MinecraftNumberType.VAR -> VarInt.read { input.readByte() }
			MinecraftNumberType.UNSIGNED -> input.readUInt().toInt()
		}
	}

	override fun decodeTaggedLong(tag: ProtocolDesc): Long {
		return when (tag.type) {
			MinecraftNumberType.DEFAULT -> input.readLong()
			MinecraftNumberType.VAR -> VarLong.read { input.readByte() }
			MinecraftNumberType.UNSIGNED -> input.readULong().toLong()
		}
	}

	override fun decodeTaggedFloat(tag: ProtocolDesc): Float {
		return input.readFloat()
	}

	override fun decodeTaggedDouble(tag: ProtocolDesc): Double {
		return input.readDouble()
	}

	@OptIn(ExperimentalStdlibApi::class)
	override fun decodeTaggedString(tag: ProtocolDesc): String {
		return read(readByte = { input.readByte() }) { length ->
			ByteArray(length) { input.readByte() }
		}
	}

	@OptIn(ExperimentalStdlibApi::class)
	override fun decodeTaggedEnum(tag: ProtocolDesc, enumDescriptor: SerialDescriptor): Int {
		val enumTag = extractEnumDescriptor(enumDescriptor)
		val ordinal = when (enumTag.type) {
			MinecraftEnumType.VAR_INT -> VarInt.read { input.readByte() }
			MinecraftEnumType.BYTE -> input.readByte().toInt()
			MinecraftEnumType.UNSIGNED_BYTE -> input.readUByte().toInt()
			MinecraftEnumType.INT -> input.readInt()
			MinecraftEnumType.STRING -> enumDescriptor.getElementIndex(read(readByte = { input.readByte() }) { length ->
				ByteArray(length) { input.readByte() }
			})
		}

		return findEnumIndexByTag(enumDescriptor, ordinal)
	}

	override fun SerialDescriptor.getTag(index: Int): ProtocolDesc {
		return extractProtocolDescriptor(this, index)
	}

	override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
		return when (descriptor.kind) {
			StructureKind.CLASS, StructureKind.LIST -> MinecraftProtocolDecoder(input, serializersModule)
			else -> MinecraftProtocolDecoder(input, serializersModule)
		}
	}
}