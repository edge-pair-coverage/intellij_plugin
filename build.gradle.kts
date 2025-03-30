plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.24"
    id("org.jetbrains.intellij.platform") version "2.4.0"
    id("org.jetbrains.intellij.platform.migration") version "2.4.0"
}

group = "org.juancatalan"
version = "1.0.2"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        val type = providers.gradleProperty("platformType")
        val version = providers.gradleProperty("platformVersion")

        create(type, version)

        bundledPlugins(listOf("java", "JUnit"))
        // plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })
    }
}

tasks {
    // Set the JVM compatibility versions

    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    /*
        patchPluginXml {
            sinceBuild.set("232")
            untilBuild.set("251.*")
        }

        signPlugin {
            certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
            privateKey.set(System.getenv("PRIVATE_KEY"))
            password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
        }

        publishPlugin {
            token.set(System.getenv("PUBLISH_TOKEN"))
        }

         */
}
