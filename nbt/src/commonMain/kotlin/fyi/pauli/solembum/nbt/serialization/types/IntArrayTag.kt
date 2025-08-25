package fyi.pauli.solembum.nbt.serialization.types

import kotlinx.io.Buffer

public class IntArrayTag private constructor(override var name: String? = null) : ArrayTag<IntArray>() {
	internal constructor(buffer: Buffer, name: String? = null) : this(name) {
		read(buffer)
	}

	public constructor(value: IntArray, name: String? = null) : this(name) {
		this.value = value
	}

	override val arraySize: Int
		get() = value.size
	override val type: TagType = TagType.INT_ARRAY
	override val size: Int
		get() = Int.SIZE_BYTES + value.size * Int.SIZE_BYTES

	override fun read(buffer: Buffer) {
		val length = buffer.readInt()

		value = IntArray(length) { buffer.readInt() }
	}

	override fun write(buffer: Buffer) {
		buffer.writeInt(value.size)
		value.forEach { buffer.writeInt(it) }
	}

	override fun clone(name: String?): Tag<IntArray> {
		return IntArrayTag(value, name)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as IntArrayTag

		if (name != other.name) return false
		if (arraySize != other.arraySize) return false
		if (type != other.type) return false
		if (size != other.size) return false

		return true
	}

	override fun hashCode(): Int {
		var result = name?.hashCode() ?: 0
		result = 31 * result + arraySize
		result = 31 * result + type.hashCode()
		result = 31 * result + size
		return result
	}
}