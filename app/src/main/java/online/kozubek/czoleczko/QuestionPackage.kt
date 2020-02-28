package online.kozubek.czoleczko

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class QuestionPackage(
    @PrimaryKey
    val questionPackageId: UUID = UUID.randomUUID(),
    var name: String = ""
)