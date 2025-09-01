plugins {
  `kotlin-dsl`
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

  implementation(plugin("com.vanniktech.maven.publish", "0.34.0"))
}