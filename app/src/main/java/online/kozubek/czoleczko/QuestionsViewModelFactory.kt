package online.kozubek.czoleczko

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.*

class QuestionsViewModelFactory(private val questionId: UUID) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionsViewModel(questionId) as T
    }
}