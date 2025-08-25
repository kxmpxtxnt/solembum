package fyi.pauli.solembum.extensions.bytes

import java.util.zip.Deflater
import java.util.zip.Inflater

internal actual object Compressor {
	actual fun compress(input: ByteArray): ByteArray {
		val deflater = Deflater()
		val output = ByteArray(0)
		deflater.setInput(input)
		deflater.finish()
		deflater.deflate(output)

		return output
	}

	actual fun decompress(input: ByteArray): ByteArray {
		val inflater = Inflater()
		val output = ByteArray(0)
		inflater.setInput(input)
		inflater.inflate(output)

		return output
	}
}