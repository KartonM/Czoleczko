package online.kozubek.czoleczko

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import javax.security.auth.callback.Callback

private const val TAG = "ADD_PACKAGE_DIALOG"

class AddQuestionPackageFragment : DialogFragment() {

    private lateinit var packageNameEditText: EditText
    private lateinit var onInputListener: Callbacks

    interface Callbacks {
        fun onPackageNameInserted(name: String)
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

        val view = View.inflate(context, R.layout.fragment_add_question_package, null)

        packageNameEditText = view.findViewById(R.id.package_name_input)

        builder.setView(view)

        builder.setTitle(R.string.insert_package_name)

        builder.setPositiveButton(R.string.confirm) { dialog, which ->
            onInputListener.onPackageNameInserted(packageNameEditText.text.toString())
        }

        builder.setNegativeButton(R.string.cancel) { dialog, which ->
            dialog?.cancel()
        }

        return builder.create()
    }

}