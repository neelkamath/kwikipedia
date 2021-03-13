# Contributing

## Installation

1. Install [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
1. Clone the repository using one of the following methods:
    - SSH: `git clone git@github.com:neelkamath/kwikipedia.git`
    - HTTPS: `git clone https://github.com/neelkamath/kwikipedia.git`

## Developing

Replace `<GRADLE>` with `gradlew.bat` on Windows, and `./gradlew` on others.

### Testing

`<GRADLE> test`

### Documentation

`<GRADLE> dokkaHtml`

Open `build/dokka/html/kwikipedia/index.html` in your browser.

## Releasing

1. Bump the `version` in [`build.gradle.kts`](../build.gradle.kts).
1. Add a [Changelog entry](CHANGELOG.md).
1. Commit to the `master` branch. If the tests pass in the CI/CD pipeline, the new package will be uploaded to JitPack, a new GitHub release will be created, and the new documentation will be hosted.
