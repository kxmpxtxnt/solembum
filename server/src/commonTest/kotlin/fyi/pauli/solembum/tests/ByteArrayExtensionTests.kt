package fyi.pauli.solembum.tests

import fyi.pauli.solembum.extensions.bytes.Compressor
import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class ByteArrayExtensionTests {

	@Test
	fun compressAndDecompress() = runBlocking {
		val array = "Solembum is actually a werecat from Eragon.".toByteArray()

		val compressed = Compressor.compress(array)
		val decompressed = Compressor.decompress(compressed)

		assertEquals(array.decodeToString(), decompressed.decodeToString())
	}
}