package com.neelkamath.kwikipedia

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import java.net.URI

/**
 * Returns a Wikipedia search for [query].
 *
 * A [Map] is returned. Each keys is a page title (e.g., "Apple Inc."). Each value is a short descriptions (around 300
 * characters, or 2 sentences) about the page. This function can also be used to suggest Wikipedia entries. For example,
 * if [query] is `appl`, pages about the fruit, company, etc. will be returned.
 */
suspend fun search(query: String): Map<String, String> {
    val results = HttpClient { install(JsonFeature) }.use {
        it.get<JsonArray>(
            URI("https", "en.wikipedia.org", "/w/api.php", "action=opensearch&search=$query", null).toString()
        )
    }
    val titles = results[1].asJsonArray
    val descriptions = results[2].asJsonArray
    val map = mutableMapOf<String, String>()
    for (index in 0 until titles.size()) map[titles[index].asString] = descriptions[index].asString
    return map
}

internal val section = Regex("""\s*==+ [\w\s]+ ==+\s*""")
internal val separator = Regex("""\s*==+\s*""")

/**
 * Returns the content of the Wikipedia page having [title]. You can [search] for the exact [title].
 *
 * The returned [Map]'s keys are headings (e.g., "Bibliography"), and the values are their respective contents. Since
 * the page's introduction doesn't have a heading, [title] is used as the key.
 */
suspend fun getPage(title: String): Map<String, String> {
    val data = HttpClient { install(JsonFeature) }.use {
        it.get<JsonObject>(
            URI(
                "https", "en.wikipedia.org", "/w/api.php",
                "action=query&titles=$title&prop=extracts&format=json&explaintext=", null
            ).toString()
        )
    }
    val pages = data["query"].asJsonObject["pages"].asJsonObject
    val extract = pages[pages.keySet().first()].asJsonObject["extract"].asString
    val page = extract.replace(Regex("""(\n)+"""), " ").replace("  ", " ")
    val headings = listOf(title) + section.findAll(page).toList().map { it.value.replace(separator, "") }
    val sections = page.split(section).map { it.replace(separator, "") }
    return headings.zip(sections).toMap().filterValues { it.isNotEmpty() }
}