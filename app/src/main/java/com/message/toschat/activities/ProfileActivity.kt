package com.message.toschat.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.message.toschat.toschat.R
import com.message.toschat.services.SingleTon
import com.message.toschat.utils.Constance
import kotlinx.android.synthetic.main.activity_user.*


class ProfileActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance();
    var mGoogleApiClient: GoogleApiClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
        mGoogleApiClient?.connect()

        getCurrentUser()
        btn_logout.setOnClickListener {
            mAuth.signOut()
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback {
                val intent = Intent(applicationContext, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
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
