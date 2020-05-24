package com.ciuwapp.activities

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import com.ciuwapp.R

class VerifyCodeActivity(Context: Context): Dialog(Context) {
    var textTitle: TextView? = null
    var editText: EditText? = null
    var okButton: Button? = null
    var cancelButton: Button? = null
    init {
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_verify_code)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        editText = findViewById(R.id.et_verify_code)
        textTitle = findViewById(R.id.tv_verify_title)
        okButton = findViewById(R.id.btn_send)
        cancelButton = findViewById(R.id.btn_verify_cancel)
        cancelButton?.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun setButtonColor(color: Int):VerifyCodeActivity{
        if (okButton != null) {
            okButton!!.backgroundTintList = ContextCompat.getColorStateList(okButton!!.context, color)
        }
        if (cancelButton != null) {
            cancelButton!!.backgroundTintList = ContextCompat.getColorStateList(cancelButton!!.context, color)
        }
        return this
    }

    fun setTitleText(text: String): VerifyCodeActivity{
        Log.d("Dialog", "Set title to ${text}")
        textTitle?.text = text
        textTitle?.visibility = View.VISIBLE
        if (textTitle != null) {
            textTitle!!.text = text
        } else {
            Log.d("Dialog", "THe text title is actually null")
        }
        return this
    }

    fun setOkClickListener(listener: View.OnClickListener): VerifyCodeActivity{
        okButton?.setOnClickListener(listener)
        return this
    }

    fun setCancelClickListener(listener: View.OnClickListener): VerifyCodeActivity{
        cancelButton?.setOnClickListener(listener)
        return this
    }
}
