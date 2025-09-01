package fyi.pauli.solembum.networking.packet.incoming.configuration

import fyi.pauli.solembum.networking.packet.incoming.IncomingPacket
import fyi.pauli.solembum.protocol.serialization.types.NumberType
import kotlinx.serialization.Serializable

/**
 * Response to the outgoing packet (Ping) with the same id.
 *
 * @param id id is the same as the ping packet
 */
@Serializable
public data class Pong(@NumberType var id: Int) : IncomingPacket