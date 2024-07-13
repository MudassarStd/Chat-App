package com.android.chatnappsummers.utils

import android.content.Context
import android.graphics.Point
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.android.chatnappsummers.R

object DialogUtils {

    fun showAttachmentsDialog(context: Context, anchorView: View, layoutResId: Int) {
        val dialogView = LayoutInflater.from(context).inflate(layoutResId, null)
        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        // Set dialog attributes
        val window = dialog.window
        val params = window?.attributes
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)

        params?.gravity = Gravity.TOP or Gravity.START
        params?.x = location[0]
        params?.y = location[1] + anchorView.height
        window?.attributes = params

        // Show the dialog
        dialog.show()
    }
}
