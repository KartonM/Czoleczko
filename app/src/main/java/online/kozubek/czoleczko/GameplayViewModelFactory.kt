package online.kozubek.czoleczko

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*

class GameplayViewModelFactory(private val context: Context, private val questionPackageId: UUID) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameplayViewModel(context, questionPackageId) as T
    }
}