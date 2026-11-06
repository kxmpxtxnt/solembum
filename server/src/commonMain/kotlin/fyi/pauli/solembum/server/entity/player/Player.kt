package fyi.pauli.solembum.server.entity.player

import fyi.pauli.solembum.server.entity.Entity
import fyi.pauli.solembum.networking.packet.PacketHandle

/**
 * Default implementation for the player.
 * @property profile userprofile of the player.
 * @property handle connection handle of the player.
 * @author Paul Kindler
 * @since 01/11/2023
 */
public class Player(
	public val profile: GameProfile,
	public val handle: PacketHandle,
) : Entity.Creature

