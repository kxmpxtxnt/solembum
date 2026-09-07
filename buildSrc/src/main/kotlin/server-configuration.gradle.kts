plugins {
  kotlin("multiplatform")
}

repositories {
  maven("https://central.sonatype.com/repository/maven-snapshots/") {
    content {
      includeGroup("dev.whyoleg.cryptography")
    }
  }
}

kotlin {
  sourceSets {
    val ktorVersion = "3.5.2"
    val cryptographyVersion = "0.6.0"
    val kotlinLoggingVersion = "8.0.4"
    val koinVersion = "4.2.2"

    commonMain.dependencies {
      api(project(":nbt"))
      api(project(":protocol"))

      api("org.jetbrains.kotlinx:kotlinx-datetime:0.8.0-0.6.x-compat")
      api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
      api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")

      api("io.ktor:ktor-network:$ktorVersion")
      api("io.ktor:ktor-server-cio:$ktorVersion")
      api("io.ktor:ktor-server-core:$ktorVersion")
      api("io.ktor:ktor-server-websockets:$ktorVersion")

      api("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

      api("io.ktor:ktor-client-cio:$ktorVersion")
      api("io.ktor:ktor-client-core:$ktorVersion")
      api("io.ktor:ktor-client-content-negotiation:$ktorVersion")

      api("dev.whyoleg.cryptography:cryptography-core:$cryptographyVersion")
      api("dev.whyoleg.cryptography:cryptography-provider-optimal:${cryptographyVersion}")

      api("com.akuleshov7:ktoml-core:0.7.1")

      api("io.github.oshai:kotlin-logging:$kotlinLoggingVersion")

      api("io.insert-koin:koin-core:$koinVersion")
      api("io.insert-koin:koin-core-coroutines:${koinVersion}")
    }

    commonTest.dependencies {
      api("io.insert-koin:koin-test:$koinVersion")
    }

    jvmMain.dependencies {
      api("dev.whyoleg.cryptography:cryptography-provider-jdk:${cryptographyVersion}")
    }

    jvmTest.dependencies {
      api("org.junit.jupiter:junit-jupiter-engine:6.1.3")
    }

    macosMain.dependencies {
      api("dev.whyoleg.cryptography:cryptography-provider-apple:${cryptographyVersion}")
    }

    linuxMain.dependencies {
      api("dev.whyoleg.cryptography:cryptography-provider-openssl3-api:${cryptographyVersion}")
    }
  }
}