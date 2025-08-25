package fyi.pauli.solembum.protocol.desc

import fyi.pauli.solembum.protocol.serialization.types.primitives.MinecraftEnumType
import fyi.pauli.solembum.protocol.serialization.types.primitives.MinecraftNumberType

internal data class ProtocolDesc(
	val type: MinecraftNumberType,
	val maxStringLength: Int,
)

internal data class ProtocolEnumDesc(
	val type: MinecraftEnumType,
	val stringMaxLength: Int,
)

internal data class ProtocolEnumElementDesc(
	val ordinal: Int,
)