# KWikipedia

[![Download](https://api.bintray.com/packages/neelkamath/kwikipedia/kwikipedia/images/download.svg)](https://bintray.com/neelkamath/kwikipedia/kwikipedia/_latestVersion)

This is a minimalist Kotlin Wikipedia wrapper (pronounced "Quickipedia"). You can view the changelog [here](CHANGELOG.md).

## Installation

See `Maven build settings` on [Bintray](https://bintray.com/neelkamath/kwikipedia/kwikipedia)

## Usage

See the [documentation](https://neelkamath.gitlab.io/kwikipedia/).

## Contributing

### New Releases

Bump the `version` in `build.gradle.kts`, add a [Changelog entry](CHANGELOG.md), and commit to the `master` branch. CI/CD is setup so that if the tests pass, the new package will be uploaded to Bintray, a new GitHub release will be created, and the new documentation will be hosted.

### Installation

1. Install a version of Kotlin not less than 1.3, and less than 2 from [here](https://kotlinlang.org/docs/tutorials/command-line.html).
1. Clone the repository using one of the following methods.
    - SSH: `git clone git@github.com:neelkamath/kwikipedia.git`
    - HTTPS: `git clone https://github.com/neelkamath/kwikipedia.git`

### Testing

- Windows: `gradle.bat test`
- Other: `./gradlew test`

### Documentation

#### Regular

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

### Forking the Repository

1. Create a [GitLab](https://gitlab.com/users/sign_in#register-pane) account.
1. [Connect](https://docs.gitlab.com/ee/ci/ci_cd_for_external_repos/github_integration.html) the GitHub repo to a GitLab repo for CI/CD.
1. Create the following environment variables [via the UI](https://docs.gitlab.com/ee/ci/variables/#via-the-ui).
    - `BINTRAY_USER`: Bintray username 
    - `BINTRAY_KEY`: Bintray API Key (available by clicking `API Key` on your [profile](https://bintray.com/profile/edit))
    - `GITHUB_TOKEN`: This is obtained by going to [settings](https://github.com/settings/tokens), clicking `Generate new token`, entering a `Note`, selecting the `repo` scope, and clicking `Generate token`.
1. Create an account on [Bintray](https://bintray.com/).
1. Sign in to your Bintray account.
1. Click `Add New Repository` and following the steps displayed.
1. Click `Add New Package` on the page you were taken to and follow the steps displayed.
1. Upload the package (this can be done manually, or by pushing to the branch `master`).
1. On your package's page, click `Add to JCenter`, and then click `Send`. After it has been approved (this may take a few hours), you will see `jcenter` in the `Linked to` section of your package's page.

## License

This project is under the [MIT License](LICENSE).
