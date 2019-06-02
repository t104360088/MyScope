package com.example.myscope.manager.chat

import android.util.Log
import com.example.myscope.Response_DeleteChatRoom
import com.example.myscope.Response_SetMessage
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.RemoteDatabase
import java.util.*

class ChatManager : Observable() {

    //Singleton
    companion object {
        val instance: ChatManager by lazy { ChatManager() }
    }

    fun getAutoID(roomID: String): String {
        return RemoteDatabase.instance.getAutoID("ChatMessage", roomID, "Message")
    }

    fun setChatData(roomID: String, chat: Chat) {
        RemoteDatabase.instance.setDocument("Chat", roomID,
            "Message", chat.id, chat) {
            Log.e("ChatManager", "setChatData")
            when (it) {
                null -> notifyChanged(Response_SetMessage)
                else -> notifyChanged(ErrorMsg(it))
            }
        }
    }

    fun getChatList(roomID: String) {
        RemoteDatabase.instance.getDocumentList("Chat", roomID, "Message", 10) {
                msg, list ->
            if (msg != null) {
                notifyChanged(ErrorMsg(msg))
            } else {
                val mList = arrayListOf<Chat?>()
                list?.listIterator()?.forEach {
                    mList.add(it.toObject(Chat::class.java))
                }
                notifyChanged(ChatList(mList.toTypedArray()))
                Log.e("ChatManager", "getChatList")
            }
        }
    }

    fun getChatRoomList(uid: String) {
        RemoteDatabase.instance.getDocumentList("ChatRoom", uid, "Setting", 10) {
                msg, list ->
            if (msg != null) {
                notifyChanged(ErrorMsg(msg))
            } else {
                val mList = arrayListOf<ChatRoom?>()
                list?.listIterator()?.forEach {
                    mList.add(it.toObject(ChatRoom::class.java))
                }
                notifyChanged(ChatRoomList(mList.toTypedArray()))
                Log.e("ChatManager", "getChatRoomList")
            }
        }
    }

    fun deleteChatRoom(uid: String, targetUID: String) {
        RemoteDatabase.instance.deleteDocument("ChatRoom", uid, "Setting", targetUID) {
            Log.e("ChatManager", "deleteChatRoom")
            when (it) {
                null -> notifyChanged(Response_DeleteChatRoom)
                else -> notifyChanged(ErrorMsg(it))
            }
        }
    }

    private fun notifyChanged(res: Any?){
        setChanged()
        notifyObservers(res)
    }
}