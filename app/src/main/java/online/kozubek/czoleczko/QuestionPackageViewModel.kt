package online.kozubek.czoleczko

import android.content.Context
import android.widget.Toast
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import online.kozubek.czoleczko.QuestionPackage
import online.kozubek.czoleczko.database.QuestionPackageWithQuestions

class QuestionPackageViewModel(private val context: Context) : BaseObservable() {
    var questionPackage: QuestionPackageWithQuestions? = null
        set(questionPackage) {
            field = questionPackage
            notifyChange()
        }

    @get: Bindable
    val name: String?
        get() = questionPackage?.questionPackage?.name

    @get: Bindable
    val questionsCount: Int?
        get() = questionPackage?.questions?.size

    fun onPackageClick() {
    }

    fun onDeleteClick() {
        questionPackage?.let {
            QuestionRepository.get().deleteQuestionPackage(it.questionPackage)
        }
    }

    fun onEditClick() {

    }
}