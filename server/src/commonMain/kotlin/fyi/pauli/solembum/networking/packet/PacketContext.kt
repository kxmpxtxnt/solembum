package fyi.pauli.solembum.networking.packet

import kotlin.uuid.Uuid

public data class PacketContext(
	var username: String? = null,
	var loginUuid: Uuid? = null
)