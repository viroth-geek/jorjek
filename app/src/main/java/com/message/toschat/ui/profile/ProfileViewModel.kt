package com.message.toschat.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.message.toschat.ui.signin.SignInActivity

class ProfileViewModel: ViewModel() {

    val auth = FirebaseAuth.getInstance()

    fun signOut(googleSignInClient: GoogleSignInClient) {

    }

}