package online.kozubek.czoleczko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import online.kozubek.czoleczko.database.QuestionPackageWithQuestions
import online.kozubek.czoleczko.databinding.ActivityQuestionPackagesBinding
import online.kozubek.czoleczko.databinding.QuestionPackageListItemBinding

private const val TAG = "QuestionPackagesRepo"

private const val PACKAGE_NAME_INPUT_REQUEST_CODE = 0

class QuestionPackagesActivity : AppCompatActivity(), SingleTextInputDialogFragment.Callbacks {

    private lateinit var binding: ActivityQuestionPackagesBinding
    private lateinit var adapter: QuestionPackageAdapter

    //TODO("Ask someone whether the button should be managed from ViewModel")
    private lateinit var addPackageButton: Button

    private val questionPackagesViewModel: QuestionPackagesViewModel by lazy {
        ViewModelProviders.of(this).get(QuestionPackagesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = QuestionPackageAdapter()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question_packages)

        binding.lifecycleOwner = this
        binding.viewModel = questionPackagesViewModel

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@QuestionPackagesActivity)
            adapter = this@QuestionPackagesActivity.adapter
        }

        questionPackagesViewModel.questionPackagesWithQuestionsLiveData.observe(
            this@QuestionPackagesActivity,
            Observer {
                it?.let {
                    adapter.setData(it)
                }
            }
        )

        addPackageButton = findViewById(R.id.add_question_package)
        addPackageButton.setOnClickListener {
            showAddPackageDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val rtn = super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.activity_question_packages, menu)

        return rtn
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_question_package -> {
                showAddPackageDialog()
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTextInserted(input: String) {
        binding.viewModel?.addQuestionPackage(QuestionPackage(name = input))
    }

    private fun showAddPackageDialog() {
//        SingleTextInputDialogFragment().show(supportFragmentManager, "ADD_PACKAGE")
        SingleTextInputDialogFragment
            .newInstance(R.string.insert_package_name, R.string.package_name)
            .show(supportFragmentManager, "ADD_PACKAGE")
    }

    private inner class QuestionPackageViewHolder(private val binding: QuestionPackageListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = QuestionPackageViewModel(this@QuestionPackagesActivity)
        }

        fun bind(questionPackage: QuestionPackageWithQuestions) {
            binding.apply {
                viewModel?.questionPackageWithQuestions = questionPackage
                executePendingBindings()
            }
        }
    }

    private inner class QuestionPackageAdapter() : RecyclerView.Adapter<QuestionPackageViewHolder>() {
        private var questionPackages: List<QuestionPackageWithQuestions> = emptyList<QuestionPackageWithQuestions>()

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

        override fun getItemCount(): Int {
            //Log.e("TEST", questionPackages.size.toString())
            return questionPackages.size
        }

        override fun onBindViewHolder(holder: QuestionPackageViewHolder, position: Int) {
            holder.bind(questionPackages[position])
        }

        fun setData(questionPackages: List<QuestionPackageWithQuestions>) {
            this.questionPackages = questionPackages
            notifyDataSetChanged()
        }
    }
}
