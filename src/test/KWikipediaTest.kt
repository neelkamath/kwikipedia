package com.neelkamath.kwikipedia.test

import com.neelkamath.kwikipedia.*
import io.kotlintest.inspectors.forAll
import io.kotlintest.inspectors.forAtLeastOne
import io.kotlintest.inspectors.forNone
import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.matchers.collections.shouldHaveAtMostSize
import io.kotlintest.matchers.numerics.shouldBeInRange
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.string.shouldNotContain
import io.kotlintest.matchers.withClue
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.runBlocking

class SearchTest : StringSpec({
    """Search results for "appl" should include "Apple Inc."""" {
        search("appl").map { it.title } shouldContain "Apple Inc."
    }

    "Searching for seven random pages should return at most seven pages" { search(7) shouldHaveAtMostSize 7 }

    "Search results shouldn't include reference pages by default" {
        search("Go").forAll { !it.isReferencePage }
    }

    """Searching for "Go" should return reference pages when told to""" {
        search("Go", allowReferences = true).forAtLeastOne { it.isReferencePage }
    }
})

class MostViewedPagesTest : StringSpec({
    "Querying for the five most viewed pages should give at most five pages" {
        searchMostViewed(5) shouldHaveAtMostSize 5
    }
})

class TitleSearcherTest : StringSpec({
    "The correct search result should be gotten for an exact title" {
        searchTitle("Apple").url shouldBe "https://en.wikipedia.org/wiki/Apple"
    }

    "An error should be thrown if the title doesn't exactly match the title in the search result" {
        shouldThrow<Throwable> { searchTitle("Appl") }
    }
})

class PageTest : StringSpec({
    val page = runBlocking { getPage("Apple Inc.") }
    val content = page.values.joinToString(" ")

    "Headings should not contain separators" {
        page.keys.forNone { it shouldContain separator }
    }

    "Sections should not contain separators" { content shouldNotContain separator }

    "The page's contents shouldn't include the headings" { content shouldNotContain section }

    "The page should be as long as the original" {
        val originalLength = 106_718
        val acceptableDifference = 10_000
        val range = originalLength - acceptableDifference..originalLength + acceptableDifference
        withClue(
            """
            |The page should have had $range characters, but had ${content.length} characters instead. We use a range 
            |because the Wikipedia page's contents are prone to change.
            """.trimMargin()
        ) { content.length shouldBeInRange range }
    }
})