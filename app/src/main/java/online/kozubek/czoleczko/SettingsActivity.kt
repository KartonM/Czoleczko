package online.kozubek.czoleczko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker

private const val MIN_QUESTIONS_PER_GAME = 1
private const val MAX_QUESTIONS_PER_GAME = 50

//methods timeValueIndexToTimeInSeconds(timeValueIndex: Int): Int and timeInSecondsToTimeValueIndex(timeInSeconds: Int): Int
//assume that this array starts with value "0:10" and continues with interval of 10 seconds
private val questionTimeValues = arrayOf("0:10", "0:20", "0:30", "0:40", "0:50", "1:00", "1:10", "1:20", "1:30", "1:40", "1:50", "2:00", "2:10", "2:20", "2:30")

class SettingsActivity : AppCompatActivity() {

    private lateinit var timePicker: NumberPicker
    private lateinit var questionsPicker: NumberPicker

    private var pickedTimeValueIndex: Int = 0
    private var pickedCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        pickedTimeValueIndex = timeInSecondsToTimeValueIndex(
            GamePreferences.getStoredTimePerQuestionInSeconds(this@SettingsActivity)
        )

        pickedCount = GamePreferences
            .getStoredQuestionsPerGameCount(this@SettingsActivity)
            .coerceAtMost(MAX_QUESTIONS_PER_GAME)

        questionsPicker = findViewById(R.id.questions_picker)
        questionsPicker.apply {
            minValue = MIN_QUESTIONS_PER_GAME
            maxValue = MAX_QUESTIONS_PER_GAME
            wrapSelectorWheel = false
            value = pickedCount
            setOnValueChangedListener { _, _, newVal ->
                pickedCount = newVal
            }
        }

        timePicker = findViewById(R.id.time_picker)
        timePicker.apply {
            minValue = 0
            maxValue = questionTimeValues.size - 1
            displayedValues = questionTimeValues
            wrapSelectorWheel = false
            value = pickedTimeValueIndex
            setOnValueChangedListener { _, _, newVal ->
                pickedTimeValueIndex = newVal
            }
        }
    }

    override fun onPause() {
        super.onPause()
        GamePreferences.setStoredQuestionsPerGameCount(this, pickedCount)
        GamePreferences.setStoredTimePerQuestionInSeconds(this, timeValueIndexToTimeInSeconds(pickedTimeValueIndex))
    }

    private fun timeValueIndexToTimeInSeconds(timeValueIndex: Int): Int = (timeValueIndex+1) * 10

    private fun timeInSecondsToTimeValueIndex(timeInSeconds: Int): Int = timeInSeconds/10 - 1
}
