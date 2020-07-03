package com.futysh.fyfm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mAlertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun showErrorNotification(message: String) {
        if (mAlertDialog == null || !mAlertDialog!!.isShowing) {
            val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)

            val view =
                LayoutInflater.from(this).inflate(R.layout.error_notification_layout, null)
            dialogBuilder.setView(view)
            mAlertDialog = dialogBuilder.create()

            view.findViewById<TextView>(R.id.error_description_text).text = message
            view.findViewById<ImageView>(R.id.dismiss_image).setOnClickListener {
                mAlertDialog?.dismiss()
            }
            val okButton = view.findViewById<Button>(R.id.ok_button)
            view.findViewById<Button>(R.id.cancel_button).visibility = View.GONE

            okButton.setOnClickListener {
                mAlertDialog?.dismiss()
            }

            mAlertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mAlertDialog?.show()
        }
    }
}
