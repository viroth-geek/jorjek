package com.message.toschat.utils

import android.content.Context


object Util {
    fun Toast(context: Context, message: String) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    fun Log(tag: String, message: String) {
        android.util.Log.d(tag, message)
    }
}
