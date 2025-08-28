package fyi.pauli.solembum.networking.packet.incoming.login

import fyi.pauli.solembum.networking.packet.incoming.IncomingPacket
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

/**
 * Packet, used to start the login sequence.
 *
 * @param name Player's Username.
 * @param uuid The UUID of the player logging in.
 */
@Serializable
public data class LoginStart(
	var name: String, var uuid: Uuid,
) : IncomingPacket