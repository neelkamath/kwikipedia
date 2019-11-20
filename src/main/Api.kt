package com.neelkamath.kwikipedia

import com.google.gson.JsonArray
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/** Wrapper for Wikipedia's HTTP API. */
internal object Wikipedia {
    private val service = Retrofit.Builder()
        .baseUrl("https://en.wikipedia.org/w/api.php/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WikipediaService::class.java)

    /** Returns the [title]d article's content. */
    fun getArticle(title: String): String =
        service.getArticle(title).execute().body()!!.query.pages.values.first().extract

    /** For example, [query]ing `"appl"` would return results for articles such as Apple Inc. */
    fun search(query: String): List<SearchResult> = with(service.search(query).execute().body()!!) {
        (0 until get(1).asJsonArray.size()).map {
            SearchResult(
                title = get(1).asJsonArray.get(it).asString,
                description = get(2).asJsonArray.get(it).asString,
                url = get(3).asJsonArray.get(it).asString
            )
        }
    }

    /**
     * At most [limit] of the most viewed articles will be returned.
     *
     * Even if the [limit] is greater than `500`, at most `500` articles will be returned.
     */
    fun queryMostViewed(limit: Int = 10): List<NamespacedArticle> =
        service.queryMostViewed(limit).execute().body()!!.query.mostViewed

    /**
     * Returns at most [limit] random articles.
     *
     * Even if the [limit] is greater than `500`, at most `500` articles will be returned.
     */
    fun queryRandom(limit: Int = 2): List<NamespacedArticle> =
        service.queryRandom(limit).execute().body()!!.query.random
}

private interface WikipediaService {
    @GET("?action=query&prop=extracts&format=json&explaintext=")
    fun getArticle(@Query("titles") title: String): Call<ArticleResponse>

    @GET("?action=opensearch")
    fun search(@Query("search") search: String): Call<JsonArray>

    @GET("?action=query&list=mostviewed&format=json")
    fun queryMostViewed(@Query("pvimlimit") limit: Int): Call<MostViewedQueryResponse>

    @GET("?action=query&format=json&list=random&rnnamespace=0")
    fun queryRandom(@Query("rnlimit") limit: Int): Call<RandomQueryResult>
}

private data class ArticleResponse(val query: Pages)

/** The [pages] [Map]'s only key is the page number of the Wikipedia article (e.g., `"856"`). */
private data class Pages(val pages: Map<String, Article>)

private data class Article(val extract: String)

private data class MostViewedQueryResponse(val query: MostViewedData)

private data class MostViewedData(@SerializedName("mostviewed") val mostViewed: List<NamespacedArticle>)

/** The [namespace] the [title]d article belongs to. */
internal data class NamespacedArticle(@SerializedName("ns") val namespace: Int, val title: String)

private data class RandomQueryResult(val query: RandomQueryArticles)

private data class RandomQueryArticles(val random: List<NamespacedArticle>)