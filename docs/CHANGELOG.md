# v0.4.0

- `search()` and `getPage()` let you discard reference pages.
- `search()` limits results to `2` instead of `1` by default so that if the first result turns out to be a reference page, you won't get zero topical pages.
- `search()` throws an `Error` if you pass a `limit` greater than `500`.

# v0.3.1

- The contents returned by `getPage()` and its overloads now use new lines (i.e., `\n`) instead of spaces to separate content such as lists.

# v0.3.0

- New API for retrieving a page's URL: `getUrl()`
- `search()` functions have been updated to return page URLs as well.

# v0.2.2

- `search()` and `getPage()` now have overloads for random pages.

# v0.2.1

- `getPage()`'s output has had the following things removed.
    - Newlines (`\n`)
    - Content separators (`==`, `===`, etc.)

# v0.2.0

- `getPage()` now returns the page in sections instead of the entire page in a single `String`.

# v0.1.3

- Updated documentation.

# v0.1.2

- No API changes

# v0.1.1

- No API changes

# v0.1.0

- First release