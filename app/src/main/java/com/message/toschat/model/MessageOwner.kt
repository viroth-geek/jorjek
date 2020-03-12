package com.message.toschat.model

import com.google.gson.annotations.SerializedName

data class MessageOwner (
        @SerializedName("lastseen")
        val lastSeen: String,
        @SerializedName("userid")
        val userId: Int
)