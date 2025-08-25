plugins {
  `maven-publish`
}

publishing {
  repositories {
    maven {
      name = "pauli"
      url = uri("https://repo.pauli.fyi/releases")
      credentials(PasswordCredentials::class.java)
    }
  }

  publications {
    withType<MavenPublication>().configureEach {
      pom {
        name = project.name
        description = project.description

        developers {
          developer { name = "kxmpxtxnt" }
        }

        licenses {
          license {
            name = "Apache License 2.0"
            url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
          }
        }

        url = "https://github.com/kxmpxtxnt/solembum"

        scm {
          connection = "scm:git:git://github.com/kxmpxtxnt/solembum.git"
          url = "https://github.com/kxmpxtxnt/solembum"
        }
      }
    }
  }
}