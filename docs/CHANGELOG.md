# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.9.0](https://github.com/neelkamath/kwikipedia/releases/tag/v0.9.0)

### Changed

- Improved docs.

## [0.8.0](https://github.com/neelkamath/kwikipedia/releases/tag/v0.8.0)

### Changed

- Specify root package as `com.neelkamath.kwikipedia`.
- Throw a `kotlin.IllegalArgumentException` instead of a `kotlin.Error` in `searchTitle()`.
- Rename `getPage()` to `getRandomPage()`.

## [0.7.2](https://github.com/neelkamath/kwikipedia/releases/tag/v0.7.2)

### Fixed

- Document that Wikipedia doesn't return search results during random periods of time.

## [0.7.1](https://github.com/neelkamath/kwikipedia/releases/tag/v0.7.1)

### Fixed

- Fix `searchMostViewed()` randomly returning zero results by drastically increasing the default limit.

## [0.7.0](https://github.com/neelkamath/kwikipedia/releases/tag/v0.7.0)

### Changed

- Don't throw an `Error` in `search()` and `searchMostViewed()` when searching for more than 500 articles.

## [0.6.1](https://github.com/neelkamath/kwikipedia/releases/tag/v0.6.1)

### Changed

- Speed up `search()` and `searchMostViewed()` by a magnitude.

## [0.6.0](https://github.com/neelkamath/kwikipedia/releases/tag/v0.6.0)

### Added

- Search for the most viewed pages in the last day using `searchMostViewed()`.

### Changed

- Replace `getUrl()` with `searchTitle()` to give back more data.

## [0.5.0](https://github.com/neelkamath/kwikipedia/releases/tag/v0.5.0)

### Changed

- Make `isReferencePage()` a method of `SearchResult`.

## [0.4.1](https://github.com/neelkamath/kwikipedia/releases/tag/v0.4.1)

### Added

- Check whether the search result is a reference page using `isReferencePage()`.

## [0.4.0](https://github.com/neelkamath/kwikipedia/releases/tag/v0.4.0)

### Changed

- Allow discarding reference pages in `search()` and `getPage()`.
- Limit search results to two instead of one in `search()` so that if the first result turns out to be a reference page, zero topical pages won't be returned.
- Throw an `Error` in `search()` if a `limit` greater than `500` is passed.

## [0.3.1](https://github.com/neelkamath/kwikipedia/releases/tag/v0.3.1)

### Changed

- Use new lines (i.e., `\n`) instead of spaces to separate content such as lists in the data returned by `getPage()`.

## [0.3.0](https://github.com/neelkamath/kwikipedia/releases/tag/v0.3.0)

### Added

- Retrieving a page's URL by using `getUrl()`.

### Changed

- Update `search()` to return page URLs as well.

## [0.2.2](https://github.com/neelkamath/kwikipedia/releases/tag/v0.2.2)

### Changed

- Provide overloads for `search()` and `getPage()` to retrieve random pages.

## [0.2.1](https://github.com/neelkamath/kwikipedia/releases/tag/v0.2.1)

### Removed

- Remove the following from `getPage()`'s output:
    - Newlines (`\n`)
    - Content separators (`==`, `===`, etc.)

## [0.2.0](https://github.com/neelkamath/kwikipedia/releases/tag/v0.2.0)

### Changed

- Return `getPage()` the page in sections instead of the entire page in a single `String`.

## [0.1.3](https://github.com/neelkamath/kwikipedia/releases/tag/v0.1.3)

### Changed

- Updated documentation.

## [0.1.2](https://github.com/neelkamath/kwikipedia/releases/tag/v0.1.2)

### Fixed

- Fix bugs.

## [0.1.1](https://github.com/neelkamath/kwikipedia/releases/tag/v0.1.1)

### Fixed

- Fix bugs.

## [0.1.0](https://github.com/neelkamath/kwikipedia/releases/tag/v0.1.0)

### Added

- Add first version.
