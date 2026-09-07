plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.9.1")
      implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.11.0")
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
      implementation(kotlin("test-common"))
      implementation(kotlin("test-annotations-common"))
    }
  }
}