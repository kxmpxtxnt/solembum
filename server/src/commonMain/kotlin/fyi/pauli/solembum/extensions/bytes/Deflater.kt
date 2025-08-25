package fyi.pauli.solembum.extensions.bytes

internal expect object Compressor {

	fun compress(input: ByteArray): ByteArray

	fun decompress(input: ByteArray): ByteArray
}