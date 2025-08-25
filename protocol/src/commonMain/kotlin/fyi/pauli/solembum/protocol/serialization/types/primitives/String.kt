package fyi.pauli.solembum.protocol.serialization.types.primitives

import fyi.pauli.solembum.protocol.exceptions.MinecraftProtocolDecodingException
import fyi.pauli.solembum.protocol.exceptions.MinecraftProtocolEncodingException
import fyi.pauli.solembum.protocol.serialization.types.primitives.VarIntSerializer.readVarInt
import fyi.pauli.solembum.protocol.serialization.types.primitives.VarIntSerializer.writeVarInt

public object MinecraftStringEncoder {

	public const val MAX_STRING_LENGTH: Int = 32767

	@ExperimentalStdlibApi
	public inline fun readString(
		maxLength: Int = MAX_STRING_LENGTH,
		readByte: () -> Byte,
		readBytes: (length: Int) -> ByteArray,
	): String {
		val length: Int = readVarInt(readByte)
		return if (length > maxLength * 4) {
			throw MinecraftProtocolDecodingException("The received encoded string buffer length is longer than maximum allowed ($length > ${maxLength * 4})")
		} else if (length < 0) {
			throw MinecraftProtocolDecodingException("The received encoded string buffer length is less than zero! Weird string!")
		} else {
			val stringBuffer = readBytes(length).decodeToString()
			if (stringBuffer.length > maxLength) {
				throw MinecraftProtocolDecodingException("The received string length is longer than maximum allowed ($length > $maxLength)")
			} else {
				stringBuffer
			}
		}
	}

	public inline fun writeString(
		string: String, writeByte: (Byte) -> Unit, writeFully: (ByteArray) -> Unit,
	) {
		val bytes = string.encodeToByteArray()

		if (bytes.size > MAX_STRING_LENGTH) {
			throw MinecraftProtocolEncodingException("String too big (was ${bytes.size} bytes encoded, max $MAX_STRING_LENGTH)")
		} else {
			writeVarInt(bytes.size, writeByte)
			writeFully(bytes)
		}
	}
}