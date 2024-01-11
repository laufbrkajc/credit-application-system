import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
	kotlin("jvm") version "1.9.20"
	kotlin("plugin.spring") version "1.9.20"
	kotlin("plugin.jpa") version "1.9.20"
}

group = "org.nothing"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.2.1")
	implementation("org.springframework.boot:spring-boot-starter-hateoas:3.0.7")
	implementation("org.springframework.boot:spring-boot-starter-validation:3.0.4")
	implementation("org.springframework.boot:spring-boot-starter-web:3.1.0")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
	implementation("org.flywaydb:flyway-core:9.16.0")
	implementation("org.flywaydb:flyway-mysql:9.16.0")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.4")
	runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.1.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0")
	testImplementation("io.mockk:mockk:1.13.4")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
