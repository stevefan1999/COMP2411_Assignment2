import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


val compileKotlin: KotlinCompile by tasks

compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks

compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    sourceCompatibility = JavaVersion.VERSION_1_8.toString()
    targetCompatibility = JavaVersion.VERSION_1_8.toString()
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

plugins {
    application
    id("org.springframework.boot") version "2.2.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"

    kotlin("jvm") version "1.3.50"
    kotlin("plugin.spring") version "1.3.50"
    kotlin("plugin.jpa") version "1.3.50"
    kotlin("plugin.noarg") version "1.3.50"
}

springBoot {
    mainClassName = "hk.edu.polyu.comp2411.assignment.AssignmentApplicationKt"
}

tasks.bootJar {
    mainClassName = "hk.edu.polyu.comp2411.assignment.AssignmentApplicationKt"
    manifest {
        attributes("Start-Class" to "hk.edu.polyu.comp2411.assignment.AssignmentApplicationKt")
    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    listOf(
        "com.github.bkenn:kfoenix:0.1.3",
        "com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.8",
        "com.github.thomasnield:rxkotlinfx:2.2.2",
        "com.hendraanggrian.ktfx:ktfx:8.6.4",
        "com.jfoenix:jfoenix:8.0.8",
        "de.jensd:fontawesomefx-commons:8.14",
        "de.jensd:fontawesomefx-materialdesignfont:1.7.22-2",
        "io.reactivex.rxjava2:rxkotlin:2.4.0",
        "no.tornado:tornadofx:1.7.17",
        "no.tornado:tornadofx-controlsfx:0.1",
        "org.flywaydb:flyway-core:6.0.8",
        "org.jetbrains.kotlin:kotlin-reflect",
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8",
        "org.springframework.boot:spring-boot-starter-data-jpa",
        "org.springframework.boot:spring-boot-starter-security",
        files("lib/ojdbc7.jar")
    ).forEach(::implementation)

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}