package fyi.pauli.solembum.extensions.bytes

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

internal actual object Compressor {
	actual fun compress(input: ByteArray): ByteArray {
		val byteStream = ByteArrayOutputStream()
		GZIPOutputStream(byteStream).use { stream: GZIPOutputStream ->
			stream.write(input)
		}
		return byteStream.toByteArray()
	}

	actual fun decompress(input: ByteArray): ByteArray {
		val byteStream = ByteArrayInputStream(input)
		GZIPInputStream(byteStream).use { stream ->
			return stream.readBytes()
		}
	}
}