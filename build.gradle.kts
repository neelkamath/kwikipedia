import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm") version "1.3.50"
    id("org.jetbrains.dokka") version "0.9.18"
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
    id("com.github.breadmoirai.github-release") version "2.2.9"
}

group = "com.neelkamath.kwikipedia"
version = "0.7.1"

repositories { jcenter() }

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.2")
    val retrofitVersion = "2.6.2"
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
}

val test by tasks.getting(Test::class) { useJUnitPlatform { } }

kotlin.sourceSets {
    getByName("main").kotlin.srcDirs("src/main")
    getByName("test").kotlin.srcDirs("src/test")
}

val publication = "default"

if (gradle.startParameter.taskNames.contains("bintrayUpload")) {
    bintray {
        user = property("BINTRAY_USER") as String
        key = property("BINTRAY_KEY") as String
        setPublications(publication)
        publish = true
        override = true
        pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
            repo = "kwikipedia"
            name = "kwikipedia"
            vcsUrl = "https://github.com/neelkamath/kwikipedia.git"
            setLicenses("MIT")
            setVersion(project.version)
        })
    }
}

tasks.dokka { includes = listOf("src/main/resources/doc.md") }

val dokkaJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokka)
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val dokkaGitLabPages by tasks.creating(org.jetbrains.dokka.gradle.DokkaTask::class) { outputDirectory = "public" }

publishing {
    publications {
        create<MavenPublication>(publication) {
            from(components["java"])
            artifact(dokkaJar)
            artifact(sourcesJar)
        }
    }
}

if (gradle.startParameter.taskNames.contains("githubRelease")) {
    githubRelease {
        token(property("GITHUB_TOKEN") as String)
        owner("neelkamath")
        repo("kwikipedia")
        body("[Changelog](docs/CHANGELOG.md)")
        overwrite(true)
    }
}