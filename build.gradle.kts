plugins {
    kotlin("jvm") version "1.8.20"
    kotlin("plugin.serialization") version "1.8.20"
}

group = "io.hapix"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    testImplementation("com.squareup.retrofit2:retrofit:2.9.0")
    testImplementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
}

tasks.test {
    useJUnitPlatform()
}

/**
 * JVM20への対応は2023/7予定
 * https://youtrack.jetbrains.com/issue/KT-57669
 */
kotlin {
    jvmToolchain(17)
}