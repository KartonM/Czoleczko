package online.kozubek.czoleczko

import android.app.Application

class CzoleczkoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        QuestionRepository.initialize(this)
    }
}