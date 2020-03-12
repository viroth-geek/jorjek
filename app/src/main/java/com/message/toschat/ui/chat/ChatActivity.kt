package com.message.toschat.ui.chat

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import com.message.toschat.toschat.R
//import com.message.toschat.adapter.ChatAdapter
import com.message.toschat.model.*
import com.message.toschat.network.SingleTon
import com.message.toschat.toschat.databinding.ActivityStartChatBinding
import com.message.toschat.util.Constance
import com.message.toschat.util.Util
import kotlinx.android.synthetic.main.activity_start_chat.*
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private var fireBaseReference = FirebaseDatabase.getInstance()
    private var childEventListener: ValueEventListener? = null
    var keySnapshot: String? = ""
    val mMessageReference = fireBaseReference.getReference(Constance.STRING_CHAT)
    var partner: User? = null
    var you: User? = null
    val conversations = ArrayList<Message>()

    private lateinit var viewModel: ChatViewModel
    private lateinit var binding: ActivityStartChatBinding


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_chat)

        partner = intent.getSerializableExtra(Constance.STRING_USER_PACKAGE) as? User
        you = SingleTon.getCurrentUser(this@ChatActivity, Constance.SINGLE_USER)

        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.title = ""
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            try {
                if (SingleTon.getFireBaseSession(this, Constance.STRING_FIRE_BASE_SESSION) != null) {
                    SingleTon.deleteFireBaseSession(this, Constance.STRING_FIRE_BASE_SESSION)
                }
            } catch (e: Exception) {
                println(e)
            }
            finish()
        }

        setUser()
        chatRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)

        displayChatMessage()

//        chatRecyclerView.adapter = ChatAdapter(conversations, this)

        sendButton.setOnClickListener {

            val user: User? = intent.getSerializableExtra(Constance.STRING_USER_PACKAGE) as? User
            val posterId = user?.userId
            val posterName = user?.userName
            val type = "text"
            val time = "4:34"

            val reference = FirebaseDatabase.getInstance().getReference(Constance.STRING_CHAT)
//            val messagePackage = MessageResponse(SingleTon.getCurrentUser(this, Constance.SINGLE_USER)?.userId.toString(), time, messageEditText.text.toString(), type)
            val lastMessage = LastMessage("hello", "-dkfjkdjf", 1)
            val message = Message("Hello", 1)
            val childMessage = ChildMessage(1, message)
            val messages: ArrayList<ChildMessage>  = ArrayList()
            messages.add(childMessage)
            val messageOwner = MessageOwner("-dfnkdnfk", 1)
            val users: ArrayList<MessageOwner> = ArrayList()
            users.add(messageOwner)
            val messagePackage = MessageResponse(lastMessage = lastMessage, lastTimeStamp = 2329389239283, messages = messages, user = users)
            val key = reference.push().key.toString()

            val fireBaseSession = SingleTon.getFireBaseSession(this, Constance.STRING_FIRE_BASE_SESSION)
            if (fireBaseSession != null) {

                val parts = fireBaseSession.split("-")
                val part1 = parts[0]
                val part2 = parts[1]
                if (you?.userId == part1 && partner?.userId == part2 || you?.userId == part2 && partner?.userId == part1) {
                    reference.child("$part1-$part2").child("/${Constance.STRING_CONVERSATION}").child(key).setValue(messagePackage)
                    messageEditText.setText("")
                }
            } else {
                reference.child(partner?.userId + "-" + SingleTon.getCurrentUser(this, Constance.SINGLE_USER)?.userId).child("/${Constance.STRING_CONVERSATION}").child(key).setValue(messagePackage)
            }

        }
    }

    private fun displayChatMessage() {
        if (childEventListener == null) {
            childEventListener = object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    try {
                        for (snapshot in dataSnapshot.children) {
                            keySnapshot = snapshot.key
                            if (snapshot != null) {
                                SingleTon.saveFireBaseSession(this@ChatActivity, Constance.STRING_FIRE_BASE_SESSION, keySnapshot.toString())
                            } else {
                                SingleTon.saveFireBaseSession(this@ChatActivity, Constance.STRING_FIRE_BASE_SESSION, you?.userId + "-" + partner?.userId)
                            }

                            val parts = keySnapshot?.split("-")
                            val part1 = parts?.get(0)
                            val part2 = parts?.get(1)

                            if (you?.userId == part1 && partner?.userId == part2 || you?.userId == part2 && partner?.userId == part1) {
                                val listenToChild = object : ChildEventListener {
                                    override fun onCancelled(p0: DatabaseError) {

                                    }
                                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {

                                    }
                                    override fun onChildChanged(subSnapShot: DataSnapshot, p1: String?) {
                                        var message = subSnapShot.getValue(MessageResponse::class.java)
                                            Log.d("TAG", "${message.toString()}")
//                                        val contentSnapshot = snapshot.child("/conversations")
//                                        val matchSnapShot = contentSnapshot.children
//                                        for (match in matchSnapShot) {
//                                            val subCons = match.getValue(Message::class.java)
//                                            conversations.add(subCons!!)
//                                            conversations.reverse()
//                                        }
//                                        chatRecyclerView.adapter = ChatAdapter(conversations, this@ChatActivity)
                                    }

                                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
//                                        val conversations = ArrayList<Message>()
//                                        val contentSnapshot = snapshot.child("/conversations")
//                                        val matchSnapShot = contentSnapshot.children
//                                        for (match in matchSnapShot) {
//                                            val subCons = match.getValue(Message::class.java)
//                                            conversations.add(subCons!!)
//                                        }
//                                        chatRecyclerView.adapter = ChatAdapter(conversations, this@ChatActivity)
                                    }

                                    override fun onChildRemoved(p0: DataSnapshot) {

                                    }
                                }
                                mMessageReference.child(keySnapshot.toString()).child(Constance.STRING_CONVERSATION).addChildEventListener(listenToChild)
                            }
                        }
                    } catch (e: Exception) {
                        textViewStartToChat.visibility = View.VISIBLE
                    }
                }
            }
            mMessageReference.addListenerForSingleValueEvent(childEventListener as ValueEventListener)
        }
    }

    private fun setUser() {

        try {
            Glide.with(this)
                    .load(partner?.userProfile)
                    .apply(RequestOptions().placeholder(R.drawable.ic_holder))
                    .into(user_image)
            user_name.text = partner?.userName
        } catch (e: Exception) {
            Util.Toast(this, "error set image")
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        try {
            if (SingleTon.getFireBaseSession(this, Constance.STRING_FIRE_BASE_SESSION) != null) {
                SingleTon.deleteFireBaseSession(this, Constance.STRING_FIRE_BASE_SESSION)
            }
        } catch (e: Exception) {
        }
    }
}
