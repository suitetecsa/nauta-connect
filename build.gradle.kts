plugins {
    kotlin("jvm") version "1.9.22"
    id("cl.franciscosolis.sonatype-central-upload") version "1.0.0"
}

group = "io.github.suitetecsa.sdk"
version = "0.1.3"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.mockito:mockito-core:3.12.4")
    testImplementation("junit:junit:4.13.1")
    implementation("org.jsoup:jsoup:1.17.2")
}

tasks.test {
    useJUnitPlatform()
}
java {
    withSourcesJar()
    withJavadocJar()
}
kotlin {
    jvmToolchain(17)
}

sonatypeCentralUpload {
    username = System.getenv("SONATYPE_USERNAME")
    password = System.getenv("SONATYPE_PASSWORD")

    archives = files("build/libs/nauta-connect-${version}.jar", "build/libs/nauta-connect-${version}-javadoc.jar", "build/libs/nauta-connect-${version}-sources.jar")
    pom = file("./pom_gradle.txt")

    signingKey = System.getenv("SIGNING_KEY")
    signingKeyPassphrase = System.getenv("SIGNING_KEY_PASSPHRASE")
}
