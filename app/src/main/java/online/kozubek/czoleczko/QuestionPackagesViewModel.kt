package online.kozubek.czoleczko

import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import online.kozubek.czoleczko.database.QuestionPackageWithQuestions

class QuestionPackagesViewModel : ViewModel() {
    private val questionRepository = QuestionRepository.get()

    val questionPackagesWithQuestionsLiveData: LiveData<List<QuestionPackageWithQuestions>> =
        questionRepository.getQuestionPackagesWithQuestions()

    var noPackagesMessageVisibility: LiveData<Int> =
        Transformations.map(questionPackagesWithQuestionsLiveData) { questionPackages ->
            if (questionPackages.any()) View.GONE else View.VISIBLE
        }
}