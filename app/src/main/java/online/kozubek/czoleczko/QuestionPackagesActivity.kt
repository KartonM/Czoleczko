package online.kozubek.czoleczko

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import online.kozubek.czoleczko.databinding.ActivityQuestionPackagesBinding
import online.kozubek.czoleczko.databinding.QuestionPackageListItemBinding

private const val TAG = "QuestionPackagesRepo"

class QuestionPackagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionPackagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question_packages)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = QuestionPackageAdapter(emptyList())
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val view = super.onCreateView(name, context, attrs)

        QuestionRepository.get().getQuestionPackages().observe(
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
            binding.viewModel = QuestionPackageViewModel()
        }

        fun bind(questionPackage: QuestionPackage) {
            binding.apply {
                viewModel?.questionPackage = questionPackage
                executePendingBindings()
            }
        }
    }

    private inner class QuestionPackageAdapter(private val questionPackages: List<QuestionPackage>) : RecyclerView.Adapter<QuestionPackageViewHolder>() {
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
