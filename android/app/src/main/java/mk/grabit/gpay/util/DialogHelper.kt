package mk.grabit.gpay.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.proceed_transaction_dialog.view.*
import mk.grabit.gpay.R

object DialogHelper {
    fun createCustomAlertDialog(
        context: Context,
        doneButtonListener: (AlertDialog) -> Unit,
        cancelButtonListener: (AlertDialog) -> Unit
    ): AlertDialog {

        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.proceed_transaction_dialog, null)

        val alertDialog = AlertDialog.Builder(context).apply {
            setView(dialogView)
            setCancelable(false)
        }.create()

        dialogView.proceed_button.setOnClickListener { doneButtonListener(alertDialog) }
        dialogView.cancel_button.setOnClickListener { cancelButtonListener(alertDialog) }

        return alertDialog
    }


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