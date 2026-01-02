import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
  id("com.vanniktech.maven.publish")
}

publishing {
  repositories {
    maven("https://repo.pauli.fyi/releases") {
      name = "pauli"
      credentials(PasswordCredentials::class)
    }
  }
}

mavenPublishing {
  configure(KotlinMultiplatform())

  publishToMavenCentral(false)

  signAllPublications()

  pom {
    name = project.name
    description = project.description

    inceptionYear = "2023"

    url = "https://github.com/kxmpxtxnt/solembum"

    licenses {
      license {
        name = "Apache License 2.0"
        url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
        distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
      }
    }

    developers {
      developer {
        id = "kxmpxtxnt"
        name = "Paul Kindler"
        url = "https://github.com/kxmpxtxnt"
      }
    }

    scm {
      url = "https://github.com/kxmpxtxnt/solembum"
      connection = "scm:git:git://github.com/kxmpxtxnt/solembum.git"
      developerConnection = "scm:git:ssh://git@github.com/kxmpxtxnt/solembum.git"
    }
  }
}

tasks.register("publishAllPublicationsToAllRepositories") {
  dependsOn(
    "publishAllPublicationsToPauliRepository",
    //"publishAllPublicationsToMavenCentralRepository"
  )
}
