package online.kozubek.czoleczko.database

import androidx.lifecycle.LiveData
import androidx.room.*
import online.kozubek.czoleczko.QuestionPackage
import java.util.*

@Dao
interface QuestionPackageDao {
    @Query("SELECT * FROM QuestionPackage")
    fun getQuestionPackages(): LiveData<List<QuestionPackage>>

    @Transaction
    @Query("SELECT * FROM QuestionPackage")
    fun getQuestionPackagesWithQuestions(): LiveData<List<QuestionPackageWithQuestions>>

    @Query("SELECT * FROM QuestionPackage WHERE questionPackageId=(:id)")
    fun getQuestionPackage(id: UUID): LiveData<QuestionPackage?>

    @Transaction
    @Query("SELECT * FROM QuestionPackage WHERE questionPackageId=(:id)")
    fun getQuestionPackageWithQuestions(id: UUID): LiveData<QuestionPackageWithQuestions?>

    @Insert
    fun addQuestionPackage(questionPackage: QuestionPackage)

    @Delete
    fun deleteQuestionPackage(questionPackage: QuestionPackage)
}