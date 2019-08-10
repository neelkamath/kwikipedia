package com.neelkamath.kwikipedia.test

import com.neelkamath.kwikipedia.getPage
import com.neelkamath.kwikipedia.search
import com.neelkamath.kwikipedia.section
import com.neelkamath.kwikipedia.separator
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SearchTest {
    @Test
    fun `Search results for "appl" should include "Apple Inc"`() = runBlocking {
        val results = search("appl")
        assertTrue("Apple Inc." in results, "Results were $results instead")
    }
}

class PageTest {
    private val page = runBlocking { getPage("Apple Inc.") }
    private val content = page.values.joinToString()

    @Test
    fun `Content shouldn't include newline characters`() = assertFalse(
        content.contains("\n"), "Content containing \\n: $content"
    )

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