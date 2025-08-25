package fyi.pauli.solembum.entity.player

/**
 * Default implementation for the player.
 * @property profile userprofile of the player.
 * @property handle connection handle of the player.
 * @author Paul Kindler
 * @since 01/11/2023
 */
public class Player(
	public val profile: fyi.pauli.solembum.entity.player.UserProfile,
	public val handle: fyi.pauli.solembum.networking.packet.PacketHandle,
) : fyi.pauli.solembum.entity.Entity.Creature

