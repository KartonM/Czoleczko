package online.kozubek.czoleczko

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import online.kozubek.czoleczko.database.QuestionPackageWithQuestions
import java.util.*

class QuestionsViewModel(val questionPackageId: UUID) : ViewModel() {
    private val questionRepository = QuestionRepository.get()

    val questionPackageWithQuestionsLiveData: LiveData<QuestionPackageWithQuestions?> =
        questionRepository.getQuestionPackageWithQuestions(questionPackageId)

    var noQuestionsMessageVisibility: LiveData<Int> =
        Transformations.map(questionPackageWithQuestionsLiveData) { questionPackage ->
            if (questionPackage == null || questionPackage.questions.any()) View.GONE else View.VISIBLE
        }

    fun addQuestion(question: Question) {
        questionRepository.addQuestion(question)
    }

    fun updateQuestion(question: Question) {
        questionRepository.updateQuestion(question)
    }

    fun updateQuestionPackage(questionPackage: QuestionPackage) {
        questionRepository.updateQuestionPackage(questionPackage)
    }
}