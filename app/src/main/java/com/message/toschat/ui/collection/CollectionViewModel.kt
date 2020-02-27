package com.message.toschat.ui.collection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.message.toschat.model.User
import com.message.toschat.network.remote.FireBaseService

class CollectionViewModel : ViewModel() {

    private var googleSignInClient: GoogleSignInClient? = null
    private var users : MutableLiveData<ArrayList<User>> = MutableLiveData()
    private var finalUsers: MutableLiveData<ArrayList<User>> = MutableLiveData()
    private var fireBaseService: FireBaseService = FireBaseService()
    fun getUser() {
        val users  = fireBaseService.getUserFromFireBase()
//        println("users ${users.value!!.size}")
    }

}
