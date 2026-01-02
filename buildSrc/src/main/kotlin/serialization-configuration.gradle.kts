plugins {
  kotlin("multiplatform")
  kotlin("plugin.serialization")
}

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.8.2")
      implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.10.0-RC")
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
      implementation(kotlin("test-common"))
      implementation(kotlin("test-annotations-common"))
    }
  }
}