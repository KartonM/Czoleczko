package online.kozubek.czoleczko

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import online.kozubek.czoleczko.database.QuestionDatabase
import online.kozubek.czoleczko.database.QuestionPackageWithQuestions
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

private const val DATABASE_NAME = "question-database"

class QuestionRepository private constructor(context: Context) {

    private val db: QuestionDatabase = Room.databaseBuilder(
        context.applicationContext,
        QuestionDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val questionDao = db.questionDao()
    private val questionPackageDao = db.questionPackageDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getQuestionPackage(id: UUID): LiveData<QuestionPackage?> = questionPackageDao.getQuestionPackage(id)

    fun getQuestionPackages(): LiveData<List<QuestionPackage>> = questionPackageDao.getQuestionPackages()

    fun getQuestionPackagesWithQuestions(): LiveData<List<QuestionPackageWithQuestions>> = questionPackageDao.getQuestionPackagesWithQuestions()

    fun getQuestionPackageWithQuestions(id: UUID): LiveData<QuestionPackageWithQuestions?> = questionPackageDao.getQuestionPackageWithQuestions(id)

    fun addQuestionPackage(questionPackage: QuestionPackage) {
        executor.execute {
            questionPackageDao.addQuestionPackage(questionPackage)
        }
    }

    fun addQuestion(question: Question) {
        executor.execute {
            questionDao.addQuestion(question)
        }
    }

    fun updateQuestion(question: Question) {
        executor.execute {
            questionDao.updateQuestion(question)
        }
    }

    fun deleteQuestionPackage(questionPackage: QuestionPackage) {
        executor.execute {
            questionPackageDao.deleteQuestionPackage(questionPackage)
        }
    }

    fun getQuestions(): LiveData<List<Question>> = questionDao.getQuestions()

    fun getQuestionsByPackageId(packageId: UUID) = questionDao.getQuestionsByPackageId(packageId)

    fun getQuestion(id: UUID): LiveData<Question?> = questionDao.getQuestion(id)

    fun deleteQuestion(question: Question) {
        executor.execute {
            questionDao.deleteQuestion(question)
        }
    }

    companion object {
        private var INSTANCE: QuestionRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null) {
                INSTANCE = QuestionRepository(context)
            } else {

            }
        }

        fun get(): QuestionRepository {
            return INSTANCE ?:
                    throw IllegalStateException("Question repository must be initialized")
        }
    }
}