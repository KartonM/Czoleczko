package online.kozubek.czoleczko

import android.content.Context
import android.preference.PreferenceManager

private const val PREF_FILE_NAME = "settings"
private const val PREF_TIME = "time"
private const val PREF_COUNT = "count"

object GamePreferences {
    fun getStoredTimePerQuestionInSeconds(context: Context): Int {
        return context
            .getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
            .getInt(PREF_TIME, 30)
    }

    fun getStoredQuestionsPerGameCount(context: Context): Int {
        return context
            .getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
            .getInt(PREF_COUNT, 3)
    }

    fun setStoredTimePerQuestionInSeconds(context: Context, timeInSeconds: Int) = writeInt(context, PREF_TIME, timeInSeconds)

    fun setStoredQuestionsPerGameCount(context: Context, questionsCount: Int) = writeInt(context, PREF_COUNT, questionsCount)

    private fun writeInt(context: Context, key: String, value: Int) {
        val sharedPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(key, value)
            commit()
        }
    }
}