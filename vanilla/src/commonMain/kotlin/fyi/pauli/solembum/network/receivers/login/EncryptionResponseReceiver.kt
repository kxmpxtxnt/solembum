@file:OptIn(DelicateCryptographyApi::class)

package fyi.pauli.solembum.network.receivers.login

import dev.whyoleg.cryptography.DelicateCryptographyApi
import dev.whyoleg.cryptography.algorithms.AES
import dev.whyoleg.cryptography.algorithms.RSA
import dev.whyoleg.cryptography.algorithms.SHA1
import fyi.pauli.solembum.extensions.toSignedHexString
import fyi.pauli.solembum.mojang.api.Session
import fyi.pauli.solembum.networking.packet.PacketHandle
import fyi.pauli.solembum.networking.packet.PacketReceiver
import fyi.pauli.solembum.networking.packet.incoming.login.EncryptionResponse
import fyi.pauli.solembum.networking.packet.outgoing.login.LoginSuccess
import fyi.pauli.solembum.server.Server
import fyi.pauli.solembum.server.cryptographyProvider

public object EncryptionResponseReceiver : PacketReceiver<EncryptionResponse> {
	override suspend fun onReceive(
		packet: EncryptionResponse,
		packetHandle: PacketHandle,
		server: Server,
	) {
		try {
			val rsaDecryptor = server.encryptionPair.privateKey.decryptor()

			val decryptedVerifyToken = rsaDecryptor.decrypt(packet.verifyToken)
			if (!decryptedVerifyToken.contentEquals(server.verifyToken)) {
				server.logger.debug { "ENCRYPTION verifyToken did not match (Socket: ${packetHandle.connection.socket.remoteAddress})" }
				return
			}

			val decryptedSecret = rsaDecryptor.decrypt(packet.sharedSecret)

			val cfb8 = server.cryptographyProvider.get(AES.CFB8)
			val secretKey = cfb8.keyDecoder().decodeFromByteArray(AES.Key.Format.RAW, decryptedSecret)
			packetHandle.cipher = secretKey.cipher()

			val publicKeyDer = server.encryptionPair.publicKey.encodeToByteArray(RSA.PublicKey.Format.DER)
			val serverIdBytes = server.serverName.encodeToByteArray()
			val digest = server.cryptographyProvider.get(SHA1).hasher().hash(serverIdBytes + decryptedSecret + publicKeyDer)
			val serverId = digest.toSignedHexString()

			val gameProfile = Session.hasJoined(packetHandle, serverId)

			packetHandle.sendPacket(LoginSuccess(gameProfile))

			server.logger.debug { "ENCRYPTION enabled (Socket: ${packetHandle.connection.socket.remoteAddress})" }
		} catch (e: Exception) {
			server.logger.error(e) { "ENCRYPTION failed (Socket: ${packetHandle.connection.socket.remoteAddress})" }
		}
	}
}
