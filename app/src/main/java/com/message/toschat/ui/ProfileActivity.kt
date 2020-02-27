package com.message.toschat.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.message.toschat.toschat.R
import com.message.toschat.network.SingleTon
import com.message.toschat.ui.signin.SignInActivity
import com.message.toschat.util.Constance
import kotlinx.android.synthetic.main.activity_user.*


class ProfileActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance();
    var googleSignInClient: GoogleSignInClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val googleSignInOptions : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(applicationContext, googleSignInOptions)


        getCurrentUser()
        btn_logout.setOnClickListener {
            auth.signOut()
            googleSignInClient!!.signOut().addOnSuccessListener {
                val intent = Intent(applicationContext, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }

//            if (AccessToken.getCurrentAccessToken() != null) {
//                GraphRequest(AccessToken.getCurrentAccessToken(), "me/permissions/", null, HttpMethod.DELETE, GraphRequest.Callback {
//                    AccessToken.setCurrentAccessToken(null)
//                    LoginManager.getInstance().logOut()
//                    finish()
//                    val intent = Intent(applicationContext, SignInActivity::class.java)
//                    intent.flags
//                    finish()
//                    startActivity(intent)
//
//                }).executeAsync()
//            }

        }
    }

    private fun getCurrentUser() {
        try {
            val user = SingleTon.getCurrentUser(this, Constance.SINGLE_USER)
            Glide.with(this)
                    .load(user?.userProfile)
                    .apply(RequestOptions().placeholder(R.drawable.ic_holder))
                    .into(img_profile)
            user_email.text = user?.userEmail
            user_name.text = user?.userName
        } catch (e: Exception) {

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}
