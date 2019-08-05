# KWikipedia

Minimalist Kotlin Wikipedia wrapper (pronounced "Quickipedia")

## Contributing

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

- Windows: `gradle.bat bintrayUpload`
- Other: `./gradlew bintrayUpload`

### Forking the Repository

1. Create a [GitLab](https://gitlab.com/users/sign_in#register-pane) account.
1. [Connect](https://docs.gitlab.com/ee/ci/ci_cd_for_external_repos/github_integration.html) the GitHub repo to a GitLab repo for CI/CD.
1. Create the following environment variables [via the UI](https://docs.gitlab.com/ee/ci/variables/#via-the-ui).
    - `BINTRAY_USER`: Bintray username 
    - `BINTRAY_KEY`: Bintray API Key (available by clicking `API Key` on your [profile](https://bintray.com/profile/edit))

## License

This project is under the [MIT License](LICENSE).
