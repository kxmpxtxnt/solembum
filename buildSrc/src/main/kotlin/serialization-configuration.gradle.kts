plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.9.3")
      implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.13.0")
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
    }
  }
}