plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    maven {
        isAllowInsecureProtocol = true
        url = uri("http://localhost:8081/repository/maven-public/")
    }
    mavenCentral()
    google()
    maven {
        url = uri("https://dlcdn.apache.org/")
    }
}

dependencies {
    annotationProcessor("com.google.dagger:dagger-android-processor:2.45")
    annotationProcessor("com.google.dagger:dagger-compiler:2.45")

    implementation("com.google.dagger:dagger:2.45")

    // Jackson JSON library for JSON persistence
    implementation("com.fasterxml.jackson.core:jackson-core:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.15.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("ru.yanchenko.vlad.graphapp.GraphApp")
}

tasks.test {
    useJUnitPlatform()
}