package online.kozubek.czoleczko

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import online.kozubek.czoleczko.databinding.FragmentGameplayBinding
import java.util.*

private const val ARG_PACKAGE_ID = "id"

class GameplayFragment : Fragment() {
    /**
     * Required interface for hosting activities
     */
    interface Callbacks {
        fun onGameEnded(result: GameResult)
    }
    private var callbacks: Callbacks? = null

    private lateinit var viewModelFactory: GameplayViewModelFactory
    private lateinit var binding: FragmentGameplayBinding

    private val gameplayViewModel: GameplayViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(GameplayViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val questionPackageId = UUID.fromString(arguments?.getString(ARG_PACKAGE_ID))
        viewModelFactory = GameplayViewModelFactory(questionPackageId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameplayBinding.inflate(inflater, container, false)
        binding.viewModel = gameplayViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        gameplayViewModel.gameResultLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {result ->
                if(result.hasGameEnded) {
                    callbacks?.onGameEnded(result)
                }
            }
        })
        val flyGestureDetector = GestureDetectorCompat(context, FlingGestureListener())
        binding.layout.setOnTouchListener { v, event ->
            flyGestureDetector.onTouchEvent(event)
        }
        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private inner class FlingGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            binding.viewModel?.onFling()
            return true
        }
    }

    companion object {
        fun newInstance(questionPackageId: String): GameplayFragment {
            val args = Bundle().apply { putString(ARG_PACKAGE_ID, questionPackageId) }

            return GameplayFragment().apply { arguments = args }
        }
    }
}