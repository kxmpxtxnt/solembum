package fyi.pauli.solembum.networking.packet.incoming

import fyi.pauli.solembum.networking.packet.PacketHandle
import fyi.pauli.solembum.networking.packet.PacketRegistry
import fyi.pauli.solembum.networking.packet.RegisteredIncomingPacket
import fyi.pauli.solembum.networking.packet.State
import fyi.pauli.solembum.networking.packet.incoming.handshaking.Handshake
import fyi.pauli.solembum.networking.packet.incoming.login.EncryptionResponse
import fyi.pauli.solembum.networking.packet.incoming.login.LoginStart
import fyi.pauli.solembum.networking.packet.incoming.status.PingRequest
import fyi.pauli.solembum.networking.packet.incoming.status.StatusRequest
import fyi.pauli.solembum.networking.serialization.RawPacket
import fyi.pauli.solembum.server.Server
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

public object IncomingPacketHandler {
	@OptIn(InternalSerializationApi::class)
	internal suspend fun deserializeAndHandle(
		rawPacket: RawPacket,
		packetHandle: PacketHandle,
		server: Server,
	) {
		if (rawPacket is RawPacket.NotFound) {
			server.logger.warn { "Cannot find packet with length ${rawPacket.length}." }
			return
		}
		rawPacket as RawPacket.Found

		val clientPacket =
			PacketRegistry.incomingPackets.firstOrNull { it.identifier.id == rawPacket.id && it.identifier.state == packetHandle.state }
				?: error("Cannot find packet with id ${rawPacket.id} in state ${packetHandle.state} (Socket: ${packetHandle.connection.socket.remoteAddress})")

		server.logger.debug { "RECEIVED ${clientPacket.identifier.debuggingName} in state ${packetHandle.state.debugName} (Socket: ${packetHandle.connection.socket.remoteAddress})" }
		val packet = server.mcProtocol.decodeFromByteArray(clientPacket.kClass.serializer(), rawPacket.data)

		clientPacket.receivers.forEach { (_, receiver) ->
			receiver.onReceive(packet, packetHandle, server)
		}
	}

	public fun registerJoinPackets() {
		fun createPacket(
			state: State,
			id: Int,
			name: String,
			kClass: KClass<out IncomingPacket>,
		): RegisteredIncomingPacket =
			RegisteredIncomingPacket(
				PacketIdentifier(id, state, name),
				kClass,
				mutableMapOf()
			)

		fun createLoginPacket(
			id: Int, name: String, kClass: KClass<out IncomingPacket>,
		): RegisteredIncomingPacket =
			createPacket(State.LOGIN, id, name, kClass)

		fun createConfigurationPacket(
			id: Int, name: String, kClass: KClass<out IncomingPacket>,
		) = createPacket(State.CONFIGURATION, id, name, kClass)

		val handshakePackets = listOf(
			createPacket(State.HANDSHAKING, 0x00, "Handshake", Handshake::class)
		)

		val statusPackets = listOf(
			createPacket(State.STATUS, 0x00, "Status Request", StatusRequest::class),
			createPacket(State.STATUS, 0x01, "Ping Request", PingRequest::class)
		)

		val loginPackets = listOf(
			createLoginPacket(0x00, "Login Start", LoginStart::class),
			createLoginPacket(0x01, "Encryption Response", EncryptionResponse::class)
		)

		val configurationPackets = listOf<RegisteredIncomingPacket>()

		PacketRegistry.incomingPackets.addAll(
			listOf(handshakePackets, statusPackets, loginPackets, configurationPackets).flatten()
		)
	}
}