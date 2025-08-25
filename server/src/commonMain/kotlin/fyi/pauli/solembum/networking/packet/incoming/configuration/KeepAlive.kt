package fyi.pauli.solembum.networking.packet.incoming.configuration

import kotlinx.serialization.Serializable

/**
 * The server will frequently send out a keep-alive, each containing a random ID.
 * The client must respond with the same packet.
 *
 * @param id ID to check the client response
 */
@Serializable
public data class KeepAlive(var id: Long) : fyi.pauli.solembum.networking.packet.incoming.IncomingPacket