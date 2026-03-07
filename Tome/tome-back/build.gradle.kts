import org.gradle.api.tasks.testing.Test

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("plugin.jpa") version "1.9.25"
    id("org.jlleitschuh.gradle.ktlint") version "13.0.0"
    jacoco
}

group = "Tome"
version = "0.0.1-SNAPSHOT"
description = "Tome Project"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.okta.spring:okta-spring-boot-starter:3.0.5")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    // CSV reader
    implementation("org.apache.commons:commons-csv:1.11.0")

    // Dependencias para Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    // Para Testcontainers con JUnit 5
    testImplementation("org.testcontainers:junit-jupiter")

    // Elige el contenedor de la base de datos que usas en producción (ej. PostgreSQL)
    testImplementation("org.testcontainers:postgresql")

    // Para usar @WithMockUser y probar la seguridad de Spring
    testImplementation("org.springframework.security:spring-security-test")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

ktlint {
    ignoreFailures.set(false)
    verbose.set(true)
}

tasks.register<Copy>("installGitHooks") {
    from(rootProject.file("hooks"))
    into(rootProject.file(".git/hooks"))
    fileMode = "775".toInt(8)
}

tasks.named("assemble") {
    dependsOn("installGitHooks")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco/html"))
        xml.outputLocation.set(layout.buildDirectory.file("reports/jacoco/report.xml"))
    }
}
// Temporaly disabled
// tasks.jacocoTestCoverageVerification {
//    violationRules {
//        rule {
//            limit {
//                minimum = "0.8".toBigDecimal()
//            }
//        }
//    }
// }
