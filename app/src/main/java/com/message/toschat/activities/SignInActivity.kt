package com.message.toschat.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.message.toschat.toschat.R
import com.message.toschat.models.User
import com.message.toschat.services.SingleTon
import com.message.toschat.utils.Constance
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private val mAuth = FirebaseAuth.getInstance()
    private var mGoogleApiClient: GoogleApiClient? = null

    private val RC_SIGN_IN = 7
    val EXTRA_USER: String = "user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        setContentView(R.layout.activity_sign_in)
        getCurrentUser()

        btn_google.setOnClickListener {
            signIn()
        }
        //google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        //facebook
//        callbackManager = CallbackManager.Factory.create()
//        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
//            override fun onSuccess(result: LoginResult?) {
//                handleFacebookAccessToken(result!!.getAccessToken());
//
//            }
//
//            override fun onCancel() {
//            }
//
//            override fun onError(error: FacebookException?) {
//            }
//
//        });

    }


    private fun signIn() {
        val signInIntent: Intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun getCurrentUser() {
        val currentUser = mAuth.currentUser

        if (currentUser != null) {

            val user = User(currentUser.uid, currentUser?.displayName, currentUser?.photoUrl.toString(), currentUser?.email, currentUser?.providerId)
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user_package", user)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val result: GoogleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                handleSignInResult(result)

            } catch (e: ApiException) {
                Log.w("result", "Google sign in failed", e);
            }
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        Log.d("result", "handleSignInResult: " + result.isSuccess)
        if (result.isSuccess) {
            val account: GoogleSignInAccount? = result.signInAccount
            firebaseAuthWithGoogle(account)

        } else {
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        mAuth!!.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val getUser = mAuth!!.currentUser
                        Log.e("user", getUser!!.displayName)

                        val userReference = FirebaseDatabase.getInstance().getReference(Constance.SINGLE_USER)
                        val userId = getUser.uid
                        val user = User(getUser.uid, getUser.displayName.toString(), getUser.photoUrl.toString(), getUser.email.toString(), getUser.providerId)
                        userReference.child(userId.toString()).setValue(user).addOnCompleteListener {
                            SingleTon.save(this@SignInActivity, Constance.SINGLE_USER, user)
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("user_package", user)
                            intent.flags
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(applicationContext, "Credential Sign in : failed", Toast.LENGTH_SHORT).show()
                        Log.d("test", "failed")
                    }
                }
    }

    //hide status bar
    @SuppressLint("ObsoleteSdkInt")
    private fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= 16) {
            window.setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
            window.getDecorView().setSystemUiVisibility(3328);
        } else {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    //facebook
//    private fun handleFacebookAccessToken(accessToken: AccessToken) {
//        val credential: AuthCredential = FacebookAuthProvider.getCredential(accessToken.token)
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this) { p0 ->
//                    if (p0.isSuccessful) {
//                        Log.d("Test", "signInWithCredential:success")
//                        val facebookUser: FirebaseUser = mAuth.currentUser!!
//                        val reference = FirebaseDatabase.getInstance().getReference("User")
//                        val userId = facebookUser.uid
//                        val user = User(facebookUser.displayName.toString(), facebookUser.photoUrl.toString(), facebookUser.email.toString(), facebookUser.providerId)
//                        reference.child(userId.toString()).setValue(user).addOnCompleteListener {
//                            Toast.makeText(applicationContext, "Successfully", Toast.LENGTH_LONG).show()
//                            val intent = Intent(this, MainActivity::class.java)
//                            intent.putExtra("username", facebookUser.displayName)
//                            intent.putExtra("email", facebookUser.email)
//                            intent.putExtra("url", facebookUser.photoUrl.toString())
//                            Log.d("id", facebookUser.uid)
//                            intent.flags
//                            startActivity(intent)
//                        }
//
//                    } else {
//                        Log.d("Test", "signInWithCredential: unsuccessfully");
//
//                    }
//                }
//                .addOnFailureListener {
//                    Log.d("Test", "sign in failure");
//
//                }
//
//    }
}
