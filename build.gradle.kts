plugins {
    kotlin("jvm") version "1.9.22"
    id("cl.franciscosolis.sonatype-central-upload") version "1.0.0"
    id("com.google.devtools.ksp").version("1.9.22-1.0.17")
}

group = "io.github.suitetecsa.sdk"
version = "0.1.4-alpha01"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.mockito:mockito-core:3.12.4")
    testImplementation("junit:junit:4.13.1")
    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    // Adaptador de Coroutines para Retrofit
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    // Adaptador RxJava para Retrofit
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")

    implementation("com.auth0:java-jwt:3.18.2")

    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")
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

    archives = files(
        "build/libs/nauta-connect-${version}.jar",
        "build/libs/nauta-connect-${version}-javadoc.jar",
        "build/libs/nauta-connect-${version}-sources.jar"
    )
    pom = file("./pom_gradle.txt")

    signingKey = System.getenv("SIGNING_KEY")
    signingKeyPassphrase = System.getenv("SIGNING_KEY_PASSPHRASE")
}

tasks.sonatypeCentralUpload {
    dependsOn("build")
}
