package fyi.pauli.solembum.networking.packet

import fyi.pauli.solembum.extensions.bytes.Compressor
import fyi.pauli.solembum.networking.packet.incoming.IncomingPacketHandler
import fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket
import fyi.pauli.solembum.networking.serialization.RawPacket
import fyi.pauli.solembum.protocol.serialization.types.primitives.VarIntSerializer
import fyi.pauli.solembum.protocol.serialization.types.primitives.VarIntSerializer.varIntBytesCount
import fyi.pauli.solembum.protocol.serialization.types.primitives.VarIntSerializer.writeVarInt
import fyi.pauli.solembum.server.Server
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.serialization.encodeToByteArray

/**
 * Handler class for any established connection,
 * @property state state of the connection.
 * @property connection represented connected socket.
 * @property threshold threshold used for compression.
 * @property compression whether it is compressed depends on the threshold.
 * @property server server which creates and handles the packets.
 * @author Paul Kindler
 * @since 01/11/2023
 */
public class PacketHandle(
	public var state: State,
	public val connection: Connection,
	public var threshold: Int = -1,
	public var compression: Boolean = threshold > 0,
	internal val server: Server,
) {

	/**
	 * Function to send a specific packet to the connected socket.
	 * @param packet packet to send.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 * @see fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket
	 */
	public suspend fun sendPacket(packet: OutgoingPacket) {
		withContext(server.coroutineContext) {
			launch {
				val encoded = server.mcProtocol.encodeToByteArray(packet)
				val length = encoded.size

				if (!compression) {
					connection.output.writeFully(Buffer().also { buffer ->
						writeVarInt(length, buffer::writeByte)
						buffer.write(encoded)
					}.readByteArray())
				} else {
					val lengthLength = varIntBytesCount(length)
					val compressed = Compressor.compress(encoded)
					val compressedLength = compressed.size
					connection.output.writeFully(Buffer().also { buffer ->
						writeVarInt(compressedLength + lengthLength, buffer::writeByte)
						writeVarInt(length, buffer::writeByte)
						buffer.write(compressed)
					}.readByteArray())
					connection.output.flush()
				}

				server.logger.debug { "SENT packet ${packet.debugName} with id ${packet.id} in state ${packet.state}. [Compression: $compression, Socket: ${connection.socket.remoteAddress}]" }
			}
		}
	}

	/**
	 * Function to loop incoming data and handle it.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 */
	internal suspend fun handleIncoming(): Job {
		while (true) {
			val length = VarIntSerializer.readVarInt { connection.input.readByte() }
			val secondInt = VarIntSerializer.readVarInt { connection.input.readByte() }
			val secondIntLength = varIntBytesCount(secondInt)

			if (!compression) {
				val size = length - secondIntLength

				val data = if (size > 0) ByteArray(size) { connection.input.readByte() } else byteArrayOf()

				server.launch {
					IncomingPacketHandler.deserializeAndHandle(
						RawPacket.Found(secondInt, length, data),
						this@PacketHandle,
						server
					)
				}

				continue
			}

			println("Why are we here")

			val compressedArray = ByteArray(length - secondIntLength) { connection.input.readByte() }
			val decompressedBuffer = Buffer().also { it.write(Compressor.decompress(compressedArray)) }

			val id = VarIntSerializer.readVarInt { decompressedBuffer.readByte() }
			val idLength = varIntBytesCount(id)
			val data = ByteArray(secondInt - idLength) { decompressedBuffer.readByte() }
			server.launch {
				IncomingPacketHandler.deserializeAndHandle(RawPacket.Found(secondInt, length, data), this@PacketHandle, server)
			}
		}
	}
}