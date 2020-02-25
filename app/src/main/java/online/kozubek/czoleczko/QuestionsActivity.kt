package online.kozubek.czoleczko

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
                adapter = QuestionAdapter(emptyList())
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
                it?.let {
                    binding.recyclerView.adapter = QuestionAdapter(it.questions)
                }
            }
        )

        return view
    }

    override fun onQuestionEdited(
        questionId: UUID?,
        questionText: String,
        questionAdditionalText: String,
        questionPackageId: UUID?
    ) {
//        if(questionId != null) {
//            binding.viewModel?.updateQuestion(Question(questionId, questionText, questionAdditionalText, questionPackageId!!))
//        } else {
//            val question = Question(
//                text = questionText,
//                additionalText = questionAdditionalText,
//                packageId = binding.viewModel?.questionPackageId!!
//            )
//
//            binding.viewModel?.addQuestion(question)
//        }
    }

    private fun showAddQuestionDialog() {
        EditQuestionFragment.newInstance(null).show(supportFragmentManager, "ADD_QUESTION")
    }

    private inner class QuestionViewHolder(private val binding: QuestionListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.viewModel = QuestionViewModel()
        }

        fun bind(question: Question) {
            binding.apply {
                viewModel?.question = question
                executePendingBindings()
            }
        }
    }

    private inner class QuestionAdapter(private val questions: List<Question>)
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

    }

    companion object {
        fun newIntent(packageContext: Context, questionPackageId: UUID): Intent {
            return Intent(packageContext, QuestionsActivity::class.java).apply {
                putExtra(EXTRA_PACKAGE_ID, questionPackageId.toString())
            }
        }
    }
}