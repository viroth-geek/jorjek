package com.message.toschat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.message.toschat.toschat.R
import com.message.toschat.adapter.UserAdapter
import com.message.toschat.models.User
import com.message.toschat.utils.Constance
import kotlinx.android.synthetic.main.activity_main.*


inline fun <reified T> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}


class MainActivity : AppCompatActivity() {

    val users = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val mMessageReference = FirebaseDatabase.getInstance().getReference(Constance.SINGLE_USER)
        mMessageReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    snapshot.getValue(User::class.java)?.let(users::add)
                }
                userListRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                userListRecyclerView.adapter = UserAdapter(users, applicationContext, object : View.OnClickListener, UserAdapter.OnItemClickListener {
                    override fun onItemClick(user: User) {
                        val intent = Intent(applicationContext, ChatActivity::class.java)
                        intent.putExtra(Constance.STRING_USER_PACKAGE, user)
                        startActivity(intent)
                    }

                    override fun onClick(v: View?) {
                        Toast.makeText(applicationContext, "hid", Toast.LENGTH_SHORT).show()
                    }
                })
//                userListRecyclerView.adapter.notifyDataSetChanged()
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.actionProfile -> {
                startActivity<ProfileActivity>()
            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
