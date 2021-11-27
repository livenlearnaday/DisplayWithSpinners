package io.github.livenlearnaday.displaywithspinners.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

object MessageUtils {
    @JvmStatic
    fun showAlertDialog(
        context: Context?,
        alertTitle: String?,
        alertMessage: String?
    ) {
        if (alertMessage == null || alertMessage == "") {
            val dialog = AlertDialog.Builder(
                context!!
            )
                .setTitle(alertTitle)
                .setNegativeButton("close", null)
                .create()
            dialog.show()
        } else if (alertTitle == null || alertTitle == "") {
            val dialog = AlertDialog.Builder(
                context!!
            )
                .setMessage(alertMessage)
                .setNegativeButton("close", null)
                .create()
            dialog.show()
        } else {
            val dialog = AlertDialog.Builder(
                context!!
            )
                .setTitle(alertTitle)
                .setMessage(alertMessage)
                .setNegativeButton("close", null)
                .create()
            dialog.show()
        }
    }
}