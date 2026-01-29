plugins {
    kotlin("jvm") version "2.0.21"
    id("com.gradleup.shadow") version "8.3.5"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "com.skittlemc"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib"))
}

kotlin {
    jvmToolchain(21)
}

tasks {
    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    runServer {
        minecraftVersion("1.21.4")
    }

    shadowJar {
        archiveClassifier.set("")
        mergeServiceFiles()

        relocate("kotlin", "com.skittlemc.ritualforge.shaded.kotlin")
    }

    build {
        dependsOn(shadowJar)
    }
}
