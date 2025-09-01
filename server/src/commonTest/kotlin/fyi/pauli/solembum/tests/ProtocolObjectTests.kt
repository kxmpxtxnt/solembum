package fyi.pauli.solembum.tests

import fyi.pauli.solembum.entity.player.Property
import fyi.pauli.solembum.entity.player.UserProfile
import fyi.pauli.solembum.models.Identifier
import fyi.pauli.solembum.nbt.extensions.buildCompoundTag
import fyi.pauli.solembum.nbt.serialization.types.CompoundTag
import fyi.pauli.solembum.protocol.MinecraftProtocol
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.uuid.Uuid

class ProtocolObjectTests {
	@Serializable
	data class Objects(
		val identifier: Identifier,
		val tag: CompoundTag,
		val uuid: Uuid,
		val userProfile: UserProfile,
	)

	@Test
	fun checkObjectSerializing() {
		val objects = Objects(
			Identifier("solembum", "test"),
			buildCompoundTag {
				put("inttest", 1)
			},
			Uuid.parse("df3706c0-c421-419f-9460-a64c15bc8b3f"),
			UserProfile(
				Uuid.parse("df3706c0-c421-419f-9460-a64c15bc8b3f"),
				"kxmpxtxnt",
				listOf(Property("ojojojoj", "jojoj", "signature")),
				listOf()
			)
		)

		val mc = MinecraftProtocol()

		val encoded = mc.decodeFromByteArray<Objects>(mc.encodeToByteArray(objects))

		assertEquals(objects, encoded)
	}
}