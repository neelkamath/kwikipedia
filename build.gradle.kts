import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    `maven-publish`
    kotlin("jvm") version "1.4.31"
    id("org.jetbrains.dokka") version "1.4.20"
}

group = "com.neelkamath.kwikipedia"
version = "0.9.0"

kotlin.explicitApi()

repositories.jcenter()

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.31") // Needed by Jackson.
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.1")
    testImplementation(kotlin("test-junit5"))

    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:converter-jackson:$retrofitVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        systemProperty("junit.jupiter.testinstance.lifecycle.default", "per_class")
    }
    withType<DokkaTask> {
        dokkaSourceSets.configureEach { includes.setFrom(includes + "src/main/resources/doc.md") }
    }
    register("printVersion") { println(project.version) }
    val jvmTarget = "1.8"
    compileKotlin { kotlinOptions.jvmTarget = jvmTarget }
    compileTestKotlin { kotlinOptions.jvmTarget = jvmTarget }
}

val dokkaJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing.publications.create<MavenPublication>("default") {
    artifactId = "kwikipedia"
    from(components["java"])
    artifact(dokkaJar)
    artifact(sourcesJar)
}
