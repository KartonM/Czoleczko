package online.kozubek.czoleczko

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
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

    private var packagesList: List<QuestionPackage> = emptyList()

    //TODO("Ask someone whether the button should be managed from ViewModel")
    private lateinit var addPackageButton: Button

    private val questionPackagesViewModel: QuestionPackagesViewModel by lazy {
        ViewModelProviders.of(this).get(QuestionPackagesViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_question_packages)
        binding.lifecycleOwner = this

        binding.viewModel = questionPackagesViewModel

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@QuestionPackagesActivity)
            //adapter = QuestionPackageAdapter(listOf<QuestionPackage>(QuestionPackage(name = "jeden"), QuestionPackage(name = "dwa"), QuestionPackage(name="trzy")))
            adapter = QuestionPackageAdapter(emptyList())
        }


        addPackageButton = findViewById(R.id.add_question_package)
        addPackageButton.setOnClickListener {
            showAddPackageDialog()
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val view = super.onCreateView(name, context, attrs)


       // questionPackagesViewModel.questionPackages.observe(
        QuestionRepository.get().getQuestionPackages().observe(
            this@QuestionPackagesActivity,
            Observer {
                if(it != packagesList) {
                    binding.recyclerView.adapter = QuestionPackageAdapter(it)
                    packagesList = it
                    Log.d("TEST", "observer update, ${it.size} items")
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

        override fun getItemCount(): Int {
            //Log.e("TEST", questionPackages.size.toString())
            return questionPackages.size
        }

        override fun onBindViewHolder(holder: QuestionPackageViewHolder, position: Int) {
            holder.bind(questionPackages[position])
        }
    }
}
