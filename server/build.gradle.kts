plugins {
  `kotlin-project`
  `server-configuration`
}

kotlin {
  targets.configureEach {
    compilations.configureEach {
      compileTaskProvider.configure {
        compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
      }
    }
  }

  sourceSets {
    all {
      languageSettings.optIn("fyi.pauli.solembum.extensions.internal.InternalGaiaApi")
      languageSettings.optIn("kotlin.uuid.ExperimentalUuidApi")
      languageSettings.enableLanguageFeature("InlineClasses")
    }
  }
}