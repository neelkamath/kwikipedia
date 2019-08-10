package com.neelkamath.kwikipedia.test

import com.neelkamath.kwikipedia.getPage
import com.neelkamath.kwikipedia.search
import com.neelkamath.kwikipedia.section
import com.neelkamath.kwikipedia.sectionSeparator
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
    @Test
    fun `Headings should not contain separators`() = runBlocking {
        val headings = getPage("Apple Inc.").keys
        assertTrue(
            headings.none { it.contains(sectionSeparator) },
            "Headings containing separators: ${headings.filter { it.contains(sectionSeparator) }}"
        )
    }

    @Test
    fun `The page's contents shouldn't include the headings`() = runBlocking {
        val page = getPage("Apple Inc.").values.joinToString()
        assertFalse(
            page.contains(section),
            "Headings included in the page's contents: ${section.findAll(page).toList().map { it.value }}"
        )
    }

    @Test
    fun `The page should be as long as the original`() = runBlocking {
        val pageLength = getPage("Apple Inc.").values.joinToString().length
        val length = 106_718
        val difference = 10_000
        val range = length - difference..length + difference
        assertTrue(
            pageLength in range,
            """
            The page should have had $range characters, but had $pageLength characters instead. We use a range because 
            the Wikipedia page's contents are prone to change.
            """.replace(Regex("""\s+"""), " ")
        )
    }
}