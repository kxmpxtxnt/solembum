package fyi.pauli.solembum.networking.packet.incoming

import kotlin.reflect.KClass

public object PacketRegistry {
	public val incomingPackets: MutableList<fyi.pauli.solembum.networking.packet.incoming.RegisteredIncomingPacket> =
		mutableListOf()
}

public data class RegisteredIncomingPacket(
	val identifier: fyi.pauli.solembum.networking.packet.incoming.PacketIdentifier,
	var kClass: KClass<out fyi.pauli.solembum.networking.packet.incoming.IncomingPacket>,
	val receivers: MutableMap<fyi.pauli.solembum.models.Identifier, fyi.pauli.solembum.networking.packet.PacketReceiver<fyi.pauli.solembum.networking.packet.incoming.IncomingPacket>>,
)