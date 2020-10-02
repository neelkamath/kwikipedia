import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    `maven-publish`
    kotlin("jvm") version "1.4.10"
    id("org.jetbrains.dokka") version "1.4.10"
    id("com.jfrog.bintray") version "1.8.5"
    id("com.github.breadmoirai.github-release") version "2.2.12"
}

group = "com.neelkamath.kwikipedia"
version = "0.7.2"

repositories.jcenter()

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    val retrofitVersion = "2.9.0"
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation(kotlin("test-junit5"))
}

tasks.test.get().useJUnitPlatform()

kotlin {
    explicitApi()

    sourceSets {
        main.get().kotlin.srcDirs("src/main")
        test.get().kotlin.srcDirs("src/test")
    }
}

val publication = "default"

if (gradle.startParameter.taskNames.contains("bintrayUpload"))
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

tasks.withType<DokkaTask> {
    dokkaSourceSets.configureEach { includes.setFrom(includes + listOf("src/main/resources/doc.md")) }
}

val dokkaJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc)
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val dokkaGitLabPages by tasks.creating(org.jetbrains.dokka.gradle.DokkaTask::class) {
    outputDirectory.set(file("public"))
}

publishing.publications.create<MavenPublication>(publication) {
    from(components["java"])
    artifact(dokkaJar)
    artifact(sourcesJar)
}

if (gradle.startParameter.taskNames.contains("githubRelease"))
    githubRelease {
        token(property("GITHUB_TOKEN") as String)
        owner("neelkamath")
        repo("kwikipedia")
        body("[Changelog](docs/CHANGELOG.md)")
        overwrite(true)
    }
