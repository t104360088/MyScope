package com.example.myscope.manager.chat

import java.io.Serializable

data class Chat (val id: String = "",
                 val uid: String = "",
                 val avatar: String = "",
                 val name: String = "",
                 val msg: String = "",
                 val time: Long = 0,
                 val type: Int = 0)

data class ChatRoom (val id: String = "",
                     val uid: String = "",
                     val targetUID: String = "",
                     val avatar: String = "",
                     val name: String = "",
                     var lastMsg: String = "",
                     var lastMsgType: Int = 0,
                     var lastTime: Long = 0): Serializable

data class ChatList(val list: Array<Chat?> = arrayOf())

data class ChatRoomList(val list: Array<ChatRoom?> = arrayOf())