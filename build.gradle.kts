plugins {
    java
    id("io.papermc.paperweight.userdev") version "1.1.11"
    id("xyz.jpenilla.run-paper") version "1.0.4"
}

group = "lt.vu.mif.it.paskui"
version = "0.3"

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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
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
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }
}