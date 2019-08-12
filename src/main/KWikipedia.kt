package com.neelkamath.kwikipedia

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import java.net.URI

/**
 * Search results for pages.
 *
 * Page titles (e.g., `"Apple Inc."`) are mapped to short descriptions (around 300 characters or 2 sentences each) about
 * the page.
 */
typealias SearchResults = Map<String, String>

/**
 * Searches Wikipedia for [query].
 *
 * This function can also be used to suggest Wikipedia pages. For example, if [query] is `appl`, pages about the fruit,
 * tech company, etc. will be returned.
 */
suspend fun search(query: String): SearchResults {
    val results = get<JsonArray>("action=opensearch&search=$query")
    val titles = results[1].asJsonArray
    val descriptions = results[2].asJsonArray
    val map = mutableMapOf<String, String>()
    for (index in 0 until titles.size()) map[titles[index].asString] = descriptions[index].asString
    return map
}

/** A page's [title]. */
private data class RandomPage(val title: String)

/** Pages present in the search results for random pages. */
private data class RandomResults(val random: List<RandomPage>)

/** Search results for random pages. */
private data class RandomSearch(val query: RandomResults)

/** Search for [limit] (at most 500) random Wikipedia pages. */
suspend fun search(limit: Int = 1): SearchResults {
    val searchResults = get<RandomSearch>("action=query&format=json&list=random&rnnamespace=0&rnlimit=$limit")
    val searches = searchResults.query.random.map {
        val results = search(it.title)
        it.title to results.getValue(results.keys.first())
    }
    return searches.toMap()
}

/** Wikipedia returns content with sections separated with this pattern. */
internal val section = Regex("""\s*==+ [\w\s]+ ==+\s*""")

/** Wikipedia section titles are surrounded with this pattern. */
internal val separator = Regex("""\s*==+\s*""")

/**
 * A Wikipedia page's contents.
 *
 * Headings (e.g., `"Bibliography"`) are mapped to their respective contents. Since the page's introduction doesn't have
 * a heading, the page's title (e.g., `"Apple Inc."`) is used instead.
 */
typealias Page = Map<String, String>

/** Returns the Wikipedia page for the specified [title]. You can [search] for the exact [title]. */
suspend fun getPage(title: String): Page {
    val data = get<JsonObject>("action=query&titles=$title&prop=extracts&format=json&explaintext=")
    val pages = data["query"].asJsonObject["pages"].asJsonObject
    val extract = pages[pages.keySet().first()].asJsonObject["extract"].asString
    val page = extract.replace(Regex("""(\n)+"""), " ").replace("  ", " ")
    val headings = listOf(title) + section.findAll(page).toList().map { it.value.replace(separator, "") }
    val sections = page.split(section).map { it.replace(separator, "") }
    return headings.zip(sections).toMap().filterValues { it.isNotEmpty() }
}

/** Returns a random Wikipedia page. */
suspend fun getPage(): Page = getPage(search().keys.first())

/**
 * Performs an HTTP GET request with a [query] to the Wikipedia API to return a [T].
 *
 * [query]'s values needn't be URI encoded (e.g., `"action=opensearch&search=Apple Inc."`).
 */
private suspend inline fun <reified T> get(query: String) = HttpClient { install(JsonFeature) }.use {
    it.get<T>(URI("https", "en.wikipedia.org", "/w/api.php", query, null).toString())
}