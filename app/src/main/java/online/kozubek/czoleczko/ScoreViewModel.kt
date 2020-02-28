package online.kozubek.czoleczko

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel : ViewModel() {
    var playerScore = MutableLiveData<Int>().apply { value = 0 }
    var maxScore = MutableLiveData<Int>().apply { value = 0 }
}