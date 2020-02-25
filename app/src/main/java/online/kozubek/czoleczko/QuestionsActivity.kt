package online.kozubek.czoleczko

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import online.kozubek.czoleczko.databinding.ActivityQuestionsBinding
import online.kozubek.czoleczko.databinding.QuestionListItemBinding
import java.util.*

private const val EXTRA_PACKAGE_ID = "online.kozubek.czoleczko.question_package_id"

class QuestionsActivity : AppCompatActivity(), EditQuestionFragment.Callbacks {

    private lateinit var viewModelFactory: QuestionsViewModelFactory
    private lateinit var binding: ActivityQuestionsBinding
    private lateinit var adapter: QuestionAdapter

    private val questionsViewModel: QuestionsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(QuestionsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val questionPackageId = UUID.fromString(intent.extras?.getString(EXTRA_PACKAGE_ID)!!)

        viewModelFactory = QuestionsViewModelFactory(questionPackageId)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_questions)

        binding.apply {
            lifecycleOwner = this@QuestionsActivity
            viewModel = questionsViewModel

            adapter = QuestionAdapter(emptyList())
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                this@apply.adapter = this@QuestionsActivity.adapter
            }

        }

        findViewById<Button>(R.id.add_question).setOnClickListener {
            showAddQuestionDialog()
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        val view =  super.onCreateView(name, context, attrs)

        questionsViewModel.questionPackageWithQuestionsLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                Log.i("QUESTIONS", "Observer called, ${it?.questions?.size} items")
                it?.let {
                    if(adapter.questions != it.questions) {
                        adapter = QuestionAdapter(it.questions)
                        binding.recyclerView.adapter = adapter
                    } else {
                        adapter.setData(it.questions)
                    }
                    supportActionBar?.title = it.questionPackage.name
                }
            }
        )

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val rtn = super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.activity_questions, menu)

        return rtn
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.new_question -> {
                showAddQuestionDialog()
                true
            }
            R.id.edit_question_package_name -> {

                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onQuestionEdited(
        questionId: UUID?,
        questionText: String,
        questionAdditionalText: String,
        questionPackageId: UUID?
    ) {
        if(questionId != null) {
            binding.viewModel?.updateQuestion(Question(questionId, questionText, questionAdditionalText, questionPackageId!!))
        } else {
            val question = Question(
                text = questionText,
                additionalText = questionAdditionalText,
                packageId = binding.viewModel?.questionPackageId!!
            )

            binding.viewModel?.addQuestion(question)
        }
    }

    private fun showAddQuestionDialog() {
        EditQuestionFragment.newInstance(null).show(supportFragmentManager, "ADD_QUESTION")
    }

    private inner class QuestionViewHolder(private val binding: QuestionListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            Log.i("QUESTIONS", "ViewHolder initialized")
            binding.viewModel = QuestionViewModel(this@QuestionsActivity)
        }

        fun bind(question: Question) {
            binding.apply {
                viewModel?.question = question
                executePendingBindings()
            }
        }
    }

    private inner class QuestionAdapter(var questions: List<Question>)
        : RecyclerView.Adapter<QuestionViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
            val binding = DataBindingUtil.inflate<QuestionListItemBinding>(
                layoutInflater,
                R.layout.question_list_item,
                parent,
                false
            )

            return QuestionViewHolder(binding)
        }

        override fun getItemCount(): Int = questions.size

        override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
            holder.bind(questions[position])
        }

        fun setData(questions: List<Question>) {
            this.questions = questions
        }

    }

    companion object {
        fun newIntent(packageContext: Context, questionPackageId: UUID): Intent {
            return Intent(packageContext, QuestionsActivity::class.java).apply {
                putExtra(EXTRA_PACKAGE_ID, questionPackageId.toString())
            }
        }
    }
}