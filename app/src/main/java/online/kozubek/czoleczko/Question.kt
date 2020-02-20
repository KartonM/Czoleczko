package online.kozubek.czoleczko

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Question(
    @PrimaryKey
    val questionId: UUID = UUID.randomUUID(),
    var text: String = "",
    var additionalText: String = "",
    val questionPackageId: UUID
)