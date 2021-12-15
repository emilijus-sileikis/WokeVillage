plugins {
    java
    id("io.papermc.paperweight.userdev") version "1.1.14"
    id("xyz.jpenilla.run-paper") version "1.0.4"
}

group = "lt.vu.mif.it.paskui"
version = "0.7"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    paperDevBundle("1.17.1-R0.1-SNAPSHOT")
    testImplementation(platform("org.junit:junit-bom:5.8.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks {
    build {
        dependsOn(reobfJar)
    }

    test {
        useJUnitPlatform()
    }

    processResources {
        filteringCharset = "UTF-8"
        val projectData = mapOf(
            "name" to project.properties["name"],
            "version" to project.properties["version"]
        )
        filesMatching("plugin.yml") {
            expand(projectData)
        }
    }
}