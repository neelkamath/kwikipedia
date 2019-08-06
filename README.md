# KWikipedia

[![Download](https://api.bintray.com/packages/neelkamath/kwikipedia/kwikipedia/images/download.svg)](https://bintray.com/neelkamath/kwikipedia/kwikipedia/_latestVersion)

Minimalist Kotlin Wikipedia wrapper (pronounced "Quickipedia")

## Contributing

Every commit to the `master` branch in which the CI passes will cause the package to be uploaded to Bintray.

### Installation

1. Install a version of Kotlin not less than 1.3, and less than 2 from [here](https://kotlinlang.org/docs/tutorials/command-line.html).
1. Clone the repository using one of the following methods.
    - SSH: `git clone git@github.com:neelkamath/kwikipedia.git`
    - HTTPS: `git clone https://github.com/neelkamath/kwikipedia.git`

### Testing

- Windows: `gradle.bat test`
- Other: `./gradlew test`

### Documentation

- Windows: `gradle.bat dokka`
- Other: `./gradlew dokka`

Open `build/javadoc/kwikipedia/index.html` in your browser.

### Upload

`<GRADLE> bintrayUpload -PBINTRAY_USER=<USER> -PBINTRAY_KEY=<KEY>`, where `<GRADLE>` is `gradle.bat` on Windows and `./gradlew` on others, `<USER>` is your Bintray username, and `<KEY>` is your Bintray API key (available by clicking `API Key` on your [profile](https://bintray.com/profile/edit))

### Forking the Repository

1. Create a [GitLab](https://gitlab.com/users/sign_in#register-pane) account.
1. [Connect](https://docs.gitlab.com/ee/ci/ci_cd_for_external_repos/github_integration.html) the GitHub repo to a GitLab repo for CI/CD.
1. Create the following environment variables [via the UI](https://docs.gitlab.com/ee/ci/variables/#via-the-ui).
    - `BINTRAY_USER`: Bintray username 
    - `BINTRAY_KEY`: Bintray API Key (available by clicking `API Key` on your [profile](https://bintray.com/profile/edit))
1. Create an account on [Bintray](https://bintray.com/).
1. Sign in to your Bintray account.
1. Click `Add New Repository` and following the steps displayed.
1. Click `Add New Package` on the page you were taken to and follow the steps displayed.
1. Upload the package (this can be done manually, or by pushing to the branch `master`).
1. On your package's page, click `Add to JCenter`, and then click `Send`. After it has been approved (this may take a few hours), you will see JCenter in the `Linked to` section of your package's page.

## License

This project is under the [MIT License](LICENSE).
