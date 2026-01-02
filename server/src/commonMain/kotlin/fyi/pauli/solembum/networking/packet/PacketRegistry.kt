package fyi.pauli.solembum.networking.packet

import fyi.pauli.solembum.models.Identifier
import fyi.pauli.solembum.networking.packet.incoming.IncomingPacket
import fyi.pauli.solembum.networking.packet.incoming.PacketIdentifier
import kotlin.reflect.KClass

public object PacketRegistry {
	public val incomingPackets: MutableList<RegisteredIncomingPacket> = mutableListOf()
}

public data class RegisteredIncomingPacket(
	val identifier: PacketIdentifier,
	var kClass: KClass<out IncomingPacket>,
	val receivers: MutableMap<Identifier, PacketReceiver<IncomingPacket>>,
)