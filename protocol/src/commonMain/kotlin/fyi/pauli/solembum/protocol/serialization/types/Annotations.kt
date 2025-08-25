@file:OptIn(ExperimentalSerializationApi::class)

package fyi.pauli.solembum.protocol.serialization.types

import fyi.pauli.solembum.protocol.serialization.types.primitives.MinecraftEnumType
import fyi.pauli.solembum.protocol.serialization.types.primitives.MinecraftNumberType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class NumberType(
	val type: MinecraftNumberType = MinecraftNumberType.DEFAULT,
)

@SerialInfo
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
public annotation class StringLength(
	val maxLength: Int,
)

@SerialInfo
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
public annotation class EnumType(
	val type: MinecraftEnumType = MinecraftEnumType.VAR_INT,
)

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class EnumSerial(
	val ordinal: Int,
)

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
public annotation class Unprefixed