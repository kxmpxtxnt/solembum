package fyi.pauli.solembum.networking.packet.incoming.login

import fyi.pauli.solembum.networking.packet.incoming.IncomingPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlin.uuid.Uuid

@Serializable
public data class LoginStart(
	val name: String,
	@Contextual val uuid: Uuid,
) : IncomingPacket