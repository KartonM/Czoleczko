package online.kozubek.czoleczko

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(
    entity = QuestionPackage::class,
    parentColumns = arrayOf("questionPackageId"),
    childColumns = arrayOf("packageId"),
    onDelete = CASCADE
)])
data class Question(
    @PrimaryKey
    val questionId: UUID = UUID.randomUUID(),
    var text: String = "",
    var additionalText: String = "",
    val packageId: UUID
)