package fyi.pauli.solembum.internal.compression

internal expect object Compressor {

	fun compress(input: ByteArray): ByteArray

	fun decompress(input: ByteArray): ByteArray
}