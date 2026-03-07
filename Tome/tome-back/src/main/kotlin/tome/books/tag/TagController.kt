package tome.books.tag

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tome.books.dto.TagsDTO
import tome.books.toTagsDTO

@RequestMapping("/tags")
@RestController
class TagController(
    private val tagService: TagService,
) {
    //Buscar tags
    @GetMapping()
    fun searchByName(
        @RequestParam search: String,
    ): TagsDTO = toTagsDTO(tagService.getAllTagsByName(search = search))
}
