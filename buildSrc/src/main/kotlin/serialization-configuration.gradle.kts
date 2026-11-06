plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.9.2")
      implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.12.0")
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
    }
  }
}