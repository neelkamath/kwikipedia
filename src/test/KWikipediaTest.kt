package com.neelkamath.kwikipedia.test

import com.neelkamath.kwikipedia.*
import kotlinx.coroutines.runBlocking
import kotlin.test.*

internal class SearchTest {
    @Test
    fun `Search results for "appl" should include "Apple Inc"`() =
        assertTrue("Apple Inc." in search("appl").map(SearchResult::title))

    @Test
    fun `Searching for seven random pages should return at most seven pages`() {
        assertTrue(runBlocking { search(7) }.size <= 7)
    }

    @Test
    fun `Search results shouldn't include reference pages by default`() =
        assertTrue(search("Go").none(SearchResult::isReferencePage))

    @Test
    fun `Searching for "Go" should return reference pages when told to`() {
        assertTrue(search("Go", allowReferences = true).any(SearchResult::isReferencePage))
    }
}

internal class MostViewedPages {
    @Test
    fun `Querying for the five most viewed pages should give at most five pages`() =
        assertTrue(runBlocking { searchMostViewed(5) }.size <= 5)
}

internal class TitleSearcherTest {
    @Test
    fun `The correct search result should be gotten for an exact title`() =
        assertEquals("https://en.wikipedia.org/wiki/Apple", searchTitle("Apple").url)

    @Test
    fun `An error should be thrown if the title doesn't exactly match the title in the search result`() {
        assertFailsWith<Throwable> { searchTitle("Appl") }
    }
}

internal class PageTest {
    private val page = getPage("Apple Inc.")
    private val content = page.values.joinToString(" ")

    @Test
    fun `Headings should not contain separators`() = assertTrue(
        page.keys.none { it.contains(separator) },
        "Headings containing separators: ${page.keys.filter { it.contains(separator) }}"
    )

    @Test
    fun `Sections should not contain separators`() = assertFalse(
        content.contains(separator), "Content including separator ($separator): $content"
    )

    @Test
    fun `The page's contents shouldn't include the headings`() = assertFalse(
        content.contains(section),
        "Headings included in the page's contents: ${section.findAll(content).toList().map { it.value }}"
    )

    @Test
    fun `The page should be as long as the original`() {
        val originalLength = 106_718
        val acceptableDifference = 20_000
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
