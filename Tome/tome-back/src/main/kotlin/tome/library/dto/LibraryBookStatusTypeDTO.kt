package tome.library.dto

import tome.library.status.LibraryBookStatusType

data class LibraryBookStatusTypeDTO(
    val readingStatus: LibraryBookStatusType,
    val currentPage: Int?,
    val startedAt: String?,
    val finishedAt: String?,
)
