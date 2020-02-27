package online.kozubek.czoleczko

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData

private const val QUESTION_COUNTDOWN_DURATION = 30200 as Long
private const val QUESTIONS_PER_GAME = 3

class Game(questions: List<Question>) {

    var currentQuestion: MutableLiveData<Question> = MutableLiveData<Question>()
    var secondsLeft: MutableLiveData<Long> = MutableLiveData<Long>()//.apply { value =  0 }
    var resultLiveData: MutableLiveData<GameResult> = MutableLiveData<GameResult>()

    private val result = GameResult(0, QUESTIONS_PER_GAME, false)

    private val questions: List<Question> = questions.shuffled().subList(0, QUESTIONS_PER_GAME)
    private var questionIndex: Int = 0

    private lateinit var questionTimer: CountDownTimer
    private var timeLeftInMilis: Long = 0
    set(value) {
        field = value
        val currentValue = secondsLeft.value
        if((currentValue != null && currentValue > (value/1000))
            || currentValue == null) {
            secondsLeft.value = (value/1000)
        }
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

        if(questionIndex < QUESTIONS_PER_GAME) {
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
        questionTimer = QuestionCountDownTimer().start()
    }

    private inner class QuestionCountDownTimer : CountDownTimer(QUESTION_COUNTDOWN_DURATION, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timeLeftInMilis = millisUntilFinished
        }

        override fun onFinish() {
            nextQuestion()
        }
    }
}