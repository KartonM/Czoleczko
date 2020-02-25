package online.kozubek.czoleczko

import android.content.Context
import android.content.Intent
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
//import online.kozubek.czoleczko.database.QuestionPackageWithQuestions

class QuestionPackageViewModel(private val context: Context) : BaseObservable() {
    var questionPackage: QuestionPackage? = null
        set(questionPackage) {
            field = questionPackage
            notifyChange()
        }

    @get: Bindable
    val name: String?
        get() = questionPackage?.name

    @get: Bindable
    val questionsCount: Int?
    get() = 13
        //get() = questionPackage?.questions?.size

    fun onPackageClick() {

    }

    fun onDeleteClick() {
        questionPackage?.let {
            QuestionRepository.get().deleteQuestionPackage(it)
        }
    }

    fun onEditClick() {
        val id = questionPackage?.questionPackageId!!
        context.startActivity(QuestionsActivity.newIntent(context, id))
    }
}