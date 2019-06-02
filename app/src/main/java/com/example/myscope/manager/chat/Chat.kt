package com.example.myscope.manager.chat

data class Chat (val id: String = "",
                 val uid: String = "",
                 val avatar: String = "",
                 val name: String = "",
                 val msg: String = "",
                 val type: Int = 0)

data class ChatRoom (val id: String = "",
                     val targetUID: String = "",
                     val avatar: String = "",
                     val name: String = "",
                     val lastMsg: String = "",
                     val lastMsgType: Int = 0,
                     val lastTime: Long = 0)

data class ChatList(val list: Array<Chat?> = arrayOf())

data class ChatRoomList(val list: Array<ChatRoom?> = arrayOf())