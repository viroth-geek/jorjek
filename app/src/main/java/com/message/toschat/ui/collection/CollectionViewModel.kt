package com.message.toschat.ui.collection

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.message.toschat.model.User
import com.message.toschat.util.Constance

class CollectionViewModel : ViewModel() {

    var users  =  ArrayList<User>()
    var finalUsers: MutableLiveData<ArrayList<User>> = MutableLiveData()

    init {
        getUser()
    }

    fun getUser(refresh: Boolean = false) {
        if(refresh) {
            finalUsers.value?.clear()
        }
        val reference  = FirebaseDatabase.getInstance().getReference(Constance.SINGLE_USER)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshop: DataSnapshot) {
                for(snapshot in dataSnapshop.children) {
                    snapshot.getValue(User::class.java)?.let(users::add)
                }
                finalUsers.value = users
            }
            override fun onCancelled(dataBaseError: DatabaseError) {
                Log.d("user", "${dataBaseError.details} ")
                Log.d("user", "${dataBaseError.code} ")
                Log.d("user", "${dataBaseError.message} ")
            }
        })
    }
}
