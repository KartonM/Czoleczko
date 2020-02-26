package online.kozubek.czoleczko

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*

class GameplayViewModelFactory(private val questionPackageId: UUID) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameplayViewModel(questionPackageId) as T
    }
}