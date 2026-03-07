package tome.bookClub

import jakarta.persistence.Basic
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import java.util.UUID

@Entity
class BookClub(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    val name: String,
    @Lob
    @Basic(fetch = FetchType.EAGER)
    val imgBlob: ByteArray? = null,
)
