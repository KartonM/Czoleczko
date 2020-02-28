package online.kozubek.czoleczko

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import java.util.*

class GameplayViewModel(private val context: Context, private val questionPackageId: UUID) : ViewModel() {
    private val questionRepository: QuestionRepository = QuestionRepository.get()
    private val questionsLiveData: LiveData<List<Question>> = questionRepository.getQuestionsByPackageId(questionPackageId)
    private var gameInProgress: Boolean = false

    private lateinit var questions: List<Question>
    private lateinit var game: Game

    private lateinit var currentQuestionObserver: Observer<Question>
    private lateinit var timeLeftObserver: Observer<Long>
    private lateinit var gameResultObserver: Observer<GameResult>

    var questionText = MutableLiveData<String>().apply { value = "Taco Hemingway" }
    var questionAdditionalText = MutableLiveData<String>().apply { value = "Taco Hemingway" }
    var timeLeft = MutableLiveData<String>().apply { value = "Taco Hemingway" }
    var gameResultLiveData = MutableLiveData<GameResult>()

    init {
        val observer = QuestionsObserver()
        questionsLiveData.observeForever(observer)
    }

    fun onScreenTapped() {
        if(game != null && gameInProgress) {
            game.questionAnsweredCorrectly()
        }
    }

    fun onFling() {
        Log.d("GESTURE", "Fling!")
        if(game != null && gameInProgress) {
            game.skipQuestion()
        }
    }

    inner class QuestionsObserver : Observer<List<Question>> {
        override fun onChanged(t: List<Question>?) {
            if(t != null && t.isNotEmpty()) {
                questions = t
                questionsLiveData.removeObserver(this)
                game = Game(context, questions)
                gameInProgress = true

                currentQuestionObserver = Observer{
                    questionText.value = it.text
                    questionAdditionalText.value = it.additionalText
                }
                game.currentQuestion.observeForever(currentQuestionObserver)

                timeLeftObserver = Observer {
                    timeLeft.value = if(it < 60) it.toString() else "${it/60}:${precedingZeroChar(it)}${it%60}"
                }
                game.secondsLeft.observeForever(timeLeftObserver)

                gameResultObserver = Observer {
                    if(it.hasGameEnded) {
                        gameInProgress = false
                    }
                    gameResultLiveData.value = it
                    Log.i("SCORE", it.toString())
                }
                game.resultLiveData.observeForever(gameResultObserver)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        game.currentQuestion.removeObserver(currentQuestionObserver)
        game.secondsLeft.removeObserver(timeLeftObserver)
        game.resultLiveData.removeObserver(gameResultObserver)
    }

    private fun precedingZeroChar(timeInSeconds: Long): String = if(timeInSeconds/60 > 0 && timeInSeconds%60 < 10) "0" else ""
}