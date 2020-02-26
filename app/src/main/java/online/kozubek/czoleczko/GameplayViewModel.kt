package online.kozubek.czoleczko

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import java.util.*

class GameplayViewModel(private val questionPackageId: UUID) : ViewModel() {
    private val questionRepository: QuestionRepository = QuestionRepository.get()
    private val questionsLiveData: LiveData<List<Question>> = questionRepository.getQuestionsByPackageId(questionPackageId)

    private lateinit var questions: List<Question>
    private lateinit var game: Game
    private lateinit var currentQuestionObserver: Observer<Question>
    private lateinit var timeLeftObserver: Observer<Long>

    var questionText = MutableLiveData<String>().apply { value = "Taco Hemingway" }
    var questionAdditionalText = MutableLiveData<String>().apply { value = "Taco Hemingway" }
    var timeLeft = MutableLiveData<String>().apply { value = "Taco Hemingway" }

    init {
        val observer = QuestionsObserver()
        questionsLiveData.observeForever(observer)
    }


    inner class QuestionsObserver : Observer<List<Question>> {
        override fun onChanged(t: List<Question>?) {
            if(t != null && t.isNotEmpty()) {
                questions = t
                questionsLiveData.removeObserver(this)
                game = Game(questions)

                currentQuestionObserver = Observer{
                    questionText.value = it.text
                    questionAdditionalText.value = it.additionalText
                }
                game.currentQuestion.observeForever(currentQuestionObserver)

                timeLeftObserver = Observer {
                    timeLeft.value = if(it < 60) it.toString() else "${it/60}:${it%60}"
                }
                game.secondsLeft.observeForever(timeLeftObserver)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        game.currentQuestion.removeObserver(currentQuestionObserver)
        game.secondsLeft.removeObserver(timeLeftObserver)
    }
}