package online.kozubek.czoleczko

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AddQuestionPackageFragment : DialogFragment() {
    interface Callbacks {
        fun onPackageNameInserted(name: String)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val builder = AlertDialog.Builder(context!!)

        val view = inflater.inflate(R.layout.fragment_add_question_package, container, false)

        builder.setView(view)

        builder.setPositiveButton("ok") { dialog, which ->
            Log.i("DIALOG", "Wybrano nazwe paczuszki")
        }

        builder.setNegativeButton("anuluj") { dialog, which ->
            dialog?.cancel()
        }

        builder.create()
        return builder.
    }
}