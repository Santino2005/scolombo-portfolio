package tome.books.tag

import org.springframework.stereotype.Service

@Service
class TagService(
    private val tagRepo: TagRepository,
) {
    fun getAllTagsByName(search: String): List<String> = tagRepo.searchByName(search = search).map { it.name }
}
