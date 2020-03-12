package com.message.toschat.ui.collection

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.message.toschat.adapter.UserAdapter
import com.message.toschat.model.User
import com.message.toschat.network.SingleTon
import com.message.toschat.toschat.R
import com.message.toschat.toschat.databinding.ActivityMainBinding
import com.message.toschat.ui.chat.ChatActivity
import com.message.toschat.ui.profile.ProfileActivity
import com.message.toschat.ui.signin.SignInActivity
import com.message.toschat.util.Constance
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import kotlinx.android.synthetic.main.activity_main.*


class CollectionActivity : AppCompatActivity() {

    private var googleSignInClient: GoogleSignInClient? = null
    private var users = mutableListOf<User>()
    private var finalUsers = mutableListOf<User>()

    private lateinit var viewModel: CollectionViewModel
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(CollectionViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.setLifecycleOwner {
            this.lifecycle
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            toolbar.setTitleTextColor(getColor(R.color.colorWhite))
        }

        val googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.firebase_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(applicationContext, googleSignInOptions)
        setUpToolbar()
        refreshObserve(viewModel, binding)
        collectionObserve(viewModel, binding)

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

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        toolbar.title = "Collection"
    }

    private fun collectionObserve(viewModel: CollectionViewModel, binding: ActivityMainBinding) {

        val currentUser = SingleTon.getCurrentUser(this, Constance.SINGLE_USER)
        val users = ArrayList<User>()

        binding.progressBar.visibility = View.VISIBLE
        viewModel.finalUsers.observe(this, Observer { data ->
            binding.progressBar.visibility = View.GONE
            binding.refresh.isRefreshing = false

            data.forEach {
                if(it.userId != currentUser?.userId)
                    users.add(it)
            }
            binding.collectionRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = UserAdapter(users, applicationContext, object : UserAdapter.OnItemClickListener {
                    override fun onItemClick(user: User) {
                        val intent = Intent(applicationContext, ChatActivity::class.java)
                        intent.putExtra(Constance.STRING_USER_PACKAGE, user)
                        startActivity(intent)
                    }
                })
                adapter?.notifyDataSetChanged()
            }
        })
    }

    private fun refreshObserve(viewModel: CollectionViewModel, binding: ActivityMainBinding) {
        binding.refresh.setOnRefreshListener {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getUser(refresh = true)
        }
    }
}
