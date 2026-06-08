package fyi.pauli.solembum.networking.packet

import dev.whyoleg.cryptography.operations.Cipher
import fyi.pauli.solembum.networking.packet.incoming.IncomingPacketHandler
import fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket
import fyi.pauli.solembum.networking.serialization.RawPacket
import fyi.pauli.solembum.protocol.serialization.types.primitives.VarInt
import fyi.pauli.solembum.server.Server
import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.coroutineScope
import kotlinx.io.Buffer
import kotlinx.io.EOFException
import kotlinx.io.Source
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
	public var cipher: Cipher? = null,
	internal val server: Server,
) {

	@Suppress("unchecked_cast")
	public suspend fun sendPacket(packet: OutgoingPacket) {
		@OptIn(InternalSerializationApi::class)
		val serializer = packet::class.serializer() as KSerializer<OutgoingPacket>
		val data = server.mcProtocol.encodeToByteArray(serializer, packet)
		val length = data.size + VarInt.bytesCount(packet.id)

		val buffer = Buffer()

		if (!compression) {
			VarInt.write(length, buffer::writeByte)
			VarInt.write(packet.id, buffer::writeByte)
			buffer.write(data)
		} else {
			TODO("Handle compression")
		}

		val output = when (cipher) {
			null -> buffer.readByteArray()
			else -> cipher!!.encrypt(buffer.readByteArray())
		}

		connection.output.writeFully(output)
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
			val input = when (cipher) {
				null -> connection.input
				else -> ByteReadChannel(
					cipher!!.decryptingSource(connection.input.asSource()) as Source
				)
			}

			while (!connection.socket.isClosed) {
				val length = VarInt.read { input.readByte() }
				val idOrDataLength = VarInt.read { input.readByte() }
				val lengthOfIdOrDataLength = VarInt.bytesCount(idOrDataLength)

				if (!compression) {
					val size = length - lengthOfIdOrDataLength
					val data = if (size > 0) ByteArray(size) { input.readByte() } else byteArrayOf()

					IncomingPacketHandler.deserializeAndHandle(
						RawPacket.Found(idOrDataLength, length, data),
						this@PacketHandle,
						server
					)

					continue
				}

				TODO("Handle compression")
			}
		} catch (_: EOFException) {
			server.logger.debug { "CLOSED connection (Socket: ${connection.socket.remoteAddress})" }
		} catch (_: ClosedReceiveChannelException) {
			server.logger.debug { "CLOSED connection (Socket: ${connection.socket.remoteAddress})" }
		} catch (e: Exception) {
			server.logger.error(e) { "Error while reading from channel." }
		}
	}
}