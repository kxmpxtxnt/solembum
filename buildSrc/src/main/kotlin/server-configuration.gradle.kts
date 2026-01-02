plugins {
  kotlin("multiplatform")
}

kotlin {
  sourceSets {
    val ktorVersion = "3.3.3"
    val cryptographyVersion = "0.5.0"
    val kotlinLoggingVersion = "7.0.14"
    val koinVersion = "4.2.0"

    commonMain.dependencies {
      api(project(":nbt"))
      api(project(":protocol"))

      api("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
      api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
      api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0-RC")

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
      api("org.junit.jupiter:junit-jupiter-engine:6.0.1")
    }

    macosMain.dependencies {
      api("dev.whyoleg.cryptography:cryptography-provider-apple:${cryptographyVersion}")
    }

    linuxMain.dependencies {
      api("dev.whyoleg.cryptography:cryptography-provider-openssl3-api:${cryptographyVersion}")
    }
  }
}