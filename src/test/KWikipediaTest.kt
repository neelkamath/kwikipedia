package com.neelkamath.kwikipedia.test

import com.neelkamath.kwikipedia.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SearchTest {
    @Test
    fun `Search results for "appl" should include "Apple Inc"`() = runBlocking {
        val results = search("appl")
        assertTrue("Apple Inc." in results.map { it.title }, "Results were instead: $results")
    }

    @Test
    fun `Searching for seven random pages should return seven pages`() = runBlocking<Unit> {
        search(7).size.also { assertTrue(it == 7, "There were $it results instead") }
    }
}

class UrlGetterTest {
    @Test
    fun `The correct URL should be gotten for an exact title`() = runBlocking {
        assertEquals(getUrl("Apple"), "https://en.wikipedia.org/wiki/Apple")
    }
}

class PageTest {
    private val page = runBlocking { getPage("Apple Inc.") }
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
        val acceptableDifference = 10_000
        val range = originalLength - acceptableDifference..originalLength + acceptableDifference
        assertTrue(
            content.length in range,
            """
            The page should have had $range characters, but had ${content.length} characters instead. We use a range 
            because the Wikipedia page's contents are prone to change.
            """.replace(Regex("""\s+"""), " ")
        )
    }
}