package tome.seeders.csv

import tome.books.author.Author
import tome.books.languageApi.Language
import tome.books.publisherApi.Publisher
import tome.books.tag.Tag
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

data class RowLanguageDTO(
    val name: String,
)

fun convertRowToLanguages(row: Map<String, String>): RowLanguageDTO {
    val languageName = row["language"] ?: "English"
    if (languageName.isEmpty()) {
        return RowLanguageDTO("English")
    }
    return RowLanguageDTO(name = languageName)
}

data class RowPublisherDTO(
    val name: String,
)

fun convertRowToPublisher(row: Map<String, String>): RowPublisherDTO {
    val publishersName = row["publisher"] ?: ""
    return RowPublisherDTO(name = publishersName.trim())
}

data class RowAuthorDTO(
    val fullName: String,
)

fun convertRowToAuthors(row: Map<String, String>): List<RowAuthorDTO> {
    val authors = ArrayList<RowAuthorDTO>()
    val authorsCell = row["author"]?.trim()
    val authorsNames = authorsCell?.split(',')
    authorsNames?.forEach { authorName ->
        authors.add(RowAuthorDTO(fullName = authorName.trim()))
    }
    return authors
}

data class RowTagDTO(
    val name: String,
)

fun convertRowToTags(row: Map<String, String>): List<RowTagDTO> {
    val tags = mutableListOf<RowTagDTO>()

    val genreAndVotes = row["genre_and_votes"] ?: return tags

    val rawTags =
        genreAndVotes
            .split(',')
            .map { it.trim() } // clean up spaces

    for (rawTag in rawTags) {
        val parts = rawTag.split(' ')
        if (parts.size <= 1) continue
        val name = parts.dropLast(1).joinToString(" ")
        tags.add(RowTagDTO(name = name))
    }

    return tags
}

fun cleanDateString(date: String): String = date.replace(Regex("(\\d+)(st|nd|rd|th)"), "$1")

fun parseDate(dateStr: String): LocalDate? {
    val cleaned = cleanDateString(dateStr).trim()

    val patterns =
        listOf(
            "MMMM d yyyy",
            "MMMM yyyy",
            "yyyy",
        )

    for (pattern in patterns) {
        try {
            val formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)
            return LocalDate.parse(cleaned, formatter)
        } catch (ignored: DateTimeParseException) {
        }
    }
    return null
}

data class RowBookDTO(
    val title: String,
    val isbn: String,
    val releasedDate: LocalDate,
    val pages: Int,
    val coverUrl: String,
    val synopsis: String,
    val language: Language,
    val publisher: Publisher,
    val tags: List<Tag>,
    val authors: List<Author>,
)

fun convertRowToBook(
    row: Map<String, String>,
    language: Language,
    publisher: Publisher,
    tags: List<Tag>,
    authors: List<Author>,
): RowBookDTO? {
    val releaseDate: LocalDate? = parseDate(row["date_published"] ?: "")
    if (releaseDate == null) {
        return null
    }
    return RowBookDTO(
        title = row["title"]?.trim() ?: "",
        isbn = row["isbn13"]?.trim() ?: "",
        releasedDate = releaseDate,
        pages = row["number_of_pages"]?.takeIf { it.isNotBlank() }?.toInt() ?: 0,
        coverUrl = row["cover_link"]?.trim() ?: "",
        synopsis = row["description"]?.trim() ?: "",
        language = language,
        publisher = publisher,
        tags = tags,
        authors = authors,
    )
}
