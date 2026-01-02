package fyi.pauli.solembum.protocol.serialization

import fyi.pauli.solembum.protocol.desc.ProtocolDesc
import fyi.pauli.solembum.protocol.desc.extractEnumDescriptor
import fyi.pauli.solembum.protocol.desc.extractEnumElementDescriptor
import fyi.pauli.solembum.protocol.desc.extractProtocolDescriptor
import fyi.pauli.solembum.protocol.serialization.types.Unprefixed
import fyi.pauli.solembum.protocol.serialization.types.primitives.MinecraftEnumType
import fyi.pauli.solembum.protocol.serialization.types.primitives.MinecraftNumberType
import fyi.pauli.solembum.protocol.serialization.types.primitives.MinecraftString.write
import fyi.pauli.solembum.protocol.serialization.types.primitives.VarInt.write
import fyi.pauli.solembum.protocol.serialization.types.primitives.VarLong
import kotlinx.io.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.internal.TaggedEncoder
import kotlinx.serialization.modules.SerializersModule

@OptIn(InternalSerializationApi::class)
internal class MinecraftProtocolEncoder(
	private val output: Buffer,
	override val serializersModule: SerializersModule,
) : TaggedEncoder<ProtocolDesc>() {

	override fun SerialDescriptor.getTag(index: Int): ProtocolDesc {
		return extractProtocolDescriptor(this@getTag, index)
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
		if (!descriptor.annotations.map { it::class }
				.contains(Unprefixed::class)) write(collectionSize) { output.writeByte(it) }
		return super.beginCollection(descriptor, collectionSize)
	}

	override fun encodeTaggedInt(tag: ProtocolDesc, value: Int) {
		when (tag.type) {
			MinecraftNumberType.DEFAULT -> output.writeInt(value)
			MinecraftNumberType.VAR -> write(value) { output.writeByte(it) }
			MinecraftNumberType.UNSIGNED -> output.writeUInt(value.toUInt())
		}
	}

	override fun encodeTaggedByte(tag: ProtocolDesc, value: Byte) {
		when (tag.type) {
			MinecraftNumberType.UNSIGNED -> output.writeUByte(value.toUByte())
			else -> output.writeByte(value)
		}
	}

	override fun encodeTaggedShort(tag: ProtocolDesc, value: Short) {
		when (tag.type) {
			MinecraftNumberType.UNSIGNED -> output.writeUShort(value.toUShort())
			else -> output.writeShort(value)
		}
	}

	override fun encodeTaggedLong(tag: ProtocolDesc, value: Long) {
		when (tag.type) {
			MinecraftNumberType.DEFAULT -> output.writeLong(value)
			MinecraftNumberType.VAR -> VarLong.write(value) { output.writeByte(it) }
			MinecraftNumberType.UNSIGNED -> output.writeULong(value.toULong())
		}
	}

	override fun encodeTaggedFloat(tag: ProtocolDesc, value: Float) {
		output.writeFloat(value)
	}

	override fun encodeTaggedDouble(tag: ProtocolDesc, value: Double) {
		output.writeDouble(value)
	}

	override fun encodeTaggedBoolean(tag: ProtocolDesc, value: Boolean) {
		output.writeByte(if (value) 0x01 else 0x00)
	}

	override fun encodeTaggedString(tag: ProtocolDesc, value: String) {
		write(value, { output.writeByte(it) }) { output.write(it) }
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun encodeTaggedEnum(tag: ProtocolDesc, enumDescriptor: SerialDescriptor, ordinal: Int) {
		val enumDesc = extractEnumElementDescriptor(enumDescriptor, ordinal)

		when (extractEnumDescriptor(enumDescriptor).type) {
			MinecraftEnumType.VAR_INT -> write(enumDesc.ordinal) { output.writeByte(it) }
			MinecraftEnumType.BYTE -> output.writeByte(enumDesc.ordinal.toByte())
			MinecraftEnumType.UNSIGNED_BYTE -> output.writeUByte(enumDesc.ordinal.toUByte())
			MinecraftEnumType.INT -> output.writeInt(enumDesc.ordinal)
			MinecraftEnumType.STRING -> write(
				enumDescriptor.getElementName(ordinal),
				{ output.writeByte(it) }) { output.write(it) }
		}
	}

	@OptIn(ExperimentalSerializationApi::class)
	override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
		return when (descriptor.kind) {
			StructureKind.CLASS, StructureKind.LIST -> MinecraftProtocolEncoder(output, serializersModule)
			else -> super.beginStructure(descriptor)
		}
	}
}