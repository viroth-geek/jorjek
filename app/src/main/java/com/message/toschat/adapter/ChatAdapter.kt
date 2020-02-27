package com.message.toschat.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.message.toschat.toschat.R
import com.message.toschat.model.Message
import com.message.toschat.network.SingleTon
import com.message.toschat.util.Constance
import java.util.*

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2
    var context: Context? = null;
    var messageList: ArrayList<Message> = arrayListOf()

    constructor()
    constructor(messageList: ArrayList<Message>, context: Context) {
        this.messageList = messageList
        this.context = context;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            SendMessageHolder(LayoutInflater.from(context).inflate(R.layout.layout_send_message, parent, false))
        } else {
            ReceiveMessageHolder(LayoutInflater.from(context).inflate(R.layout.layout_recieve_message, parent, false))
        }
    }

    override fun getItemCount(): Int {

        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        messageList.reverse()
        val message = messageList[position]
        if (message.sender.equals(SingleTon.getCurrentUser(context!!, Constance.SINGLE_USER)?.userId))
            return VIEW_TYPE_MESSAGE_SENT
        else {
            return VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val userMessage = messageList[position]
        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> {
                (holder as SendMessageHolder).bind(userMessage)
            }
            VIEW_TYPE_MESSAGE_RECEIVED -> {
                (holder as ReceiveMessageHolder).bind(userMessage)
            }
            else -> {

            }
        }
    }

    private class ReceiveMessageHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val titleReceiver = itemView?.findViewById<TextView>(R.id.titleReceiver)
        val timeReceiver = itemView?.findViewById<TextView>(R.id.timeReceiver)

        fun bind(message: Message) {
            titleReceiver?.text = message.title
            timeReceiver?.text = message.time
        }
    }

    private class SendMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var titleSender = itemView.findViewById<TextView>(R.id.titleSender)!!
        var timeSender = itemView.findViewById<TextView>(R.id.timeSender)!!

        fun bind(message: Message) {
            titleSender.text = message.title
            timeSender.text = message.time
        }
    }

    fun addItem(subject: Message) {
        messageList.add(subject)
        val newMsgPosition = messageList.size - 1
        ChatAdapter().notifyItemInserted(newMsgPosition)
    }
}

