package online.kozubek.czoleczko

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

private const val TAG = "ADD_PACKAGE_DIALOG"

private const val ARG_TITLE = "title"
private const val ARG_HINT = "hint"
private const val ARG_VALUE = "value"

class SingleTextInputDialogFragment : DialogFragment() {

    private lateinit var inputEditText: EditText
    private lateinit var onInputListener: Callbacks

    interface Callbacks {
        fun onTextInserted(input: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            onInputListener = activity as Callbacks
        } catch (e: ClassCastException) {
            Log.e(TAG, "onAttach", e)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)

        val view = View.inflate(context, R.layout.fragment_single_text_input, null)

        inputEditText = view.findViewById(R.id.text_input)

        inputEditText.apply {
            setText(arguments?.getString(ARG_VALUE))

            arguments?.getInt(ARG_HINT)?.let { hintResourceId ->
                hint = resources.getString(hintResourceId)
            }
        }

        builder.setView(view)

        builder.setTitle(arguments?.getInt(ARG_TITLE)!!)

        builder.setPositiveButton(R.string.confirm) { dialog, which ->
            onInputListener.onTextInserted(inputEditText.text.toString())
        }

        builder.setNegativeButton(R.string.cancel) { dialog, which ->
            dialog?.cancel()
        }

        return builder.create()
    }

    companion object {
        fun newInstance(titleResourceId: Int, hintResourceId: Int? = null, currentValue: String = ""): SingleTextInputDialogFragment {
            val args = Bundle().apply {
                putInt(ARG_TITLE, titleResourceId)
                hintResourceId?.let {
                    putInt(ARG_HINT, it)
                }
                putString(ARG_VALUE, currentValue)
            }

            return SingleTextInputDialogFragment().apply { arguments = args }
        }
    }
}