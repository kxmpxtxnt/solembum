package fyi.pauli.solembum.tests

import io.ktor.utils.io.core.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertTrue

class ByteArrayExtensionTests {

	@Test
	fun compressAndDecompress() = runBlocking {
		val array = "My name is Paul".toByteArray()

		val compressed = _root_ide_package_.fyi.pauli.solembum.extensions.bytes.Compressor.compress(array)
		val decompressed = _root_ide_package_.fyi.pauli.solembum.extensions.bytes.Compressor.decompress(compressed)

		val equals = array.contentEquals(decompressed)

		assertTrue(equals)
	}
}