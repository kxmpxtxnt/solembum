package fyi.pauli.solembum.networking.packet.incoming.login

import kotlinx.serialization.Serializable

/**
 * Acknowledgement to the Login Success packet sent by the server.
 */
@Serializable
public class LoginAcknowledged : fyi.pauli.solembum.networking.packet.incoming.IncomingPacket