package fyi.pauli.solembum.extensions.bytes

import io.ktor.utils.io.pool.DirectByteBufferPool
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.zip.DataFormatException
import java.util.zip.Deflater
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import java.util.zip.Inflater

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