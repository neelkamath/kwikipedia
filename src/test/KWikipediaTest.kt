package com.neelkamath.kwikipedia.test

import com.neelkamath.kwikipedia.*
import io.kotlintest.inspectors.forAll
import io.kotlintest.inspectors.forAtLeastOne
import io.kotlintest.inspectors.forNone
import io.kotlintest.matchers.collections.shouldContain
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.matchers.numerics.shouldBeInRange
import io.kotlintest.matchers.string.shouldContain
import io.kotlintest.matchers.string.shouldEndWith
import io.kotlintest.matchers.string.shouldNotContain
import io.kotlintest.matchers.string.shouldNotEndWith
import io.kotlintest.matchers.withClue
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.runBlocking

class SearchTest : StringSpec({
    """Search results for "appl" should include "Apple Inc."""" {
        search("appl").map { it.title } shouldContain "Apple Inc."
    }

    "Searching for seven random pages should return seven pages" { search(7) shouldHaveSize 7 }

    "Search results shouldn't include reference pages by default" {
        search("Go").forAll { it.description shouldNotEndWith referenceDescription }
    }

    """Searching for "Go" should return reference pages when told to""" {
        search("Go", allowReferences = true).forAtLeastOne { it.description shouldEndWith referenceDescription }
    }
})

class UrlGetterTest : StringSpec({
    "The correct URL should be gotten for an exact title" {
        getUrl("Apple") shouldBe "https://en.wikipedia.org/wiki/Apple"
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