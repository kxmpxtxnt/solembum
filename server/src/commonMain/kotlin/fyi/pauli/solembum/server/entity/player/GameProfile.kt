package fyi.pauli.solembum.server.entity.player

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys
import kotlin.uuid.Uuid

/**
 * Userprofile of a connection.
 * @property uuid uuid of a connection.
 * @property username username of a connection-
 * @property properties list of properties of a connection.
 * @author Paul Kindler
 * @since 01/11/2023
 */
@Serializable
public data class GameProfile(
	@SerialName("id") val uuid: Uuid,
	@SerialName("name") val username: String,
	val properties: List<Property>,
	val profileActions: List<ProfileAction>,
)

/**
 * Property data class used in the UserProfile.
 * @property name name of the property.
 * @property value value of the property.
 * @property signature signature of the property.
 * @author Paul Kindler
 * @since 01/11/2023
 */
@Serializable
public data class Property(val name: String, val value: String, val signature: String)

/**
 * Actions set by Mojang.
 *
 * TODO: Add fitting response to those actions.
 */
public enum class ProfileAction {
	FORCED_NAME_CHANGE,
	USING_BANNED_SKIN
}