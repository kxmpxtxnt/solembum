package fyi.pauli.solembum.networking.packet.incoming.status

import kotlinx.serialization.Serializable

/**
 * Requests the ping.
 *
 * @param payload May be any number. Notchian clients use a system-dependent time value which is counted in milliseconds.
 */
@Serializable
public data class PingRequest(var payload: Long) : fyi.pauli.solembum.networking.packet.incoming.IncomingPacket