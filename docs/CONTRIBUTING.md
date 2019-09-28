# Contributing

If you are forking the repository to develop the project as your own and not just to send back a PR, follow [these steps](fork.md).

## Installation

1. Install a [Java JDK or JRE](http://www.oracle.com/technetwork/java/javase/downloads/index.html) version 8 or higher. 
1. Clone the repository using one of the following methods.
    - SSH: `git clone git@github.com:neelkamath/kwikipedia.git`
    - HTTPS: `git clone https://github.com/neelkamath/kwikipedia.git`
    
## Developing

Substitute `<GRADLE>` with `gradlew.bat` on Windows and `./gradlew` on others.

### Testing

`<GRADLE> test`

### Documentation

`<GRADLE> dokka`

Open `build/dokka/kwikipedia/index.html` in your browser.

### New Releases

Bump the `version` in `build.gradle.kts`, add a [Changelog entry](CHANGELOG.md), and commit to the `master` branch. CI/CD is setup so that if the tests pass, the new package will be uploaded to Bintray, a new GitHub release will be created, and the new documentation will be hosted.