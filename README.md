# KWikipedia

[![Download](https://api.bintray.com/packages/neelkamath/kwikipedia/kwikipedia/images/download.svg)](https://bintray.com/neelkamath/kwikipedia/kwikipedia/_latestVersion)

This is a minimalist Kotlin Wikipedia library (pronounced "Quickipedia") which deals with the inconsistencies in Wikipedia's API by performing tasks such as data sanitization for you.

There are [GitHub releases](https://github.com/neelkamath/kwikipedia/releases) and a [changelog](CHANGELOG.md).

If you are forking the repository to develop the project as your own and not just to send back a PR, follow [these steps](fork.md).

## Installation

See `Maven build settings` on [Bintray](https://bintray.com/neelkamath/kwikipedia/kwikipedia)

## Usage

See the [documentation](https://neelkamath.gitlab.io/kwikipedia/).

## Contributing

When updating the version of Kotlin used, update the version of the Kotlin plugin used in `build.gradle.kts`, the Docker image used in `.gitlab-ci.yml`, and the README's [Installation section](#installation).

### New Releases

Bump the `version` in `build.gradle.kts`, add a [Changelog entry](CHANGELOG.md), and commit to the `master` branch. CI/CD is setup so that if the tests pass, the new package will be uploaded to Bintray, a new GitHub release will be created, and the new documentation will be hosted.

### Installation

1. Install a version of Kotlin not less than 1.3.50, and less than 2 from [here](https://kotlinlang.org/docs/tutorials/command-line.html).
1. Clone the repository using one of the following methods.
    - SSH: `git clone git@github.com:neelkamath/kwikipedia.git`
    - HTTPS: `git clone https://github.com/neelkamath/kwikipedia.git`

### Testing

- Windows: `gradle.bat test`
- Other: `./gradlew test`

### Documentation

#### HTML

- Windows: `gradle.bat dokka`
- Other: `./gradlew dokka`

Open `build/dokka/kwikipedia/index.html` in your browser.

#### JAR

`<GRADLE> dokkaJar`, where `<GRADLE>` is `gradle.bat` on Windows and `./gradlew` on others

#### For GitLab Pages

`<GRADLE> dokkaGitLabPages`, where `<GRADLE>` is `gradle.bat` on Windows, and `./gradlew` on others

This will output the documentation files to the `public/` directory, since GitLab deploys `public/` as a static website.

### Sources JAR

JCenter requires a JAR containing the source code to be uploaded along with the package. This JAR can be created with `<GRADLE> sourcesJar`, where `<GRADLE>` is `gradle.bat` on Windows and `./gradlew` on others.

### Bintray Releases

`<GRADLE> bintrayUpload -PBINTRAY_USER=<USER> -PBINTRAY_KEY=<KEY>`, where `<GRADLE>` is `gradle.bat` on Windows and `./gradlew` on others, `<USER>` is your Bintray username, and `<KEY>` is your Bintray API key (available by clicking `API Key` on your [profile](https://bintray.com/profile/edit))

### GitHub Releases

`<GRADLE> githubRelease -PGITHUB_TOKEN=<TOKEN>`, where `<GRADLE>` is `gradle.bat` on Windows and `./gradlew` on others, and `<TOKEN>` is your GitHub token (obtained by going to [settings](https://github.com/settings/tokens), clicking `Generate new token`, entering a `Note`, selecting the `repo` scope, and clicking `Generate token`)

## License

This project is under the [MIT License](LICENSE).
