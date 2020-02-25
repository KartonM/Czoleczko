package online.kozubek.czoleczko

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import online.kozubek.czoleczko.database.QuestionPackageWithQuestions

class QuestionViewModel(private val context: Context) : BaseObservable() {

    var question: Question? = null
        set(question) {
            field = question
            notifyChange()
        }

    @get: Bindable
    val text: String?
        get() = question?.text

    @get: Bindable
    val additionalText: String?
        get() = question?.additionalText

    @get: Bindable
    val additionalTextVisibility: Int
        get() = if(additionalText.isNullOrBlank()) View.GONE else View.VISIBLE

    fun deleteQuestion() {
        question?.let {
            QuestionRepository.get().deleteQuestion(question!!)
        }
    }

    fun editQuestion() {
        EditQuestionFragment.newInstance(question).show((context as AppCompatActivity).supportFragmentManager, "ADD_QUESTION")
    }
}