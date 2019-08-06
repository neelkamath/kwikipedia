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

internal val wikiSection = Regex("""\s*=+ [\w\s]+ =+\s*""")

/** Returns the content of the Wikipedia page having [title]. You can [search] for the exact [title]. */
suspend fun getPage(title: String): String {
    val data = HttpClient { install(JsonFeature) }.use {
        it.get<JsonObject>(
            URI(
                "https", "en.wikipedia.org", "/w/api.php",
                "action=query&titles=$title&prop=extracts&format=json&explaintext=", null
            ).toString()
        )
    }
    val pages = data["query"].asJsonObject["pages"].asJsonObject
    return wikiSection.replace(pages[pages.keySet().first()].asJsonObject["extract"].asString, "")
}