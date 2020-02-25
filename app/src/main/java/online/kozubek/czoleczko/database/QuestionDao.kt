package online.kozubek.czoleczko.database

import androidx.lifecycle.LiveData
import androidx.room.*
import online.kozubek.czoleczko.Question
import online.kozubek.czoleczko.QuestionPackage
import java.util.*

@Dao
interface QuestionDao {

    @Query("SELECT * FROM Question")
    fun getQuestions(): LiveData<List<Question>>

    @Query("SELECT * FROM Question WHERE packageId=(:questionPackageId)")
    fun getQuestionsByPackageId(questionPackageId: UUID): LiveData<List<Question>>

    @Query("SELECT * FROM Question WHERE questionId=(:id)")
    fun getQuestion(id: UUID): LiveData<Question?>

    @Insert
    fun addQuestion(question: Question)

    @Update
    fun updateQuestion(question: Question)

    @Delete
    fun deleteQuestion(question: Question)
}