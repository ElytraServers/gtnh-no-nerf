plugins {
	java
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.jetbrains:annotations:24.0.0")
}

tasks.test {
	useJUnitPlatform()
}
