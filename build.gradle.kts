import com.jfrog.bintray.gradle.BintrayExtension

plugins {
    kotlin("jvm") version "1.3.41"
    id("org.jetbrains.dokka") version "0.9.18"
    id("com.jfrog.bintray") version "1.8.4"
}

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

if (gradle.startParameter.taskNames.contains("bintrayUpload")) {
    bintray {
        user = property("BINTRAY_USER") as String
        key = property("BINTRAY_KEY") as String
        setConfigurations("archives")
        publish = true
        pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
            repo = "KWikipedia"
            name = "KWikipedia"
            desc = "Minimalist Kotlin Wikipedia wrapper"
            websiteUrl = "https://github.com/neelkamath/kwikipedia"
            issueTrackerUrl = "https://github.com/neelkamath/kwikipedia/issues"
            vcsUrl = "https://github.com/neelkamath/kwikipedia.git"
            setLicenses("MIT")
            setLabels("Wikipedia", "wrapper")
            githubRepo = "neelkamath/kwikipedia"
            githubReleaseNotesFile = "README.md"
            setVersion("0.1.0")
        })
    }
}