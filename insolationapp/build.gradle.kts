plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
    id("jacoco")
}

group = "org.fintech2024"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.4.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.4.0")

    implementation("com.vladmihalcea:hibernate-types-60:2.21.1")


    implementation("org.liquibase:liquibase-core:4.29.2")
    implementation("org.postgresql:postgresql:42.7.4")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.8")

    implementation("org.springframework.boot:spring-boot-starter-data-redis:3.4.0")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.3.5")

    implementation("org.springframework.boot:spring-boot-starter-security:3.3.5")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")

    testImplementation("org.testcontainers:junit-jupiter:1.20.2")
    testImplementation("org.mockito:mockito-core:5.14.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it).exclude(
                "**/model/**",
            )
        })
    )
}
