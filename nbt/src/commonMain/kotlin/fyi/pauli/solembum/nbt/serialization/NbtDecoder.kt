package fyi.pauli.solembum.nbt.serialization

import fyi.pauli.solembum.nbt.extensions.*
import fyi.pauli.solembum.nbt.serialization.types.ArrayTag
import fyi.pauli.solembum.nbt.serialization.types.ListTag
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.internal.NamedValueDecoder

@OptIn(InternalSerializationApi::class)
internal open class NbtDecoder(open val tag: fyi.pauli.solembum.nbt.extensions.AnyTag) : NamedValueDecoder() {
	protected open var currentIndex = 0

	protected open fun currentNbtTag(name: String) = tag.compound[name]
	protected open fun currentNbtTag() = currentTagOrNull?.let { currentNbtTag(it) } ?: tag

	@OptIn(ExperimentalSerializationApi::class)
	override fun decodeElementIndex(descriptor: SerialDescriptor) =
		if (currentIndex < descriptor.elementsCount) currentIndex++ else DECODE_DONE

	override fun decodeTaggedByte(tag: String) = currentNbtTag(tag).byte
	override fun decodeTaggedShort(tag: String) = currentNbtTag(tag).short
	override fun decodeTaggedInt(tag: String) = currentNbtTag(tag).int
	override fun decodeTaggedLong(tag: String) = currentNbtTag(tag).long
	override fun decodeTaggedFloat(tag: String) = currentNbtTag(tag).float
	override fun decodeTaggedDouble(tag: String) = currentNbtTag(tag).double
	override fun decodeTaggedString(tag: String) = currentNbtTag(tag).string

	@Suppress("UNCHECKED_CAST")
	@OptIn(ExperimentalSerializationApi::class)
	override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
		val currentTag = currentNbtTag()

		val decoder = when (descriptor.kind) {
			StructureKind.CLASS -> NbtDecoder(currentTag)
			StructureKind.LIST -> if (currentTag.isNbtList) ListTagDecoder(currentNbtTag().nbtList)
			else PrimitiveArrayTagDecoder(currentTag as ArrayTag<Any>)

			else -> this
		}

		return decoder
	}
}

internal class PrimitiveArrayTagDecoder(override val tag: ArrayTag<Any>) : NbtDecoder(tag) {
	override var currentIndex = -1
	private val lastIndex = tag.arraySize - 1

	override fun currentNbtTag(name: String) = tag

	override fun decodeElementIndex(descriptor: SerialDescriptor) = if (currentIndex < lastIndex) ++currentIndex
	else DECODE_DONE

	override fun decodeTaggedByte(tag: String) = currentNbtTag(tag).byteArray[currentIndex]
	override fun decodeTaggedInt(tag: String) = currentNbtTag(tag).intArray[currentIndex]
	override fun decodeTaggedLong(tag: String) = currentNbtTag(tag).longArray[currentIndex]
	override fun decodeTaggedShort(tag: String) = currentNbtTag(tag).intArray[currentIndex].toShort()
	override fun decodeTaggedBoolean(tag: String) = currentNbtTag(tag).byteArray[currentIndex] == 1.toByte()
	override fun decodeTaggedChar(tag: String) = currentNbtTag(tag).byteArray[currentIndex].toInt().toChar()
	override fun decodeTaggedFloat(tag: String) = Float.fromBits(currentNbtTag(tag).intArray[currentIndex])
	override fun decodeTaggedDouble(tag: String) = Double.fromBits(currentNbtTag(tag).longArray[currentIndex])
}

internal class ListTagDecoder(override val tag: ListTag) : NbtDecoder(tag) {
	override var currentIndex = -1
	private val lastIndex = tag.value.lastIndex

	override fun currentNbtTag(name: String): AnyTag = tag.list[currentIndex]

	override fun decodeElementIndex(descriptor: SerialDescriptor) = if (currentIndex < lastIndex) ++currentIndex
	else DECODE_DONE
}