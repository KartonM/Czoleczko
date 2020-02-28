package online.kozubek.czoleczko

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import online.kozubek.czoleczko.database.QuestionPackageWithQuestions

class QuestionPackageViewModel(private val context: Context) : BaseObservable() {
    var questionPackageWithQuestions: QuestionPackageWithQuestions? = null
        set(questionPackage) {
            field = questionPackage
            notifyChange()
        }

    @get: Bindable
    val name: String?
        get() = questionPackageWithQuestions?.questionPackage?.name

    @get: Bindable
    val questionsCount: Int?
        get() = questionPackageWithQuestions?.questions?.size

    fun onPackageClick() {
        val id = questionPackageWithQuestions?.questionPackage?.questionPackageId!!
        val intent = GameActivity.newIntent(context, id)

        context.startActivity(intent)
    }

    fun onDeleteClick() {
        questionPackageWithQuestions?.let {
            QuestionRepository.get().deleteQuestionPackage(it.questionPackage)
        }
    }

    fun onEditClick() {
        val id = questionPackageWithQuestions?.questionPackage?.questionPackageId!!
        context.startActivity(QuestionsActivity.newIntent(context, id))
    }
}