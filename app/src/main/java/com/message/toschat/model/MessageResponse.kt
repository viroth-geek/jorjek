package com.message.toschat.model

import com.google.gson.annotations.SerializedName

data class MessageResponse(
        @SerializedName("lastmessage")
        var lastMessage: LastMessage,
        @SerializedName("lasttimestamp")
        val lastTimeStamp: Long,
        @SerializedName("messages")
        val messages: ArrayList<ChildMessage>,
        @SerializedName("users")
        val user: ArrayList<MessageOwner>
)