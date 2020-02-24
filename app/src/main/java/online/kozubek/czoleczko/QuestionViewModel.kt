package online.kozubek.czoleczko

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import online.kozubek.czoleczko.database.QuestionPackageWithQuestions

class QuestionViewModel : BaseObservable() {

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
}