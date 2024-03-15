import cl.franciscosolis.sonatypecentralupload.SonatypeCentralUploadTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22"
    `maven-publish`
    id("cl.franciscosolis.sonatype-central-upload") version "1.0.2"
    id("com.google.devtools.ksp").version("1.9.22-1.0.17")
}

group = "io.github.suitetecsa.sdk"
version = "0.1.4-alpha02"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.mockito:mockito-core:3.12.4")
    testImplementation("junit:junit:4.13.2")
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
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
kotlin {
    jvmToolchain(17)
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
            pom {
                name.set(project.name)
                description.set("A tool designed to interact with ETECSA services.")
                url.set("http://github.com/suitetecsa/nauta-connect")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://github.com/suitetecsa/nauta-connect/blob/master/LICENSE")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("lesclaz")
                        name.set("Lesly Cintra")
                        email.set("lesclaz95@gmail.com")
                    }
                }
                scm {
                    url.set("http://github.com/suitetecsa/nauta-connect/tree/master")
                    connection.set("scm:git:git://github.com/suitetecsa/nauta-connect.git")
                    developerConnection.set("scm:git:ssh://github.com/suitetecsa/nauta-connect.git")
                }
            }
        }
    }
}

tasks.named<SonatypeCentralUploadTask>("sonatypeCentralUpload") {
    dependsOn("jar", "sourcesJar", "javadocJar", "generatePomFileForMavenPublication")

    username = System.getenv("SONATYPE_USERNAME")
    password = System.getenv("SONATYPE_PASSWORD")

    archives = files(
        tasks.named("jar"),
        tasks.named("sourcesJar"),
        tasks.named("javadocJar"),
    )
    pom = file(
        tasks.named("generatePomFileForMavenPublication").get().outputs.files.single()
    )

    signingKey = System.getenv("SIGNING_KEY")
    signingKeyPassphrase = System.getenv("SIGNING_KEY_PASSPHRASE")
}
