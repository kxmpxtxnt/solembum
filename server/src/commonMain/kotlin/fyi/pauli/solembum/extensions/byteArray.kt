package fyi.pauli.solembum.extensions

import kotlin.experimental.inv

public fun ByteArray.toSignedHexString(): String {
	val negative = (this[0].toInt() and 0x80) != 0
	val bytes = if (negative) twosComplement() else this
	val hex = bytes.joinToString("") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }
		.trimStart('0').ifEmpty { "0" }
	return if (negative) "-$hex" else hex
}

public fun ByteArray.twosComplement(): ByteArray {
	val result = copyOf()
	for (i in result.indices) result[i] = result[i].inv()
	var carry = 1
	for (i in result.indices.reversed()) {
		val v = (result[i].toInt() and 0xFF) + carry
		result[i] = v.toByte()
		carry = v ushr 8
		if (carry == 0) break
	}
	return result
}