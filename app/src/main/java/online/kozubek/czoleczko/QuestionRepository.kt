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
    private val executor = Executors.newSingleThreadExecutor()


    fun getQuestionPackages(): LiveData<List<QuestionPackage>> = questionDao.getQuestionPackages()

    fun getQuestionPackagesWithQuestions(): LiveData<List<QuestionPackageWithQuestions>> = questionDao.getQuestionPackagesWithQuestions()

    fun getQuestionPackageWithQuestions(id: UUID): LiveData<QuestionPackageWithQuestions?> = questionDao.getQuestionPackageWithQuestions(id)

    fun addQuestionPackage(questionPackage: QuestionPackage) {
        executor.execute {
            questionDao.addQuestionPackage(questionPackage)
        }
    }

    fun addQuestion(question: Question) {
        executor.execute {
            questionDao.addQuestion(question)
        }
    }

    fun deleteQuestionPackage(questionPackage: QuestionPackage) {
        executor.execute {
            questionDao.deleteQuestionPackage(questionPackage)
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