package online.kozubek.czoleczko

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
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

class QuestionPackagesActivity : AppCompatActivity(), AddQuestionPackageFragment.Callbacks {

    private lateinit var binding: ActivityQuestionPackagesBinding
    private lateinit var adapter: QuestionPackageAdapter

    //TODO("Ask someone whether the button should be managed from ViewModel")
    private lateinit var addPackageButton: Button

    private val questionPackagesViewModel: QuestionPackagesViewModel by lazy {
        ViewModelProviders.of(this).get(QuestionPackagesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = QuestionPackageAdapter(emptyList())
        binding = DataBindingUtil.setContentView(this, R.layout.activity_question_packages)

        binding.lifecycleOwner = this
        binding.viewModel = questionPackagesViewModel

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@QuestionPackagesActivity)
            adapter = this@QuestionPackagesActivity.adapter
        }


        addPackageButton = findViewById(R.id.add_question_package)
        addPackageButton.setOnClickListener {
            showAddPackageDialog()
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val view = super.onCreateView(name, context, attrs)


        questionPackagesViewModel.questionPackagesWithQuestionsLiveData.observe(
            this@QuestionPackagesActivity,
            Observer {
                it?.let {
                    if(adapter.questionPackages != it) {
                        adapter = QuestionPackageAdapter(it)
                        binding.recyclerView.adapter = adapter
                    } else {
                        adapter.setData(it)
                    }
                }
            }
        )

        return view
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

    override fun onPackageNameInserted(name: String) {
        binding.viewModel?.addQuestionPackage(QuestionPackage(name = name))
    }

    private fun showAddPackageDialog() {
        AddQuestionPackageFragment().show(supportFragmentManager, "ADD_PACKAGE")

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

    private inner class QuestionPackageAdapter(var questionPackages: List<QuestionPackageWithQuestions>) : RecyclerView.Adapter<QuestionPackageViewHolder>() {
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
        }
    }
}
