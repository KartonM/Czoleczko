package online.kozubek.czoleczko

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import java.util.*

private const val EXTRA_PACKAGE_ID = "online.kozubek.czoleczko.question_package_id"

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //remove title and notification bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_game)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null) {

            val questionPackageId = intent.extras?.getString(EXTRA_PACKAGE_ID)!!
            val fragment = GameplayFragment.newInstance(questionPackageId)

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    companion object {
        fun newIntent(packageContext: Context, questionPackageId: UUID): Intent {
            return Intent(packageContext, GameActivity::class.java).apply {
                putExtra(EXTRA_PACKAGE_ID, questionPackageId.toString())
            }
        }
    }
}
