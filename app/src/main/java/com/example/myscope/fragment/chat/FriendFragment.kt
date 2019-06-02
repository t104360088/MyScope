package com.example.myscope.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.Response_DeleteFriend
import com.example.myscope.adapter.FriendAdapter
import com.example.myscope.fragment.base.LazyLoadFragment
import com.example.myscope.manager.DialogManager
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.chat.ChatManager
import com.example.myscope.manager.friend.Friend
import com.example.myscope.manager.friend.FriendList
import com.example.myscope.manager.friend.FriendManager
import com.example.myscope.manager.user.UserManager
import kotlinx.android.synthetic.main.fragment_friend.*
import java.util.*

class FriendFragment : LazyLoadFragment() {
    private lateinit var adapter: FriendAdapter
    private val friends = arrayListOf<Friend>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Toast.makeText(mActivity, "好友列表", Toast.LENGTH_SHORT).show()
        initListView()
    }

    private fun initListView() {
        UserManager.instance.getMyUser()?.let {
            if (!::adapter.isInitialized){
                adapter = FriendAdapter(mActivity, friends)
            }

            listView.adapter = adapter
            listView.setOnItemLongClickListener { parent, view, position, id ->
                val option = arrayOf("刪除好友")
                DialogManager.instance.showList(mActivity, option)?.setOnItemClickListener { _, _, p, _ ->
                    DialogManager.instance.dismissDialog()
                    when (p) {
                        0 -> {
                            val msg = "確定要刪除此好友嗎?"
                            DialogManager.instance.showMessage(mActivity, msg, true)?.
                                setOnClickListener { v ->
                                    DialogManager.instance.dismissDialog()
                                    FriendManager.instance.deleteFriend(it.uid, friends[position].targetUID)
                                    ChatManager.instance.deleteChatRoom(it.uid, friends[position].targetUID)
                            }
                        }
                    }
                }
                return@setOnItemLongClickListener true
            }
        }
    }

    override fun requestData() {
        UserManager.instance.getMyUser()?.let {
            FriendManager.instance.getFriendList(it.uid)
        }
    }

    override fun update(o: Observable?, arg: Any?) {

        when (arg) {
            is FriendList -> {
                friends.clear()
                for (i in arg.list) {
                    friends.add(i!!)
                }
                adapter.notifyDataSetChanged()
            }
            is ErrorMsg -> {
                mActivity.runOnUiThread {
                    showSnackbar(arg.msg)
                }
            }
            Response_DeleteFriend -> {
                UserManager.instance.getMyUser()?.let {
                    FriendManager.instance.getFriendList(it.uid)
                }
            }
        }
    }
}