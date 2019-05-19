package com.example.myscope.manager.friend

import android.util.Log
import com.example.myscope.Response_DeleteFriend
import com.example.myscope.Response_SetFriend
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.RemoteDatabase
import java.util.*

class FriendManager : Observable() {

    //Singleton
    companion object {
        val instance: FriendManager by lazy { FriendManager() }
    }

    fun setFriendData(friend: Friend) {
        RemoteDatabase.instance.setDocument("Friend", friend.uid,
            "Status", friend.targetUID, friend) {
            Log.e("FriendManager", "setFriendData")
            when (it) {
                null -> notifyChanged(Response_SetFriend)
                else -> notifyChanged(ErrorMsg(it))
            }
        }
    }

    fun getFriendStatus(uid: String, targetUID: String) {
        RemoteDatabase.instance.getDocument("Friend", uid, "Status", targetUID) {
                msg, data ->
            if (!msg.isNullOrEmpty())
                notifyChanged(ErrorMsg(msg))
            else {
                data?.toObject(Friend::class.java)?.let {
                    notifyChanged(it)
                    Log.e("FriendManager", "getFriendData")
                } ?: run {
                    val friend = Friend(uid, targetUID , "", "", 0)
                    notifyChanged(friend)
                    Log.e("FriendManager", "noFriendData")
                }
            }
        }
    }

    fun getFriendList(uid: String) {
        RemoteDatabase.instance.queryDocumentListByEqualTo("Friend", uid, "Status", "status", 3, 10) {
                msg, list ->
            if (msg != null) {
                notifyChanged(ErrorMsg(msg))
            } else {
                val mList = arrayListOf<Friend?>()
                list?.listIterator()?.forEach {
                    mList.add(it.toObject(Friend::class.java))
                }
                notifyChanged(FriendList(mList.toTypedArray()))
                Log.e("FriendManager", "getFriendList")
            }
        }
    }

    fun getUnfriendList(uid: String) {
        RemoteDatabase.instance.queryDocumentListByLessThan("Friend", uid, "Status", "status", 3, 10) {
            msg, list ->
            if (msg != null) {
                notifyChanged(ErrorMsg(msg))
            } else {
                val mList = arrayListOf<Friend?>()
                list?.listIterator()?.forEach {
                    mList.add(it.toObject(Friend::class.java))
                }
                notifyChanged(UnfriendList(mList.toTypedArray()))
                Log.e("FriendManager", "getUnfriendList")
            }
        }
    }

    //Called by reject invitation, cancel invitation, delete friend
    fun deleteFriend(uid: String, targetUID: String) {
        RemoteDatabase.instance.deleteDocument("Friend", uid, "Status", targetUID) {
            Log.e("FriendManager", "deleteFriend")
            when (it) {
                null -> notifyChanged(Response_DeleteFriend)
                else -> notifyChanged(ErrorMsg(it))
            }
        }
    }

    private fun notifyChanged(res: Any?){
        setChanged()
        notifyObservers(res)
    }
}