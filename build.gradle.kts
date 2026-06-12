import org.gradle.kotlin.dsl.intellijPlatform
import org.jetbrains.intellij.platform.gradle.*

plugins {
    id("org.jetbrains.intellij.platform") version "2.2.1"
    kotlin("jvm") version "2.0.0"
    id("io.qameta.allure") version "2.11.2"
}

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaUltimate("2024.3")
        testFramework(TestFrameworkType.Starter)
    }
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.kodein.di:kodein-di-jvm:7.20.2")
    testImplementation("io.qameta.allure:allure-junit5:2.27.0")
    testImplementation("org.aspectj:aspectjweaver:1.9.20")
}

allure {
    adapter {
        autoconfigure.set(true)
        aspectjWeaver.set(true)
    }
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}