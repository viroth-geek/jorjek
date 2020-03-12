package com.message.toschat.ui.collection

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.message.toschat.model.User
import com.message.toschat.network.SingleTon
import com.message.toschat.util.Constance

class CollectionViewModel : ViewModel() {

    var users  =  ArrayList<User>()
    var finalUsers: MutableLiveData<ArrayList<User>> = MutableLiveData()


    fun getUser(refresh: Boolean = false, context: Context) {
        if(refresh) {
            finalUsers.value?.clear()
        }
        val currentUser = SingleTon.getCurrentUser(context, Constance.USER)

        val reference  = FirebaseDatabase.getInstance().getReference(Constance.USER)
        val userReference = FirebaseDatabase.getInstance().getReference(Constance.USER)
        val chatReference = FirebaseDatabase.getInstance().getReference(Constance.CHAT)

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshop: DataSnapshot) {

                for(snapshot in dataSnapshop.children) {
                    snapshot.getValue(User::class.java)?.let(users::add)
                }
                finalUsers.value = users

                Log.d("user", "value ${dataSnapshop.value}")

            }
            override fun onCancelled(dataBaseError: DatabaseError) {
                Log.d("user", "${dataBaseError.details} ")
                Log.d("user", "${dataBaseError.code} ")
                Log.d("user", "${dataBaseError.message} ")
            }
        })

        chatReference
                .orderByChild("users/${currentUser!!.userId}/userId")
                .equalTo(currentUser.userId)
                .addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.d("log_message", "$p0")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d("log_message", "${dataSnapshot}")
                userReference
                        .orderByChild("userId")
                        .equalTo("${dataSnapshot}")
            }

        })

    }
}
