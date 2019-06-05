package com.example.myscope.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.adapter.ChatAdapter
import com.example.myscope.fragment.base.ObserverFragment
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.chat.Chat
import com.example.myscope.manager.chat.ChatList
import com.example.myscope.manager.chat.ChatManager
import com.example.myscope.manager.chat.ChatRoom
import com.example.myscope.manager.user.UserManager
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.*

class ChatFragment : ObserverFragment() {
    private lateinit var adapter: ChatAdapter
    private lateinit var room: ChatRoom
    private val chatList = arrayListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            room = it.get("Room") as ChatRoom
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Toast.makeText(mActivity, "聊天", Toast.LENGTH_SHORT).show()
        setActionBar()
        initListView()
        setListen()
        ChatManager.instance.getChatList(room.id)
    }

    private fun setActionBar(){
        mActivity.setTitle(room.name)
        mActivity.setBack()
    }

    private fun initListView() {
        UserManager.instance.getMyUser()?.let {
            if (!::adapter.isInitialized) {
                adapter = ChatAdapter(mActivity, chatList, it.uid, object : ChatAdapter.MsgListener {
                    override fun onMessage(position: Int) {
                    }

                    override fun onPhoto(position: Int) {
                    }
                })
            }

            listView.adapter = adapter
        }
    }

    private fun setListen() {
        tv_send.setOnClickListener {
            if (ed_msg.length() > 0) {
                UserManager.instance.getMyUser()?.let {
                    val id = ChatManager.instance.getAutoID(room.id)
                    val msg = ed_msg.text.toString()
                    val time = System.currentTimeMillis()
                    val chat = Chat(id, it.uid, it.avatar?:"", it.name?:"", msg, time, 0)
                    ChatManager.instance.setChatData(room.id, chat)

                    //Update chat room last message
                    room.lastMsg = msg
                    room.lastMsgType = 0
                    room.lastTime = time
                    ChatManager.instance.setChatRoomData(it.uid, room)

                    ed_msg.setText("")
                }
            }
        }
    }

    override fun update(o: Observable?, arg: Any?) {

        when (arg) {
            is ChatList -> {
                chatList.clear()
                for (i in arg.list) {
                    chatList.add(i!!)
                }

                mActivity.runOnUiThread {
                    adapter.notifyDataSetChanged()
                    listView.setSelection(chatList.size) //滾至最底，要再改善
                }
            }
            is ErrorMsg -> {
                mActivity.runOnUiThread {
                    showSnackbar(arg.msg)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ChatManager.instance.removeChatListen()
    }
}