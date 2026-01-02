package fyi.pauli.solembum.networking.packet

import fyi.pauli.solembum.models.Identifier
import fyi.pauli.solembum.networking.packet.incoming.IncomingPacket
import fyi.pauli.solembum.networking.packet.incoming.PacketIdentifier
import fyi.pauli.solembum.networking.packet.outgoing.OutgoingPacket
import fyi.pauli.solembum.networking.packet.outgoing.status.PingResponse
import fyi.pauli.solembum.networking.packet.outgoing.status.StatusResponse
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.reflect.KClass

public object PacketRegistry {
	public val incomingPackets: MutableList<RegisteredIncomingPacket> = mutableListOf()
}

public data class RegisteredIncomingPacket(
	val identifier: PacketIdentifier,
	var kClass: KClass<out IncomingPacket>,
	val receivers: MutableMap<Identifier, PacketReceiver<IncomingPacket>>,
)