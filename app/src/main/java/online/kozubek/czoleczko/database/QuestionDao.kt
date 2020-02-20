package online.kozubek.czoleczko.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import online.kozubek.czoleczko.Question
import online.kozubek.czoleczko.QuestionPackage
import java.util.*

@Dao
interface QuestionDao {
    @Query("SELECT * FROM QuestionPackage")
    fun getQuestionPackages(): LiveData<List<QuestionPackage>>


    @Transaction
    @Query("SELECT * FROM QuestionPackage WHERE questionPackageId=(:id)")
    fun getQuestionPackageWithQuestions(id: UUID): LiveData<QuestionPackageWithQuestions?>

    @Insert
    fun addQuestionPackage(questionPackage: QuestionPackage)

    @Insert
    fun addQuestion(question: Question)
}