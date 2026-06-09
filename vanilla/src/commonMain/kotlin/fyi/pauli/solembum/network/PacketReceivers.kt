package fyi.pauli.solembum.network

import fyi.pauli.solembum.models.Identifier
import fyi.pauli.solembum.network.receivers.handshake.HandshakeReceiver
import fyi.pauli.solembum.network.receivers.login.EncryptionResponseReceiver
import fyi.pauli.solembum.network.receivers.login.LoginStartReceiver
import fyi.pauli.solembum.network.receivers.status.PingRequestReceiver
import fyi.pauli.solembum.network.receivers.status.StatusRequestReceiver
import fyi.pauli.solembum.networking.packet.PacketReceiver
import fyi.pauli.solembum.networking.packet.PacketRegistry
import fyi.pauli.solembum.networking.packet.State
import fyi.pauli.solembum.networking.packet.incoming.IncomingPacket

public object PacketReceivers {
	private val identifier = Identifier("werecat", "vanilla-receiver")

	@Suppress("UNCHECKED_CAST")
	internal fun registerVanillaReceivers() {
		fun registerReceiver(state: State, id: Int, vararg receivers: PacketReceiver<*>) {
			val registeredPacket =
				PacketRegistry.incomingPackets.first { it.identifier.state == state && it.identifier.id == id }

			receivers.filterIsInstance<PacketReceiver<IncomingPacket>>().forEach { receiver ->
				registeredPacket.receivers[identifier] = receiver
			}
		}

		registerReceiver(State.HANDSHAKING, 0x00, HandshakeReceiver)

		registerReceiver(State.STATUS, 0x00, StatusRequestReceiver)
		registerReceiver(State.STATUS, 0x01, PingRequestReceiver)

		registerReceiver(State.LOGIN, 0x00, LoginStartReceiver)
		registerReceiver(State.LOGIN, 0x01, EncryptionResponseReceiver)
	}
}