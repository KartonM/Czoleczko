package online.kozubek.czoleczko

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import online.kozubek.czoleczko.QuestionPackage
import online.kozubek.czoleczko.database.QuestionPackageWithQuestions

class QuestionPackageViewModel : BaseObservable() {
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
}