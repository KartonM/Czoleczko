package online.kozubek.czoleczko

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*

class GameplayViewModel(private val questionPackageId: UUID) : ViewModel() {
    private val questionRepository: QuestionRepository = QuestionRepository.get()
    private val questionsLiveData: LiveData<List<Question>> = questionRepository.getQuestionsByPackageId(questionPackageId)

    private lateinit var questions: List<Question>


    val questionText = MutableLiveData<String>().apply { value = "Leci sobie nowy Future" }

    init {
        val observer = QuestionsObserver()
        questionsLiveData.observeForever(observer)
    }


    inner class QuestionsObserver : androidx.lifecycle.Observer<List<Question>> {
        override fun onChanged(t: List<Question>?) {
            if(t != null && t.isNotEmpty()) {
                questions = t
                questionsLiveData.removeObserver(this)
            }
        }
    }
}