package com.example.myscope.manager.friend

data class Friend(val uid: String = "",
                  val targetUID: String = "",
                  val name: String = "",
                  val avatar: String = "",
                  val status: Int = -1)

data class FriendList(val list: Array<Friend?> = arrayOf())

data class UnfriendList(val list: Array<Friend?> = arrayOf())