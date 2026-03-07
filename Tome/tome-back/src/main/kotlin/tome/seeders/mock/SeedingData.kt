package tome.seeders.mock

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
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
import java.time.LocalDate

@Component
@Profile("test")
class SeedingData(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository,
    private val tagRepository: TagRepository,
    private val publisherRepository: PublisherRepository,
    private val languageRepository: LanguageRepository,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        seedAuthor()
        seedPublisher()
        seedTags()
        seedLanguage()
        seedBooks()
    }

    private val english = Language(null, "English")
    private val french = Language(null, "French")
    private val spanish = Language(null, "Spanish")
    private val german = Language(null, "German")
    private val italian = Language(null, "Italian")
    private val portuguese = Language(null, "Portuguese")

    private fun seedLanguage() {
        if (languageRepository.findAll().isNotEmpty()) return
        val language = listOf<Language>(english, french, spanish, german, italian, portuguese)
        languageRepository.saveAll(language)
    }

    private val tolkien = Author(null, "J.R.R", "Tolkien")
    private val backman = Author(null, "Fredrik", "Backman")
    private val sanderson = Author(null, "Brandon", "Sanderson")
    private val herbert = Author(null, "Frank", "Herbert")
    private val jenkinsReid = Author(null, "Taylor", "Jenkins Reid")
    private val butler = Author(null, "Octavia E.", "Butler")
    private val gaiman = Author(null, "Neil", "Gaiman")

    private fun seedAuthor() {
        if (authorRepository.findAll().isNotEmpty()) return
        val author = listOf<Author>(tolkien, backman, sanderson, herbert, jenkinsReid, butler, gaiman)
        authorRepository.saveAll(author)
    }

    private val williamMorrow = Publisher(null, "William Morrow")
    private val atriaBooks = Publisher(null, "Atria Books")
    private val dragonsteel = Publisher(null, "Dragonsteel Entertainment")
    private val torBooks = Publisher(null, "Tor Books")
    private val warnerBooks = Publisher(null, "Warner Books")
    private val ace = Publisher(null, "Ace")

    private fun seedPublisher() {
        if (publisherRepository.findAll().isNotEmpty()) return
        val publisher = listOf<Publisher>(williamMorrow, atriaBooks, dragonsteel, torBooks, warnerBooks, ace)
        publisherRepository.saveAll(publisher)
    }

    private val fantasy = Tag(null, "Fantasy")
    private val fiction = Tag(null, "Fiction")
    private val sciFi = Tag(null, "Science Fiction")
    private val contemporary = Tag(null, "Contemporary")
    private val adventure = Tag(null, "Adventure")
    private val classics = Tag(null, "Classics")
    private val highFantasy = Tag(null, "High Fantasy")
    private val epicFantasy = Tag(null, "Epic Fantasy")
    private val sciFiFantasy = Tag(null, "Science Fiction Fantasy")
    private val novel = Tag(null, "Novel")
    private val drama = Tag(null, "Drama")
    private val literaryFiction = Tag(null, "Literary Fiction")
    private val bookClub = Tag(null, "Book Club")
    private val adult = Tag(null, "Adult")
    private val summer = Tag(null, "Summer")
    private val friendship = Tag(null, "Friendship")
    private val adultFiction = Tag(null, "Adult Fiction")
    private val epic = Tag(null, "Epic")
    private val magic = Tag(null, "Magic")
    private val mythology = Tag(null, "Mythology")
    private val historicalFiction = Tag(null, "Historical Fiction")
    private val lgbt = Tag(null, "LGBT")
    private val queer = Tag(null, "Queer")
    private val historical = Tag(null, "Historical")
    private val urbanFantasy = Tag(null, "Urban Fantasy")

    private fun seedTags() {
        if (tagRepository.findAll().isNotEmpty()) return
        val tags: List<Tag> =
            listOf(
                fantasy,
                fiction,
                sciFi,
                contemporary,
                adventure,
                classics,
                highFantasy,
                epicFantasy,
                sciFiFantasy,
                novel,
                drama,
                literaryFiction,
                bookClub,
                adult,
                summer,
                friendship,
                adultFiction,
                epic,
                magic,
                mythology,
                historicalFiction,
                lgbt,
                queer,
                historical,
                urbanFantasy,
            )
        tagRepository.saveAll(tags)
    }

    private fun seedBooks() {
        if (bookRepository.findAll().isNotEmpty()) return
        val books =
            listOf<Book>(
                Book(
                    null,
                    "The Fellowship of the Ring",
                    "9780547928210",
                    LocalDate.parse("1954-07-29"),
                    407,
                    "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1651340688i/727798.jpg",
                    "The first volume in J.R.R. Tolkien's epic adventure THE LORD OF THE RINGS\n" +
                        "One Ring to rule them all, One Ring to find them, One Ring to bring them all and in the darkness bind them\n" +
                        "In ancient times the Rings of Power were crafted by the Elven-smiths, and Sauron, the Dark Lord, " +
                        "forged the One Ring, filling it with his own power so that he could rule all others. But the One " +
                        "Ring was taken from him, and though he sought it throughout Middle-earth, it remained lost to him." +
                        " After many ages it fell into the hands of Bilbo " +
                        "Baggins, as told in The Hobbit. In a sleepy village in the Shire, " +
                        "young Frodo Baggins finds himself faced with an immense task," +
                        " as his elderly cousin Bilbo entrusts the Ring to his " +
                        "care. Frodo must leave his home and make a perilous " +
                        "journey across Middle-earth to the Cracks of Doom, there to " +
                        "destroy the Ring and foil the Dark Lord in his evil purpose.",
                    english,
                    williamMorrow,
                    listOf(fantasy, classics, fiction, adventure, highFantasy, epicFantasy, sciFiFantasy, novel),
                    listOf(tolkien),
                ),
                Book(
                    null,
                    "The Two Towers",
                    "9780261102361",
                    LocalDate.parse("1954-11-11"),
                    448,
                    "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1629308732i/727800.jpg",
                    "Begin your journey into Middle-earth.\n" +
                        "The inspiration for the upcoming original series " +
                        "on Prime Video, The Lord of the Rings: The Rings of Power.\n" +
                        "The Two Towers is the second part" +
                        " of J.R.R. Tolkien’s epic adventure The Lord of the Rings.\n" +
                        "One Ring to rule them all, One Ring to " +
                        "find them, One Ring to bring them all and in the darkness bind them.\n" +
                        "Frodo and his Companions of " +
                        "the Ring have been beset by danger during " +
                        "their quest to prevent the Ruling Ring from falling into the " +
                        "hands of the Dark Lord by destroying it in the Cracks of Doom. They " +
                        "have lost the wizard, Gandalf, in a battle in the Mines of Moria. And Boromir," +
                        " seduced by the power of the Ring, tried to seize it by force. While Frodo and " +
                        "Sam made their escape, the rest of the company was attacked by Orcs. Now they " +
                        "continue the journey alone down the great River Anduin—alone, that is, save for" +
                        " the mysterious creeping figure that follows wherever they go.",
                    english,
                    williamMorrow,
                    listOf(fantasy, classics, fiction, adventure, highFantasy, epicFantasy, sciFiFantasy, novel),
                    listOf(tolkien),
                ),
                Book(
                    null,
                    "The Return of the King",
                    "9780261102378",
                    LocalDate.parse("1955-10-20"),
                    432,
                    "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1634055544i/727810.jpg",
                    "The first volume in J.R.R. Tolkien's epic adventure THE LORD OF THE RINGS\n" +
                        "One Ring to rule them all, One Ring to find them, One Ring to bring them all and in the darkness bind them\n" +
                        "In ancient times the Rings of Power were crafted by the Elven-smiths, and Sauron, the Dark Lord, " +
                        "forged the One Ring, filling it with his own power so that he could rule all others." +
                        " But the One Ring was taken from him, and though he sought it throughout Middle-earth, " +
                        "it remained lost to him. After many ages it fell into the hands of Bilbo Baggins, as told " +
                        "in The Hobbit. In a sleepy village in the Shire, young Frodo Baggins finds himself faced" +
                        " with an immense task, as his elderly cousin Bilbo entrusts the Ring to his care. Frodo must " +
                        "leave his home and make a perilous journey across Middle-earth to the Cracks of Doom, there to " +
                        "destroy the Ring and foil the Dark Lord in his evil purpose.",
                    english,
                    williamMorrow,
                    listOf(fantasy, classics, fiction, adventure, highFantasy, epicFantasy, sciFiFantasy, novel),
                    listOf(tolkien),
                ),
                Book(
                    null,
                    "My Friends",
                    "9781982112820",
                    LocalDate.parse("2025-05-06"),
                    436,
                    "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1734625930i/217163697.jpg",
                    "\"The world is full of miracles, but none greater than how far a " +
                        "young person can be carried by someone else's belief in them.\"\n" +
                        "\n" +
                        "Most people don’t even notice them—three tiny figures sitting at the end of a " +
                        "long pier in the corner of one of the most famous paintings in the world." +
                        " Most people think it’s just a depiction of a wide expanse of sea. But Louisa, soon to be eighteen years " +
                        "old and an aspiring artist herself, knows otherwise. She is determined to find out the story behind these " +
                        "three enigmatic figures.\n" +
                        "\n" +
                        "More than two decades before, in a distant seaside town, a group of teenagers find refuge from their " +
                        "bruising home lives by spending long summer days on an abandoned pier telling silly jokes, sharing" +
                        " secrets, and committing small acts of rebellion. These lost souls find in each other a reason to get up " +
                        "every morning, a reason to dream, a reason to love.\n" +
                        "\n" +
                        "Out of that summer emerges a transcendent work of art, a painting that, after a chance encounter in an " +
                        "alleyway, will unexpectedly be placed into Louisa’s care. She embarks on a surprise-filled cross-country " +
                        "journey to discover how the painting came to be and to decide what to do with it. The closer she gets " +
                        "to the painting’s birthplace, the more anxious she becomes about what she'll find. Louisa's complicated life is " +
                        "proof that happy endings are sometimes possible, but they don't always take the form we expect them to.\n" +
                        "\n" +
                        "Fredrik Backman's signature charm, humor, and attention to the poignant " +
                        "details of everyday life are on full display in this funny, moving " +
                        "novel. His most heartfelt and personal tale yet, My Friends is a stunning testament to " +
                        "the transformative, timeless power of art and friendship.",
                    english,
                    atriaBooks,
                    listOf(fiction, contemporary, literaryFiction, bookClub, adult, summer, friendship, adultFiction),
                    listOf(backman),
                ),
                Book(
                    null,
                    "Beartown",
                    "9781501160769",
                    LocalDate.parse("2017-04-25"),
                    418,
                    "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1525349525i/31443394.jpg",
                    "People say Beartown is finished. A tiny community nestled deep in the forest...",
                    english,
                    atriaBooks,
                    listOf(fiction, contemporary, literaryFiction, bookClub, adult, summer, friendship, adultFiction),
                    listOf(backman),
                ),
                Book(
                    null,
                    "And Every Morning the Way Home Gets Longer and Longer",
                    "9781501160578",
                    LocalDate.parse("2015-08-24"),
                    97,
                    "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1472074835i/31373633.jpg",
                    "From the New York Times bestselling author of A Man Called Ove...",
                    english,
                    atriaBooks,
                    listOf(fiction, contemporary, literaryFiction, bookClub, adult, summer, friendship, adultFiction),
                    listOf(backman),
                ),
                Book(
                    null,
                    "Tress of the Emerald Sea",
                    "",
                    LocalDate.parse("2023-01-10"),
                    443,
                    "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1672574587i/60531406.jpg",
                    "The only life Tress has known on her island home in an emerald-green ocean...",
                    english,
                    dragonsteel,
                    listOf(fantasy, fiction, bookClub, adventure, highFantasy, adult),
                    listOf(sanderson),
                ),
                Book(
                    null,
                    "Yumi and the Nightmare Painter",
                    "9781938570377",
                    LocalDate.parse("2023-07-01"),
                    480,
                    "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1689135481i/60531416.jpg",
                    "Yumi comes from a land of gardens, meditation, and spirits...",
                    english,
                    dragonsteel,
                    listOf(fantasy, fiction, bookClub, adventure, highFantasy, adult),
                    listOf(sanderson),
                ),
                Book(
                    null,
                    "The Sunlit Man",
                    "9781938570391",
                    LocalDate.parse("2023-10-01"),
                    447,
                    "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1696146860i/60531420.jpg",
                    "Running. Putting distance between himself and the relentless Night Brigade...",
                    english,
                    dragonsteel,
                    listOf(fantasy, fiction, bookClub, adventure, highFantasy, adult),
                    listOf(sanderson),
                ),
                Book(
                    null,
                    "Dune",
                    "9780593099322",
                    LocalDate.parse("1965-06-01"),
                    658,
                    "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1555447414i/44767458.jpg",
                    "Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides...",
                    english,
                    ace,
                    listOf(sciFi, fiction, fantasy, sciFiFantasy, classics, novel, adventure, bookClub, adult),
                    listOf(herbert),
                ),
                Book(
                    null,
                    "Dune Messiah",
                    "9780593098233",
                    LocalDate.parse("1969-07-01"),
                    336,
                    "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1577043824i/44492285.jpg",
                    "Dune Messiah continues the story of Paul Atreides...",
                    english,
                    ace,
                    listOf(sciFi, fiction, fantasy, sciFiFantasy, classics, novel, adventure, bookClub, adult),
                    listOf(herbert),
                ),
                Book(
                    null,
                    "Children of Dune",
                    "9780593098240",
                    LocalDate.parse("1976-04-01"),
                    609,
                    "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1564783201i/44492286.jpg",
                    "The Children of Dune are twin siblings Leto and Ghanima Atreides...",
                    english,
                    ace,
                    listOf(sciFi, fiction, fantasy, sciFiFantasy, classics, novel, adventure, bookClub, adult),
                    listOf(herbert),
                ),
            )
        bookRepository.saveAll(books)
    }
}
