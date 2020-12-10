package com.neelkamath.kwikipedia

import kotlinx.coroutines.runBlocking
import kotlin.test.*

class SearchTest {
    @Test
    fun `Search results for "appl" should include "Apple Inc"`(): Unit =
        assertTrue("Apple Inc." in search("appl").map(SearchResult::title))

    @Test
    fun `Searching for seven random pages should return at most seven pages`(): Unit =
        assertTrue(runBlocking { search(7) }.size <= 7)

    @Test
    fun `Search results shouldn't include reference pages by default`(): Unit =
        assertTrue(search("Go").none(SearchResult::isReferencePage))

    @Test
    fun `Searching for "Go" should return reference pages when told to`(): Unit =
        assertTrue(search("Go", allowReferences = true).any(SearchResult::isReferencePage))
}

class MostViewedPages {
    @Test
    fun `Querying for the five most viewed pages should give at most five pages`(): Unit =
        assertTrue(runBlocking { searchMostViewed(5) }.size <= 5)
}

class TitleSearcherTest {
    @Test
    fun `The correct search result should be gotten for an exact title`(): Unit =
        assertEquals("https://en.wikipedia.org/wiki/Apple", searchTitle("Apple").url)

    @Test
    fun `An error should be thrown if the title doesn't exactly match the title in the search result`() {
        assertFailsWith<IllegalArgumentException> { searchTitle("Appl") }
    }
}

class PageTest {
    private val page = getPage("Apple Inc.")
    private val content = page.values.joinToString(" ")

    @Test
    fun `Headings should not contain separators`(): Unit = assertTrue(
        page.keys.none { it.contains(separator) },
        "Headings containing separators: ${page.keys.filter { it.contains(separator) }}"
    )

    @Test
    fun `Sections should not contain separators`(): Unit = assertFalse(
        content.contains(separator), "Content including separator ($separator): $content"
    )

    @Test
    fun `The page's contents shouldn't include the headings`(): Unit = assertFalse(
        content.contains(section),
        "Headings included in the page's contents: ${section.findAll(content).toList().map { it.value }}"
    )

    @Test
    fun `The page should be as long as the original`() {
        val originalLength = 100_000
        val acceptableDifference = 25_000
        val range = originalLength - acceptableDifference..originalLength + acceptableDifference
        assertTrue(
            content.length in range,
            """
            |The page should have had $range characters, but had ${content.length} characters instead. We use a range 
            |because the Wikipedia page's contents are prone to change.
            """.trimMargin()
        )
    }
}
