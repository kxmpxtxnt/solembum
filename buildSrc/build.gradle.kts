plugins {
  `kotlin-dsl`
}

repositories {
  mavenCentral()
  gradlePluginPortal()
  maven("https://repo.pauli.fyi/releases")
  maven("https://central.sonatype.com/repository/maven-snapshots/") {
    content {
      includeGroup("dev.whyoleg.cryptography")
    }
  }
}


dependencies {
  fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"

  val kotlinVersion = "2.4.20"

  implementation(kotlin("gradle-plugin", kotlinVersion))

  compileOnly(plugin("org.jetbrains.kotlin.plugin.serialization", embeddedKotlinVersion))
  runtimeOnly(plugin("org.jetbrains.kotlin.plugin.serialization", kotlinVersion))

  implementation(plugin("com.vanniktech.maven.publish", "0.36.0"))
}