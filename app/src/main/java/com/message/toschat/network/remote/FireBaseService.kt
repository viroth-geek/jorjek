package com.message.toschat.network.remote

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.message.toschat.model.User
import com.message.toschat.util.Constance

class FireBaseService {

    fun getUserFromFireBase(): MutableLiveData<ArrayList<User>> {
        val users = MutableLiveData<ArrayList<User>>()
        val reference  = FirebaseDatabase.getInstance().getReference(Constance.SINGLE_USER)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshop: DataSnapshot) {
                println("aa ${dataSnapshop.value}")
//                for(snapshot in dataSnapshop.children) {
//                    snapshot.getValue(User::class.java)?.let(users.value!!::add)
//
//                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return users
    }
}