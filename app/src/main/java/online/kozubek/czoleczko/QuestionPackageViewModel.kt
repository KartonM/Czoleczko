package online.kozubek.czoleczko

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import online.kozubek.czoleczko.QuestionPackage

class QuestionPackageViewModel : BaseObservable() {
    var questionPackage: QuestionPackage? = null
        set(questionPackage) {
            field = questionPackage
            notifyChange()
        }

    @get: Bindable
    val name: String?
        get() = questionPackage?.name
}