# KWikipedia

[![Release](https://jitpack.io/v/neelkamath/kwikipedia.svg)](https://jitpack.io/#neelkamath/kwikipedia)

This is a minimal Kotlin Wikipedia library (pronounced "Quickipedia") which deals with the inconsistencies in
Wikipedia's API by performing tasks such as data sanitization for you. It runs on JVM 8 (e.g., Java, Kotlin).

## Installation

1. Add the JitPack maven repository (`https://jitpack.io`) to the list of repositories.
1. Add the dependency information:
    - Group: `com.neelkamath`
    - Artifact: `kwikipedia`
    - Version: 0.9.0

Here's an example for Gradle using its Kotlin DSL:

```kotlin
import java.net.URI

repositories {
    maven { url = URI.create("https://jitpack.io") }
}

dependencies {
    implementation("com.neelkamath:kwikipedia:0.9.0")
}
```

## Usage

- View the latest docs [here](https://jitpack.io/com/github/neelkamath/kwikipedia/latest/javadoc).
- To view a previous version's docs, open `https://jitpack.io/com/github/neelkamath/kwikipedia/<VERSION>/javadoc` after replacing `<VERSION>` with the version (e.g., `0.9.0`).
- [Changelog](CHANGELOG.md)

## [Contributing](CONTRIBUTING.md)

## License

This project is under the [MIT License](LICENSE).
