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
import kotlinx.coroutines.coroutineScope
import kotlinx.io.Buffer
import kotlinx.io.EOFException
import kotlinx.io.readByteArray
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer

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


	@Suppress("unchecked_cast")
	public suspend fun sendPacket(packet: OutgoingPacket) {
		@OptIn(InternalSerializationApi::class)
		val serializer = packet::class.serializer() as KSerializer<OutgoingPacket>
		val data = server.mcProtocol.encodeToByteArray(serializer, packet)
		val length = data.size + varIntBytesCount(packet.id)

		if (!compression) {
			val buffer = Buffer()
			writeVarInt(length, buffer::writeByte)
			writeVarInt(packet.id, buffer::writeByte)
			buffer.write(data)
			connection.output.writeFully(buffer.readByteArray())
		} else {
			TODO("Correct sending!")
//			val lengthLength = varIntBytesCount(length)
//			val compressedData = Compressor.compress(data)
//			val compressedLength = compressedData.size
//			connection.output.writeFully(Buffer().also { buffer ->
//				writeVarInt(compressedLength + lengthLength, buffer::writeByte)
//				writeVarInt(length, buffer::writeByte)
//				buffer.write(compressedData)
//			}.readByteArray())
		}
		connection.output.flush()
		server.logger.debug { "SENT packet ${packet.debugName} with id ${packet.id} in state ${packet.state}. [Compression: $compression, Socket: ${connection.socket.remoteAddress}]" }
	}

	/**
	 * Function to loop incoming data and handle it.
	 * @author Paul Kindler
	 * @since 01/11/2023
	 */
	internal suspend fun handleIncoming() = coroutineScope {
		try {
			while (!connection.socket.isClosed) {
				val length = VarIntSerializer.readVarInt { connection.input.readByte() }
				val secondInt = VarIntSerializer.readVarInt { connection.input.readByte() }
				val secondIntLength = varIntBytesCount(secondInt)

				if (!compression) {
					val size = length - secondIntLength

					val data = if (size > 0) ByteArray(size) { connection.input.readByte() } else byteArrayOf()

					IncomingPacketHandler.deserializeAndHandle(
						RawPacket.Found(secondInt, length, data),
						this@PacketHandle,
						server
					)

					continue
				}

				val compressedArray = ByteArray(length - secondIntLength) { connection.input.readByte() }
				val decompressedBuffer = Buffer().also { it.write(Compressor.decompress(compressedArray)) }

				val id = VarIntSerializer.readVarInt { decompressedBuffer.readByte() }
				val idLength = varIntBytesCount(id)
				val data = ByteArray(secondInt - idLength) { decompressedBuffer.readByte() }

				IncomingPacketHandler.deserializeAndHandle(RawPacket.Found(secondInt, length, data), this@PacketHandle, server)
			}
		} catch (_: EOFException) {
			connection.socket.close()
		} catch (e: Exception) {
			server.logger.error(e) { "Error while reading from channel." }
		}
	}
}