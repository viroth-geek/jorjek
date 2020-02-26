package com.message.toschat.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.message.toschat.toschat.R

class SplashActivity : AppCompatActivity() {

    private var mDelayHandler: Handler? = null
    private val DELAY: Long = 3000 //3 seconds

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {

            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        hideStatusBar()

        //Initialize the Handler
        mDelayHandler = Handler()
        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, DELAY)

    }

    public override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun hideStatusBar(){
        if (Build.VERSION.SDK_INT >= 16) {
            window.setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            window.decorView.systemUiVisibility = 3328;
        }else{
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}