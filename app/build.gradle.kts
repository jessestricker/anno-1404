plugins {
    kotlin("jvm") version "1.9.20"
    application
}

repositories {
    mavenCentral()
}

dependencies {}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    mainClass = "org.example.AppKt"
}
