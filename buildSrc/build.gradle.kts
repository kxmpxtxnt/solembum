plugins {
  `kotlin-dsl`
  kotlin("plugin.serialization") version embeddedKotlinVersion
}

repositories {
  mavenCentral()
  gradlePluginPortal()
  maven("https://repo.pauli.fyi/releases")
}

dependencies {
  fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"

  val kotlinVersion = "2.2.10"

  implementation(kotlin("gradle-plugin", kotlinVersion))

  compileOnly(plugin("org.jetbrains.kotlin.plugin.serialization", embeddedKotlinVersion))
  runtimeOnly(plugin("org.jetbrains.kotlin.plugin.serialization", kotlinVersion))
}