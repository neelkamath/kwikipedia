package com.neelkamath.kwikipedia

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.net.URI

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

/** Page's [title]. */
private data class RandomPage(val title: String)

/** Pages present in the search results for random pages. */
private data class RandomResults(val random: List<RandomPage>)

/** Search results for random pages. */
private data class RandomSearch(val query: RandomResults)

private data class MostViewedSearch(val query: MostViewedData)

private data class MostViewedData(val mostviewed: List<PopularPage>)

private data class PopularPage(val ns: Int, val title: String)

/**
 * Searches for [query] and returns reference pages only if you [allowReferences].
 *
 * This function can also be used to suggest pages. For example, if [query] is `appl`, pages about the fruit, tech
 * company, etc. will be returned.
 */
suspend fun search(query: String, allowReferences: Boolean = false): List<SearchResult> = with(query(query)) {
    (0 until get(1).asJsonArray.size())
        .map {
            SearchResult(
                title = get(1).asJsonArray[it].asString,
                description = get(2).asJsonArray[it].asString,
                url = get(3).asJsonArray[it].asString
            )
        }
        .let { results ->
            if (allowReferences) results else results.filterNot { it.isReferencePage }
        }
}

private suspend fun query(query: String) = getQuery<JsonArray>("action=opensearch&search=$query")

/**
 * Search for no more than [limit] (at most 500) random pages and returns reference pages if you [allowReferences].
 *
 * Results are [limit]ed to `2` by default in case you [allowReferences] and the first result is a reference page.
 */
suspend fun search(limit: Int = 2, allowReferences: Boolean = false): List<SearchResult> {
    if (limit > 500) throw Error("<limit> cannot be greater than 500")
    return coroutineScope {
        getQuery<RandomSearch>("action=query&format=json&list=random&rnnamespace=0&rnlimit=$limit")
            .query
            .random
            .map {
                async { searchTitle(it.title) }
            }
            .map { it.await() }
            .let { results ->
                if (allowReferences) results else results.filterNot { it.isReferencePage }
            }
    }
}

/**
 * Searches for the most popular pages in the last day ([limit]ed to a maximum of `500`).
 *
 * Since only content pages are retrieved, you may get fewer than the [limit] of pages.
 */
suspend fun searchMostViewed(limit: Int = 10): List<SearchResult> {
    if (limit > 500) throw Error("<limit> cannot be greater than 500")
    return coroutineScope {
        getQuery<MostViewedSearch>("action=query&list=mostviewed&pvimlimit=$limit&format=json")
            .query
            .mostviewed
            .filter { it.ns == 0 && it.title != "Main Page" }
            .map {
                async { searchTitle(it.title) }
            }
            .map { it.await() }
    }
}

/** Get a [SearchResult] for [title] if you know [title] is the exact name of a page. */
suspend fun searchTitle(title: String): SearchResult = with(query(title).asJsonArray) {
    val resultTitle = get(1).asJsonArray[0].asString
    if (resultTitle != title) throw Error("<title> ($title) didn't match the search result ($resultTitle)")
    SearchResult(resultTitle, description = get(2).asJsonArray[0].asString, url = get(3).asJsonArray[0].asString)
}

/** Returns the Wikipedia page for the specified [title]. You can [search] for the exact [title]. */
suspend fun getPage(title: String): Page {
    val pages = getQuery<JsonObject>("action=query&titles=$title&prop=extracts&format=json&explaintext=")["query"]
        .asJsonObject["pages"]
        .asJsonObject
    val page = pages[pages.keySet().first()]
        .asJsonObject["extract"]
        .asString
        .replace(Regex("""(\n){2,}"""), "\n")
        .replace("  ", " ")
    val headings = listOf(title) + section.findAll(page).toList().map { it.value.replace(separator, "") }
    val sections = page.split(section).map { it.replace(separator, "") }
    return headings.zip(sections).toMap().filterValues { it.isNotEmpty() }
}

/** Returns a random Wikipedia page (if you allow [allowReferences], this might be a reference page). */
suspend fun getPage(allowReferences: Boolean = false): Page =
    getPage(search(allowReferences = allowReferences)[0].title)

/**
 * Performs an HTTP GET request with a [query] to the Wikipedia API to return a [T].
 *
 * The [query]'s values needn't be URI encoded (e.g., `"action=opensearch&search=Apple Inc."`).
 */
private suspend inline fun <reified T> getQuery(query: String) = HttpClient { install(JsonFeature) }.use {
    it.get<T>(URI("https", "en.wikipedia.org", "/w/api.php", query, null).toString())
}