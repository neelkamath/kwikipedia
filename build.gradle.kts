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
version = "0.3.1"

repositories { jcenter() }

dependencies {
    val ktorVersion = "1.2.3"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-apache:$ktorVersion")
    implementation("io.ktor:ktor-client-json:$ktorVersion")
    implementation("io.ktor:ktor-client-gson:$ktorVersion")
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

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
        body("[Changelog](CHANGELOG.md)")
        overwrite(true)
    }
}