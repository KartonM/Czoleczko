package online.kozubek.czoleczko

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import online.kozubek.czoleczko.databinding.FragmentScoreBinding

private const val ARG_PLAYER_SCORE = "player"
private const val ARG_MAX_SCORE = "max"

class ScoreFragment : Fragment() {
    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onBackClicked()
        fun onReplayClicked()
    }


    private var callbacks: Callbacks? = null
    private lateinit var binding: FragmentScoreBinding

    private val scoreViewModel: ScoreViewModel by lazy {
        ViewModelProviders.of(this).get(ScoreViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scoreViewModel.playerScore.value = arguments?.getInt(ARG_PLAYER_SCORE) ?: 0
        scoreViewModel.maxScore.value = arguments?.getInt(ARG_MAX_SCORE) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScoreBinding.inflate(inflater, container, false)
        binding.viewModel = scoreViewModel

        binding.backButton.setOnClickListener {
            callbacks?.onBackClicked()
        }
        binding.replayButton.setOnClickListener {
            callbacks?.onReplayClicked()
        }

        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(result: GameResult): ScoreFragment {
            val args = Bundle().apply {
                putInt(ARG_PLAYER_SCORE, result.score)
                putInt(ARG_MAX_SCORE, result.maxScore)
            }

            return ScoreFragment().apply { arguments = args }
        }
    }
}