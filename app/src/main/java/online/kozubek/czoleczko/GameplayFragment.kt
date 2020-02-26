package online.kozubek.czoleczko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import online.kozubek.czoleczko.databinding.FragmentGameplayBinding
import java.util.*

private const val ARG_PACKAGE_ID = "id"

class GameplayFragment : Fragment() {

    private lateinit var viewModelFactory: GameplayViewModelFactory
    private lateinit var binding: FragmentGameplayBinding

    private val gameplayViewModel: GameplayViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(GameplayViewModel::class.java)
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

        return binding.root
    }


    companion object {
        fun newInstance(questionPackageId: String): GameplayFragment {
            val args = Bundle().apply { putString(ARG_PACKAGE_ID, questionPackageId) }

            return GameplayFragment().apply { arguments = args }
        }
    }
}