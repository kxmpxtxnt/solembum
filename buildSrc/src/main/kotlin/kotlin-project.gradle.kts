import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
  kotlin("multiplatform")
}

kotlin {
  explicitApi()

  compilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")

    apiVersion = KotlinVersion.KOTLIN_2_2
    languageVersion = KotlinVersion.KOTLIN_2_2
  }

  jvm()

  linuxX64()
  linuxArm64()

  mingwX64()

  macosX64()
  macosArm64()

  applyDefaultHierarchyTemplate()

  sourceSets {
    all {
      languageSettings.optIn("kotlin.uuid.ExperimentalUuidApi")
    }

    mingwMain {
      dependsOn(linuxMain.get())
    }
  }
}