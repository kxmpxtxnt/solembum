package fyi.pauli.solembum.network.receivers.login

import dev.whyoleg.cryptography.algorithms.RSA
import fyi.pauli.solembum.config.ServerConfig
import fyi.pauli.solembum.networking.packet.PacketHandle
import fyi.pauli.solembum.networking.packet.PacketReceiver
import fyi.pauli.solembum.networking.packet.incoming.login.LoginStart
import fyi.pauli.solembum.networking.packet.outgoing.login.EncryptionRequest
import fyi.pauli.solembum.server.Server
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

public object LoginStartReceiver : PacketReceiver<LoginStart>, KoinComponent {

	public val config: ServerConfig by inject()

	override suspend fun onReceive(
		packet: LoginStart,
		packetHandle: PacketHandle,
		server: Server,
	) {
		if (config.server.encryption.enabled) {
			packetHandle.sendPacket(EncryptionRequest(
				server.serverName,
				server.encryptionPair.publicKey.encodeToByteArray(RSA.PublicKey.Format.DER),
				server.verifyToken,
				config.server.auth.mojangAuth
			))
		}
	}
}