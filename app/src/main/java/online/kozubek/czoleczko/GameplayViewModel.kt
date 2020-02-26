package online.kozubek.czoleczko

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*

class GameplayViewModel(private val questionPackageId: UUID) : ViewModel() {
    private val questionRepository: QuestionRepository = QuestionRepository.get()

    private var questions: List<Question> = questionRepository.getQuestionsByPackageId(questionPackageId).value ?: emptyList()

    init {
        questions.forEach {
            Log.i("DUPA", it.text)
        }
    }
}