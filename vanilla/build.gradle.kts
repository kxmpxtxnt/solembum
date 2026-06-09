plugins {
  `kotlin-project`
  `serialization-configuration`
}

kotlin {
  sourceSets {
    commonMain {
      dependencies {
        implementation(project(":server"))
      }
    }

    jvmMain {
      dependencies {
        runtimeOnly("ch.qos.logback:logback-classic:1.5.23")
      }
    }
  }
}