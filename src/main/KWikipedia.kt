package com.neelkamath.kwikipedia

import com.neelkamath.kwikipedia.Wikipedia.getArticle
import com.neelkamath.kwikipedia.Wikipedia.queryMostViewed
import com.neelkamath.kwikipedia.Wikipedia.queryRandom
import com.neelkamath.kwikipedia.Wikipedia.search
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

/**
 * A Wikipedia page's contents.
 *
 * Headings (e.g., `"Bibliography"`) are mapped to their respective contents. Since the page's introduction doesn't have
 * a heading, the page's title (e.g., `"Apple Inc."`) is used instead.
 */
typealias Page = Map<String, String>

/** If a page's description ends with this [String], it is a reference page (a page containing only redirects). */
internal const val referenceDescription = " may refer to:"
/** Wikipedia returns content with sections separated with this pattern. */
internal val section = Regex("""\s*==+ [\w\s]+ ==+\s*""")
/** Wikipedia section titles are surrounded with this pattern. */
internal val separator = Regex("""\s*==+\s*""")

/**
 * Search result for a page.
 *
 * An example of a [title] is `"Apple Inc."`. [description]'s are around 2 sentences (300 characters) each. The [url]
 * links to the page.
 */
data class SearchResult(val title: String, val description: String, val url: String) {
    val isReferencePage = description.endsWith(referenceDescription)
}

/**
 * [query]s Wikipedia, returning reference pages only if you [allowReferences].
 *
 * This function can also be used to suggest pages. For example, if [query] is `"appl"`, pages about the fruit (apple),
 * the tech company (Apple Inc.), etc. will be returned.
 */
fun search(query: String, allowReferences: Boolean = false): List<SearchResult> = search(query).let { results ->
    if (allowReferences) results else results.filterNot { it.isReferencePage }
}

/**
 * Search for no more than [limit] (at most 500) random pages and returns reference pages if you [allowReferences].
 *
 * Results are [limit]ed to `2` by default in case you [allowReferences] and the first result is a reference page.
 */
suspend fun search(limit: Int = 2, allowReferences: Boolean = false): List<SearchResult> = coroutineScope {
    queryRandom(limit)
        .map {
            async { searchTitle(it.title) }
        }
        .map { it.await() }
        .let { results ->
            if (allowReferences) results else results.filterNot { it.isReferencePage }
        }
}

/**
 * Searches for the most popular pages in the last day.
 *
 * Since only content pages are retrieved, you may get fewer than the [limit] of pages. At most `500` articles will be
 * returned even if the [limit] is greater.
 */
suspend fun searchMostViewed(limit: Int = 10): List<SearchResult> = coroutineScope {
    queryMostViewed(limit)
        .filter { it.namespace == 0 && it.title != "Main Page" }
        .map {
            async { searchTitle(it.title) }
        }
        .map { it.await() }
}

/** Get a [SearchResult] for [title] if you know [title] is the exact name of a page. */
fun searchTitle(title: String): SearchResult = search(title)[0].also {
    if (it.title != title) throw Error("<title> ($title) didn't match the search result (${it.title})")
}

/** Returns the Wikipedia page for the specified [title]. You can [search] for the exact [title]. */
fun getPage(title: String): Page {
    val page = getArticle(title).replace(Regex("""(\n){2,}"""), "\n").replace("  ", " ")
    val headings = listOf(title) + section.findAll(page).toList().map { it.value.replace(separator, "") }
    val sections = page.split(section).map { it.replace(separator, "") }
    return headings.zip(sections).toMap().filterValues { it.isNotEmpty() }
}

/** Returns a random Wikipedia page (if you allow [allowReferences], this might be a reference page). */
suspend fun getPage(allowReferences: Boolean = false): Page =
    getPage(search(allowReferences = allowReferences)[0].title)