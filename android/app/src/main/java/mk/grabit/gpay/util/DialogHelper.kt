package mk.grabit.gpay.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.transaction_error_dialog.view.*
import mk.grabit.gpay.R

object DialogHelper {

    fun createProgressDialog(
        context: Context?,
        messageText: String?
    ): AlertDialog? {
        val view =
            View.inflate(context, R.layout.progress_layout, null)
        val progressText = view.findViewById<TextView>(R.id.progress_layout_text)
        progressText.text = messageText
        return AlertDialog.Builder(context!!)
            .setCancelable(false)
            .setView(view)
            .create()
    }
}