package online.kozubek.czoleczko

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import java.lang.ClassCastException
import java.util.*

private const val TAG = "EDIT_QUESTION_DIALOG"
private const val ARG_ID = "id"
private const val ARG_TEXT = "text"
private const val ARG_ADDITIONAL_TEXT = "additional"
private const val ARG_PACKAGE_ID = "package"

class EditQuestionFragment : DialogFragment() {

    private lateinit var questionTextEditText: EditText
    private lateinit var questionAdditionalTextEditText: EditText
    private lateinit var questionEditedListener: Callbacks

    private var questionId: UUID? = null
    private var questionPackageId: UUID? = null

    interface Callbacks {
        fun onQuestionEdited(questionId: UUID?, questionText: String, questionAdditionalText: String, questionPackageId: UUID?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            questionEditedListener = activity as Callbacks
        } catch (e: ClassCastException) {
            Log.e(TAG, "onAttach", e)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        val view = View.inflate(context, R.layout.fragment_add_question, null)

        questionTextEditText = view.findViewById(R.id.question_text_input)
        questionAdditionalTextEditText = view.findViewById(R.id.question_additional_text_input)

        arguments?.apply {
            getString(ARG_ID)?.let {
                questionId = UUID.fromString(it)
                questionTextEditText.setText(getString(ARG_TEXT))
                questionAdditionalTextEditText.setText(getString(ARG_ADDITIONAL_TEXT))
                questionPackageId = UUID.fromString(getString(ARG_PACKAGE_ID)!!)
            }
        }


        return builder.setView(view)
            .setTitle(R.string.insert_question)
            .setView(view)
            .setPositiveButton(R.string.confirm) { _, _ ->
                questionEditedListener.onQuestionEdited(
                    questionId,
                    questionTextEditText.text.toString(),
                    questionAdditionalTextEditText.text.toString(),
                    questionPackageId
                )
            }.setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog?.cancel()
            }.create()
    }

    companion object {
        fun newInstance(question: Question?): EditQuestionFragment {
            val args = Bundle().apply {
                question?.let {
                    putString(ARG_ID, it.questionId.toString())
                    putString(ARG_TEXT, it.text)
                    putString(ARG_ADDITIONAL_TEXT, it.additionalText)
                    putString(ARG_PACKAGE_ID, it.questionPackageId.toString())
                }
            }

            return EditQuestionFragment().apply { arguments = args }
        }
    }
}