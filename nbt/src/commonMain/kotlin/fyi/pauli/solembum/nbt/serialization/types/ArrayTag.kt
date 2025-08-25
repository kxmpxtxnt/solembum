package fyi.pauli.solembum.nbt.serialization.types

public abstract class ArrayTag<T : Any> protected constructor(override var name: String? = null) : Tag<T>() {
	internal abstract val arraySize: Int
}