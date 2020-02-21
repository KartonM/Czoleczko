package online.kozubek.czoleczko

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import online.kozubek.czoleczko.database.QuestionPackageWithQuestions
import online.kozubek.czoleczko.databinding.ActivityQuestionPackagesBinding
import online.kozubek.czoleczko.databinding.QuestionPackageListItemBinding

private const val TAG = "QuestionPackagesRepo"

class QuestionPackagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionPackagesBinding

    private val questionPackagesViewModel: QuestionPackagesViewModel by lazy {
        ViewModelProviders.of(this).get(QuestionPackagesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question_packages)
        binding.lifecycleOwner = this

        binding.viewModel = questionPackagesViewModel

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = QuestionPackageAdapter(emptyList())
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val view = super.onCreateView(name, context, attrs)

        questionPackagesViewModel.questionPackagesWithQuestionsLiveData.observe(
            this,
            Observer {
                binding.recyclerView.adapter = QuestionPackageAdapter(it)
            }
        )

        return view
    }

    private inner class QuestionPackageViewHolder(private val binding: QuestionPackageListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = QuestionPackageViewModel(this@QuestionPackagesActivity)
        }

        fun bind(questionPackage: QuestionPackageWithQuestions) {
            binding.apply {
                viewModel?.questionPackage = questionPackage
                executePendingBindings()
            }
        }
    }

    private inner class QuestionPackageAdapter(private val questionPackages: List<QuestionPackageWithQuestions>) : RecyclerView.Adapter<QuestionPackageViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): QuestionPackageViewHolder {
            val binding = DataBindingUtil.inflate<QuestionPackageListItemBinding>(
                layoutInflater,
                R.layout.question_package_list_item,
                parent,
                false
            )

            return QuestionPackageViewHolder(binding)
        }

        override fun getItemCount(): Int = questionPackages.size

        override fun onBindViewHolder(holder: QuestionPackageViewHolder, position: Int) {
            holder.bind(questionPackages[position])
        }
    }
}
