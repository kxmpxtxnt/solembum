import org.apache.tools.ant.taskdefs.condition.Os

plugins {
  kotlin("multiplatform")
}

kotlin {
  sourceSets {
    jvm()

    mingwX64()

    linuxX64()
    linuxArm64()

    if (Os.isFamily(Os.FAMILY_MAC)) {
      macosX64()
      macosArm64()
    }

    explicitApi()
    applyDefaultHierarchyTemplate()

    mingwMain {
      dependsOn(linuxMain.get())
    }
  }
}