package com.neelkamath.kwikipedia.test

import com.neelkamath.kwikipedia.getPage
import com.neelkamath.kwikipedia.search
import com.neelkamath.kwikipedia.wikiSection
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
    fun `Wiki section code should not be present`() = runBlocking {
        val page = getPage("Apple Inc.")
        assertFalse(
            page.contains(wikiSection),
            "The page contained the following Wiki section code: ${wikiSection.findAll(page).toList().map { it.value }}"
        )
    }

    @Test
    fun `The page should be as long as the original`() = runBlocking {
        val pageLength = getPage("Apple Inc.").length
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