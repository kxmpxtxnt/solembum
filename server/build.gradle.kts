plugins {
  `kotlin-project`
  `maven-publishing`
  `server-configuration`
  `serialization-configuration`
}

kotlin {
  sourceSets {
    all {
      languageSettings.optIn("fyi.pauli.solembum.extensions.internal.InternalSolembumApi")
    }
  }
}