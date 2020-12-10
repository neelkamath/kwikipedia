# Contributing

## Forking

If you're forking the repository to develop the project as your own and not just to send back a PR, follow these steps:

1. Set up a [Bintray](https://bintray.com/) repo to publish the package.
1. Create
   a [secret](https://docs.github.com/en/free-pro-team@latest/actions/reference/encrypted-secrets#creating-encrypted-secrets-for-a-repository)
   named `BINTRAY_KEY` whose value is your Bintray API key.

## Installation

1. Install [Java JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
1. Clone the repository using one of the following methods.
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

Bump the `version` in [`build.gradle.kts`](../build.gradle.kts), add a [Changelog entry](CHANGELOG.md), and commit to
the `master` branch. If the tests pass in the CI/CD pipeline, the new package will be uploaded to Bintray, a new GitHub
release will be created, and the new documentation will be hosted.
