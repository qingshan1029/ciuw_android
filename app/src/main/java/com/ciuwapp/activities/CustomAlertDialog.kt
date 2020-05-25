package com.ciuwapp.activities
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.ciuwapp.R


class CustomAlertDialog(Context: Context): Dialog(Context) {
    var textTitle: TextView? = null
    var textBody: TextView? = null
    var confirmButton: Button? = null
    var circularWrapper: RelativeLayout? = null
    var dynamicButonWrapper: LinearLayout? = null
    var infoIconView: ImageView? = null
    init {
        setCancelable(false)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_alert_dialog)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        circularWrapper = findViewById(R.id.circularWrapper) as RelativeLayout
        dynamicButonWrapper = findViewById(R.id.dynamicButonWrapper) as LinearLayout
        infoIconView = findViewById(R.id.infoIconView) as ImageView
        textTitle = findViewById(R.id.textTitle) as TextView
        textBody = findViewById(R.id.textBody) as TextView
        confirmButton = findViewById(R.id.confirmButton)
        confirmButton?.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Dialog", "On Create")
        super.onCreate(savedInstanceState)
    }
    fun confirmButtonColor(color: Int):CustomAlertDialog{
        if (confirmButton != null) {
            confirmButton!!.backgroundTintList = ContextCompat.getColorStateList(confirmButton!!.context, color)
        }
        return this
    }

    fun setIcon(icon: Int): CustomAlertDialog{
        infoIconView?.setImageDrawable(context.getDrawable(icon))
        return this
    }

    fun setTitleText(text: String): CustomAlertDialog{
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

    fun setContentText(text: String): CustomAlertDialog{
        Log.d("Dialog", "Set content text to ${text}")
        textBody?.visibility = View.VISIBLE
        if (textBody != null) {
            var htmlString: Spanned?
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                htmlString = Html.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
            else{
                htmlString = Html.fromHtml(text)
            }
            textBody?.text = htmlString

        } else {
            Log.d("Dialog", "THe text body is actually null")
        }
        return this
    }
    fun setConfirmText(text: String): CustomAlertDialog{
        confirmButton?.setText(text)
        return this
    }
    fun showNotice(title: String, subtitle: String): CustomAlertDialog{
        setTitleText(title)
        setContentText(subtitle)
        confirmButtonColor(R.color.noticeColor)
        circularWrapper?.background = context.getDrawable(R.drawable.ic_launcher_background)
        infoIconView?.setImageDrawable(context.getDrawable(R.drawable.ic_infoicon))
        return this
    }

    fun showWarning(title: String, subtitle: String): CustomAlertDialog{
        setTitleText(title)
        setContentText(subtitle)
        setConfirmText("Reset")
        confirmButtonColor(R.color.warningColor)
        circularWrapper?.background = context.getDrawable(R.drawable.info_circular_warning_background)
        infoIconView?.imageTintList = ContextCompat.getColorStateList(infoIconView!!.context, R.color.darkTextColor)
        confirmButton?.setTextColor(ContextCompat.getColor(textBody!!.context, R.color.whiteTextColor))
        return this
    }
    fun showDownload(title: String, subtitle: String): CustomAlertDialog{
        setTitleText(title)
        setContentText(subtitle)
        infoIconView?.setImageDrawable(context.getDrawable(R.drawable.ic_infoicon))
        return this
    }

    fun setConfirmButtonListener(listener: View.OnClickListener): CustomAlertDialog{
        confirmButton?.setOnClickListener(listener)
        return this
    }
    fun addButton(title: String, listener: View.OnClickListener): CustomAlertDialog{
        val button = Button(context)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.leftMargin = context.resources.getDimension(R.dimen.cell_spacing).toInt()
        layoutParams.rightMargin = context.resources.getDimension(R.dimen.cell_spacing).toInt()
        button.layoutParams = layoutParams
        button.isAllCaps = false
//        button.typeface = ResourcesCompat.getFont(context, R.font.lato_regular);
        button.text = title
        button.setOnClickListener(listener)
        button.backgroundTintList = ContextCompat.getColorStateList(button.context, R.color.colorPrimary)
        button.setTextColor(Color.WHITE)
        dynamicButonWrapper?.addView(button)
        return this
    }
    fun addBackButton(title: String, listener: View.OnClickListener): CustomAlertDialog{
        val button = Button(context)
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.leftMargin = context.resources.getDimension(R.dimen.cell_spacing).toInt()
        layoutParams.rightMargin = context.resources.getDimension(R.dimen.cell_spacing).toInt()
        button.layoutParams = layoutParams
        button.isAllCaps = false
//        button.typeface = ResourcesCompat.getFont(context, R.font.lato_regular);
        button.text = title
        button.setOnClickListener(listener)
        button.backgroundTintList = ContextCompat.getColorStateList(button.context, R.color.noticeColor)
        button.setTextColor(Color.WHITE)
        dynamicButonWrapper?.addView(button)
        return this
    }
}
