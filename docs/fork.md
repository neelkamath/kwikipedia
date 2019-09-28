1. Create an account on [Bintray](https://bintray.com/).
1. Sign in to your Bintray account.
1. Click `Add New Repository` and following the steps displayed.
1. Click `Add New Package` on the page you were taken to and follow the steps displayed.
1. Create a [GitLab](https://gitlab.com/users/sign_in#register-pane) account.
1. [Connect](https://docs.gitlab.com/ee/ci/ci_cd_for_external_repos/github_integration.html) the GitHub repo to a GitLab repo for CI/CD.
1. Create the following environment variables [via the UI](https://docs.gitlab.com/ee/ci/variables/#via-the-ui).
    - `BINTRAY_USER`: Bintray username 
    - `BINTRAY_KEY`: Bintray API Key (available by clicking `API Key` on your [profile](https://bintray.com/profile/edit))
    - `GITHUB_TOKEN`: This is obtained by going to [settings](https://github.com/settings/tokens), clicking `Generate new token`, entering a `Note`, selecting the `repo` scope, and clicking `Generate token`.
1. Upload the package (this can be done manually, or by pushing to the branch `master`).
1. On your package's page, click `Add to JCenter`, and then click `Send`. After it has been approved (this may take a few hours), you will see `jcenter` in the `Linked to` section of your package's page.