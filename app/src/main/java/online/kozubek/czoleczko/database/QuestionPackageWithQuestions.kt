package online.kozubek.czoleczko.database

import androidx.room.Embedded
import androidx.room.Relation
import online.kozubek.czoleczko.Question
import online.kozubek.czoleczko.QuestionPackage

data class QuestionPackageWithQuestions(
    @Embedded val questionPackage: QuestionPackage,
    @Relation(
        parentColumn = "questionPackageId",
        entityColumn = "questionId"
    )
    val questions: List<Question>
)