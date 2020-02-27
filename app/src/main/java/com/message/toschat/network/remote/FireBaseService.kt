package com.message.toschat.network.remote

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.message.toschat.model.User
import com.message.toschat.util.Constance

class FireBaseService {

    fun getUserFromFireBase() : ArrayList<User> {

        val users = ArrayList<User>()
        val reference  = FirebaseDatabase.getInstance().getReference(Constance.SINGLE_USER)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshop: DataSnapshot) {
                for(snapshot in dataSnapshop.children) {
                    snapshot.getValue(User::class.java)?.let(users::add)
                }
                Log.d("final_user", "${users.size}")
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
        return users
    }
}