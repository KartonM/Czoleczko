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

class QuestionsActivity : AppCompatActivity(), EditQuestionFragment.Callbacks, SingleTextInputDialogFragment.Callbacks {

    private lateinit var viewModelFactory: QuestionsViewModelFactory
    private lateinit var binding: ActivityQuestionsBinding
    private var adapter: QuestionAdapter = QuestionAdapter()

    private var questionPackage: QuestionPackage? = null

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

            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                this@apply.adapter = this@QuestionsActivity.adapter
            }

        }

        questionsViewModel.questionPackageWithQuestionsLiveData.observe(
            this,
            androidx.lifecycle.Observer {
                it?.let {
                    adapter.setData(it.questions)
                    supportActionBar?.title = it.questionPackage.name
                    questionPackage = it.questionPackage
                }
            }
        )

        findViewById<Button>(R.id.add_question).setOnClickListener {
            showAddQuestionDialog()
        }
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
                SingleTextInputDialogFragment
                    .newInstance(R.string.insert_package_name, R.string.package_name, questionPackage?.name ?: "")
                    .show(supportFragmentManager, "EDIT_PACKAGE_NAME")
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
            questionsViewModel.updateQuestion(Question(questionId, questionText, questionAdditionalText, questionPackageId!!))
        } else {
            val question = Question(
                text = questionText,
                additionalText = questionAdditionalText,
                packageId = binding.viewModel?.questionPackageId!!
            )

            questionsViewModel.addQuestion(question)
        }
    }

    override fun onTextInserted(input: String) {
        questionPackage?.let { questionPackage ->
            questionPackage.name = input
            questionsViewModel.updateQuestionPackage(questionPackage)
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

    private inner class QuestionAdapter() : RecyclerView.Adapter<QuestionViewHolder>() {
        private var questions: List<Question> = emptyList<Question>()

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
            notifyDataSetChanged()
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