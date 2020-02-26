package online.kozubek.czoleczko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import online.kozubek.czoleczko.databinding.FragmentGameplayBinding
import java.util.*

private const val ARG_PACKAGE_ID = "id"

class GameplayFragment : Fragment() {

    private lateinit var binding: FragmentGameplayBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGameplayBinding.inflate(inflater, container, false)

        return view
    }


    companion object {
        fun newInstance(questionPackageId: String): GameplayFragment {
            val args = Bundle().apply { putString(ARG_PACKAGE_ID, questionPackageId) }

            return GameplayFragment().apply { arguments = args }
        }
    }
}