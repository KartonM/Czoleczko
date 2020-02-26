package online.kozubek.czoleczko

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData

private const val QUESTION_COUNTDOWN_DURATION = 30200 as Long

class Game(private val questions: List<Question>) {

    var currentQuestion: MutableLiveData<Question> = MutableLiveData<Question>()
    var secondsLeft: MutableLiveData<Long> = MutableLiveData<Long>().apply { value =  0 }

    private lateinit var questionTimer: CountDownTimer
    private var questionIndex: Int = 0
    private var timeLeftInMilis: Long = 0
    set(value) {
        field = value
        secondsLeft.value = (value/1000)
    }

    init {
        updateQuestionTexts()
        startQuestionTimer()
    }

    fun skipQuestion() {
        nextQuestion()
    }

    fun questionAnsweredCorrectly() {
        nextQuestion()
    }

    private fun nextQuestion() {
        questionIndex++
        updateQuestionTexts()
        startQuestionTimer()
    }

    private fun updateQuestionTexts() {
        currentQuestion.value = questions[questionIndex]
    }

    private fun startQuestionTimer() {
        questionTimer = QuestionCountDownTimer().start()
    }

    inner class QuestionCountDownTimer : CountDownTimer(QUESTION_COUNTDOWN_DURATION, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timeLeftInMilis = millisUntilFinished
        }

        override fun onFinish() {
            nextQuestion()
        }
    }
}