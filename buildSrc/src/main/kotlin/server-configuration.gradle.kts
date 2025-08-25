plugins {
  id("serialization-configuration")
}

kotlin {
  sourceSets {
    val ktorVersion: String = "3.2.3"
    val cryptographyVersion: String = "0.5.0"
    val kotlinLoggingVersion: String = "7.0.13"
    val koinVersion: String = "4.1.0"

    commonMain.dependencies {
      implementation("fyi.pauli.solembum:nbt:0.1")
      implementation("fyi.pauli.solembum:protocol:0.1")

      implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
      implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
      implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")

      implementation("io.ktor:ktor-network:$ktorVersion")
      implementation("io.ktor:ktor-server-cio:$ktorVersion")
      implementation("io.ktor:ktor-server-core:$ktorVersion")
      implementation("io.ktor:ktor-server-websockets:$ktorVersion")

      implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

      implementation("io.ktor:ktor-client-cio:$ktorVersion")
      implementation("io.ktor:ktor-client-core:$ktorVersion")
      implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

      implementation("dev.whyoleg.cryptography:cryptography-core:$cryptographyVersion")

      implementation("com.akuleshov7:ktoml-core:0.7.1")

      implementation("io.github.oshai:kotlin-logging:$kotlinLoggingVersion")

      implementation("io.insert-koin:koin-core:$koinVersion")
      implementation("io.insert-koin:koin-core-coroutines:${koinVersion}")
    }

    commonTest.dependencies {
      implementation("io.insert-koin:koin-test:$koinVersion")
    }

    jvmMain.dependencies {
      implementation("dev.whyoleg.cryptography:cryptography-provider-jdk:${cryptographyVersion}")
    }

    jvmTest.dependencies {
      implementation("org.junit.jupiter:junit-jupiter-engine:5.13.4")
    }

    macosMain.dependencies {
      implementation("dev.whyoleg.cryptography:cryptography-provider-apple:${cryptographyVersion}")
    }

    linuxMain.dependencies {
      implementation("dev.whyoleg.cryptography:cryptography-provider-openssl3-api:${cryptographyVersion}")
    }
  }
}