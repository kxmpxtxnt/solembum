package fyi.pauli.solembum.networking.packet.incoming.login

import com.benasher44.uuid.Uuid
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * Packet, used to start the login sequence.
 *
 * @param name Player's Username.
 * @param uuid The UUID of the player logging in.
 */
@Serializable
public data class LoginStart(
	var name: String, var uuid: @Contextual Uuid,
) : fyi.pauli.solembum.networking.packet.incoming.IncomingPacket