package com.message.toschat.model

import com.google.gson.annotations.SerializedName

data class Message(
        @SerializedName("data")
        val data: String,
        @SerializedName("type")
        val type: Int
)

data class ChildMessage (
        @SerializedName("userid")
        val userId: Int,
        @SerializedName("message")
        val message: Message
)
