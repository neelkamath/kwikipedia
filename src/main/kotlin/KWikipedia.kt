package com.neelkamath.kwikipedia

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/**
 * A Wikipedia page's contents.
 *
 * Headings (e.g., `"Bibliography"`) are mapped to their respective contents. Since the page's introduction doesn't have
 * a heading, the page's title (e.g., `"Apple Inc."`) is used instead.
 */
public typealias Page = Map<String, String>

/** If a page's description ends with this [String], it is a reference page (a page containing only redirects). */
internal const val referenceDescription = " may refer to:"

/** Wikipedia returns content with sections separated with this pattern. */
internal val section = Regex("""\s*==+ [\w\s]+ ==+\s*""")

/** Wikipedia section titles are surrounded with this pattern. */
internal val separator = Regex("""\s*==+\s*""")

/**
 * An example of a [title] is `"Apple Inc."`. [description]'s are around 2 sentences (300 characters) each. The [url]
 * links to the page.
 */
public data class SearchResult(val title: String, val description: String, val url: String) {
    val isReferencePage: Boolean = description.endsWith(referenceDescription)
}

/**
 * [query]s Wikipedia, returning reference pages only if you [allowReferences].
 *
 * This function can also be used to suggest pages. For example, if [query] is `"appl"`, pages about the fruit (apple),
 * the tech company (Apple Inc.), etc. will be returned.
 */
public fun search(query: String, allowReferences: Boolean = false): List<SearchResult> =
    Wikipedia.search(query).let { results ->
        if (allowReferences) results else results.filterNot { it.isReferencePage }
    }

/**
 * Search for no more than [limit] (at most 500) random pages and returns reference pages if you [allowReferences].
 *
 * Results are [limit]ed to `2` by default in case you [allowReferences] and the first result is a reference page.
 */
public suspend fun search(limit: Int = 2, allowReferences: Boolean = false): List<SearchResult> = coroutineScope {
    Wikipedia
        .queryRandom(limit)
        .map {
            async { searchTitle(it.title) }
        }
        .awaitAll()
        .let { if (allowReferences) it else it.filterNot(SearchResult::isReferencePage) }
}

/**
 * Searches for the most popular pages in the last day.
 *
 * Since only content pages are retrieved, you may get significantly fewer than the [limit] of pages. At most `500`
 * articles will be returned even if the [limit] is greater.
 *
 * It has been found that Wikipedia returns zero search results for random periods of time. Only trending topic searches
 * (i.e., this function) are affected. The other search functions always function normally. We cannot recursively call
 * the function until it returns results, because this outage lasts for hours at a time.
 */
public suspend fun searchMostViewed(limit: Int = 25): List<SearchResult> = coroutineScope {
    Wikipedia
        .queryMostViewed(limit)
        .filter { it.namespace == 0 && it.title != "Main Page" }
        .map {
            async { searchTitle(it.title) }
        }
        .awaitAll()
}

/**
 * Get a [SearchResult] for [title] if you know [title] is the exact name of a page.
 *
 * Throws an [IllegalArgumentException] if the [title] doesn't match the exact name of a page.
 */
public fun searchTitle(title: String): SearchResult = search(title)[0].also {
    if (it.title != title)
        throw IllegalArgumentException("The title ($title) didn't match the search result (${it.title}).")
}

/** Returns the Wikipedia page for the specified [title]. You can [search] for the exact [title]. */
public fun getPage(title: String): Page {
    val page = Wikipedia.getArticle(title).replace(Regex("""(\n){2,}"""), "\n").replace("  ", " ")
    val headings = listOf(title) + section.findAll(page).map { it.value.replace(separator, "") }
    val sections = page.split(section).map { it.replace(separator, "") }
    return headings.zip(sections).toMap().filterValues { it.isNotEmpty() }
}

/** Returns a random Wikipedia page (if you allow [allowReferences], this might be a reference page). */
public suspend fun getRandomPage(allowReferences: Boolean = false): Page =
    getPage(search(allowReferences = allowReferences)[0].title)
