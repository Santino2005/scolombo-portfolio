package tome.seeders.csv.books

import jakarta.persistence.EntityManager
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import tome.books.Book
import tome.books.BookRepository
import tome.books.author.Author
import tome.books.author.AuthorRepository
import tome.books.languageApi.Language
import tome.books.languageApi.LanguageRepository
import tome.books.publisherApi.Publisher
import tome.books.publisherApi.PublisherRepository
import tome.books.tag.Tag
import tome.books.tag.TagRepository
import tome.seeders.DatabaseSeeder
import tome.seeders.SeedingOrders
import tome.seeders.csv.CsvReader
import tome.seeders.csv.CsvSeederProperties
import tome.seeders.csv.cleaner.CsvCleaner
import tome.seeders.csv.convertRowToAuthors
import tome.seeders.csv.convertRowToBook
import tome.seeders.csv.convertRowToLanguages
import tome.seeders.csv.convertRowToPublisher
import tome.seeders.csv.convertRowToTags
import tome.seeders.filter.SmutFilter

@Component
@Profile("prod", "dev")
class BookCsvSeeder(
    private val booksRepository: BookRepository,
    private val languageRepository: LanguageRepository,
    private val entityManager: EntityManager,
    private val publishersRepository: PublisherRepository,
    private val authorRepository: AuthorRepository,
    private val tagRepository: TagRepository,
    private val csvReader: CsvReader,
    private val props: CsvSeederProperties,
    private val smutFilter: SmutFilter,
    private val csvCleaner: CsvCleaner,
    private val languagesCache: MutableMap<String, Language> = mutableMapOf(),
    private val authorsCache: MutableMap<String, Author> = mutableMapOf(),
    private val tagsCache: MutableMap<String, Tag> = mutableMapOf(),
    private val publishersCache: MutableMap<String, Publisher> = mutableMapOf(),
) : DatabaseSeeder {
    fun getLanguageByName(name: String): Language {
        val key = name.lowercase()
        languagesCache[key]?.let { return it }

        val existing = languageRepository.findByName(name)
        if (existing != null) {
            languagesCache[existing.name.lowercase()] = existing
            return existing
        }

        val newLang = languageRepository.save(Language(id = null, name = name))
        languagesCache[newLang.name.lowercase()] = newLang
        return newLang
    }

    fun getPublisherByName(name: String): Publisher {
        val key = name.lowercase()
        publishersCache[key]?.let { return it }

        val existing = publishersRepository.findByName(name)
        if (existing != null) {
            publishersCache[existing.name.lowercase()] = existing
            return existing
        }

        val newPublisher = publishersRepository.save(Publisher(id = null, name = name))
        publishersCache[newPublisher.name.lowercase()] = newPublisher
        return newPublisher
    }

    fun getTagByName(name: String): Tag {
        val key = name.lowercase()
        tagsCache[key]?.let { return it }

        val existing = tagRepository.findByName(name)
        if (existing != null) {
            tagsCache[existing.name.lowercase()] = existing
            return existing
        }

        val newTag = tagRepository.save(Tag(id = null, name = name))
        tagsCache[newTag.name.lowercase()] = newTag
        return newTag
    }

    fun getAuthorByFullName(fullName: String): Author {
        val key = fullName.lowercase()
        authorsCache[key]?.let { return it }

        val existing = authorRepository.findByFullName(fullName)
        if (existing != null) {
            authorsCache[existing.fullName.lowercase()] = existing
            return existing
        }

        val newAuthor = authorRepository.save(Author(id = null, fullName = fullName, name = "", surname = ""))
        authorsCache[newAuthor.fullName.lowercase()] = newAuthor
        return newAuthor
    }

    @Transactional
    override fun run(vararg args: String?) {
        val logger = LoggerFactory.getLogger(BookCsvSeeder::class.java)
        val rows = csvReader.read(props.csv)
        val malformedRows: MutableList<Int> = mutableListOf()
        if (booksRepository.count() > 0) {
            return
        }
        try {
            logger.info("Beginning to seed books from CSV")
            rows.forEachIndexed { index, row ->
                val language = convertRowToLanguages(row)
                val persistedLanguage = getLanguageByName(language.name)
                val publisher = convertRowToPublisher(row)
                val persistedPublisher = getPublisherByName(publisher.name)
                val authors = convertRowToAuthors(row)
                val tags = convertRowToTags(row)

                val persistedTags = mutableListOf<Tag>()
                var hasBannedTag = false
                for (tag in tags) {
                    if (!smutFilter.isValid(tag.name)) {
                        logger.debug("Skipped row due to banned tag: ${tag.name}")
                        hasBannedTag = true
                        malformedRows.add(index)
                        break
                    }
                    persistedTags.add(getTagByName(tag.name))
                }
                if (hasBannedTag) return@forEachIndexed
                val persistedAuthors =
                    authors.map { author ->
                        getAuthorByFullName("${author.fullName}")
                    }
                val book = convertRowToBook(row, persistedLanguage, persistedPublisher, persistedTags, persistedAuthors)
                if (book == null) {
                    malformedRows.add(index)
                    return@forEachIndexed
                }
                var persistentBook = booksRepository.findBookByIsbn(book.isbn)
                if (persistentBook != null) {
                    logger.debug("Tried to seed existing book: ${persistentBook.title}")
                    malformedRows.add(index)
                } else {
                    if (!smutFilter.isValid(book.title)) {
                        malformedRows.add(index)
                        return@forEachIndexed
                    }
                    booksRepository.save(
                        Book(
                            id = null,
                            title = book.title,
                            isbn = book.isbn,
                            releasedDate = book.releasedDate,
                            pages = book.pages,
                            coverUrl = book.coverUrl,
                            synopsis = book.synopsis,
                            language = book.language,
                            publisher = book.publisher,
                            tags = book.tags,
                            authors = book.authors,
                        ),
                    )
                    val seededAmount = booksRepository.count()
                    if (seededAmount % 1000 == 0L) {
                        booksRepository.flush()
                        entityManager.clear()
                        logger.info("Flushed and cleared session at $seededAmount books")
                    }
                }
            }
        } finally {
            logger.info("Succesfully seeded ${booksRepository.count()}")
            csvCleaner.deleteRow(props.csv, malformedRows)
            logger.info("Cleaned ${malformedRows.size} malformed rows")
        }
    }

    override fun getOrder(): Int = SeedingOrders.First.ordinal
}
