package com.message.toschat.models

import com.google.gson.annotations.SerializedName

class Message {

    @SerializedName("sender")
    var sender: String? = ""
    @SerializedName("time")
    var time: String? = ""
    @SerializedName("title")
    var title: String = ""
    @SerializedName("type")
    var type: String = ""

    constructor() {}
    constructor(sender: String, time: String, title: String, type: String) {
        this.sender = sender
        this.time = time
        this.title = title
        this.type = type
    }

}


