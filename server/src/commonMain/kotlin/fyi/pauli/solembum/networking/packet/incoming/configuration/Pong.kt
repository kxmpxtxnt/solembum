package fyi.pauli.solembum.networking.packet.incoming.configuration

import kotlinx.serialization.Serializable

/**
 * Response to the outgoing packet (Ping) with the same id.
 *
 * @param id id is the same as the ping packet
 */
@Serializable
public data class Pong(@NumberType var id: Int) : fyi.pauli.solembum.networking.packet.incoming.IncomingPacket