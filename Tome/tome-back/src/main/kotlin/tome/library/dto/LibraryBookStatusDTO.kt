package tome.library.dto

import tome.library.status.LibraryBookStatusType
import java.time.LocalDate

class LibraryBookStatusDTO(
    val readingStatus: LibraryBookStatusType,
    val startedAt: LocalDate?,
    val finishedAt: LocalDate?,
    val currentPage: Int?,
)
