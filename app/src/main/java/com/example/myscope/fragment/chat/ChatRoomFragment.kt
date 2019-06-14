package com.example.myscope.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.activity.MainActivity
import com.example.myscope.adapter.ChatRoomAdapter
import com.example.myscope.fragment.base.LazyLoadFragment
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.chat.ChatManager
import com.example.myscope.manager.chat.ChatRoom
import com.example.myscope.manager.chat.ChatRoomList
import com.example.myscope.manager.user.UserManager
import kotlinx.android.synthetic.main.fragment_friend.*
import java.util.*

class ChatRoomFragment : LazyLoadFragment() {
    private lateinit var adapter: ChatRoomAdapter
    private val room = arrayListOf<ChatRoom>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_room, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initListView()
    }

    private fun initListView() {
        UserManager.instance.getMyUser()?.let {
            if (!::adapter.isInitialized){
                adapter = ChatRoomAdapter(mActivity, room)
            }

            listView.adapter = adapter
            listView.setOnItemClickListener { parent, view, position, id ->
                val b = Bundle()
                b.putSerializable("Room", room[position])
                switchTo(ChatFragment(), b)
                (mActivity as MainActivity).showNavigationDrawer(false)
                (mActivity as MainActivity).showNavigationBottom(false)
            }
        }
    }

    override fun requestData() {
        UserManager.instance.getMyUser()?.let {
            ChatManager.instance.getChatRoomList(it.uid)
        }
    }

    override fun update(o: Observable?, arg: Any?) {

        when (arg) {
            is ChatRoomList -> {
                room.clear()
                for (i in arg.list) {
                    room.add(i!!)
                }
                adapter.notifyDataSetChanged()
            }
            is ErrorMsg -> {
                mActivity.runOnUiThread {
                    showSnackbar(arg.msg)
                }
            }
        }
    }
}