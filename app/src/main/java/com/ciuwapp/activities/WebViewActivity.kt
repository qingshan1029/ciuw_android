package com.ciuwapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.ciuwapp.R
import kotlinx.android.synthetic.main.activity_web_view.*
import androidx.appcompat.widget.Toolbar

class WebViewActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // action bar
        val actionbar = supportActionBar
        actionbar!!.title = intent.getStringExtra("webTitle")
        actionbar.setDisplayHomeAsUpEnabled(true)


        val url = intent.getStringExtra("url")

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        webView.loadUrl(url)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
        else {
            super.onBackPressed()
        }

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
