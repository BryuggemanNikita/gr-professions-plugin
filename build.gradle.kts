plugins {
    id("java-library")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
    id("checkstyle")
}

group = "org.owleebr"
version = "1.0.0"
description = "Плагин работы с расами"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.purpurmc.org/snapshots")
    maven("https://jitpack.io")

}

dependencies {
    paperweight.devBundle("org.purpurmc.purpur", "1.21.5-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    compileOnly("com.github.dmulloy2:ProtocolLib:5.3.0") // Replace `dev` with correct version once available

}

configure<CheckstyleExtension> {
    toolVersion = "10.25.0"
    configFile = rootProject.file("config/checkstyle/google_checks.xml")
}

tasks {
    compileJava {
        options.release = 21
    }
}
