package com.message.toschat.network

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.message.toschat.model.User

object SingleTon {

        private fun sharePreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences("user", Context.MODE_PRIVATE)
        }

        fun save(context: Context, key: String, user: User) {

            val editor = sharePreferences(context).edit()
            val gson = Gson()
            val json = gson.toJson(user)
            editor.putString(key, json)
            editor.apply()
        }

        fun getCurrentUser(context: Context, key: String): User? {
            val gson = Gson()
            val json = sharePreferences(context).getString(key, null)
            return gson.fromJson(json, User::class.java)
        }

        fun saveFireBaseSession(context: Context, key: String, id: String) {
            val editor = sharePreferences(context).edit()
            editor.putString(key, id)
            editor.apply()
        }

        fun getFireBaseSession(context: Context, key: String): String? {
            return sharePreferences(context).getString(key, null);
        }

        fun deleteFireBaseSession(context: Context, key: String) {
            val editor = sharePreferences(context).edit()
            editor.remove(key).apply()
        }
}
