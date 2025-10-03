plugins {
    id("java-library")
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.17"
    id("checkstyle")
    id("com.gradleup.shadow") version "8.3.0"
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
    //compileOnly("com.github.dmulloy2:ProtocolLib:5.3.0") // Replace `dev` with correct version once available

    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("org.xerial:sqlite-jdbc:3.43.0.0")
    implementation("com.mojang:authlib:1.5.25")

    implementation("org.reflections:reflections:0.10.2")
    implementation("com.jeff-media:custom-block-data:2.2.4")
    implementation("com.jeff-media:MorePersistentDataTypes:2.4.0")



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
