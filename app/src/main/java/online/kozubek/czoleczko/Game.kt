package online.kozubek.czoleczko

import android.content.Context
import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData


private const val TIME = 30000L
class Game(context: Context, questions: List<Question>) {

    var currentQuestion: MutableLiveData<Question> = MutableLiveData<Question>()
    var secondsLeft: MutableLiveData<Long> = MutableLiveData<Long>().apply { value =  0 }
    var resultLiveData: MutableLiveData<GameResult> = MutableLiveData<GameResult>()

    private val questionsPerGame = GamePreferences.getStoredQuestionsPerGameCount(context).coerceAtMost(questions.size)
    private val questionCountdownDuration = GamePreferences.getStoredTimePerQuestionInSeconds(context) * 1000L + 200L

    private val result = GameResult(0, questionsPerGame, false)

    private val questions: List<Question> = questions.shuffled().subList(0, questionsPerGame)
    private var questionIndex: Int = 0

    private var questionTimer: CountDownTimer? = null
    private var timeLeftInMilis: Long = 0
    set(value) {
        field = value
        secondsLeft.value = (value/1000)
    }

    init {
        resultLiveData.value = result

        updateQuestion()
        startQuestionTimer()
    }

    fun skipQuestion() {
        nextQuestion()
    }

    fun questionAnsweredCorrectly() {
        result.score++
        resultLiveData.value = result

        nextQuestion()
    }

    private fun nextQuestion() {
        questionIndex++

        if(questionIndex < questionsPerGame) {
            updateQuestion()
            startQuestionTimer()
        } else {
            result.hasGameEnded = true
            resultLiveData.value = result
        }
    }

    private fun updateQuestion() {
        currentQuestion.value = questions[questionIndex]
    }

    private fun startQuestionTimer() {
        questionTimer?.cancel()
        questionTimer = QuestionCountDownTimer().start()
    }

    private inner class QuestionCountDownTimer : CountDownTimer(questionCountdownDuration, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timeLeftInMilis = millisUntilFinished
        }

        override fun onFinish() {
            nextQuestion()
        }
    }
}