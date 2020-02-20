package online.kozubek.czoleczko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import java.util.*

private const val TAG = "QuestionPackagesRepo"

class QuestionPackagesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_packages)
    }
}
