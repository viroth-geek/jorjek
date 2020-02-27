package com.message.toschat.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.message.toschat.adapter.UserAdapter
import com.message.toschat.model.User
import com.message.toschat.network.SingleTon
import com.message.toschat.toschat.R
import com.message.toschat.ui.signin.SignInActivity
import com.message.toschat.util.Constance
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var googleSignInClient: GoogleSignInClient? = null
    private var users = mutableListOf<User>()
    private var finalUsers = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)

        init()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.actionProfile -> {
                val intent  =  Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            else -> {
                FirebaseAuth.getInstance().signOut()
                googleSignInClient!!.signOut()
                val intent  =  Intent(this, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun init() {
        val googleSignInOptions : GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(applicationContext, googleSignInOptions)

        val mMessageReference = FirebaseDatabase.getInstance().getReference(Constance.SINGLE_USER)
        mMessageReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.getValue(User::class.java)?.let(users::add)
                }

                val currentUser = SingleTon.getCurrentUser(applicationContext, Constance.SINGLE_USER)
                users.forEach {
                    if(it.userId != currentUser!!.userId)
                        finalUsers.add(it)
                }
//                userListRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
//                userListRecyclerView.adapter = UserAdapter(finalUsers, applicationContext, object : View.OnClickListener, UserAdapter.OnItemClickListener {
//                    override fun onItemClick(user: User) {
//                        val intent = Intent(applicationContext, ChatActivity::class.java)
//                        intent.putExtra(Constance.STRING_USER_PACKAGE, user)
//                        startActivity(intent)
//                    }
//                    override fun onClick(v: View?) {
//                        Toast.makeText(applicationContext, "hid", Toast.LENGTH_SHORT).show()
//                    }
//                })
//                userListRecyclerView.adapter.notifyDataSetChanged()
            }

        })

    }
}
