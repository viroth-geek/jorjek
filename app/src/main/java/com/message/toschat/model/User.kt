package com.message.toschat.model
import java.io.Serializable


class User : Serializable {
  var userId: String? = null
  var userName: String? = null
  var userProfile: String? = null
  var userEmail: String? = null
  private var userProviderId: String? = null

  constructor()

  constructor(userId : String?, userName: String?, userProfile: String?, userEmail: String?, userProviderId: String?) {
    this.userId = userId
    this.userName = userName
    this.userProfile = userProfile
    this.userEmail = userEmail
    this.userProviderId = userProviderId
  }

}
