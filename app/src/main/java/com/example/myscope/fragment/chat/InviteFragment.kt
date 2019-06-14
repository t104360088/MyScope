package com.example.myscope.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.Response_DeleteFriend
import com.example.myscope.Response_SetFriend
import com.example.myscope.adapter.InviteAdapter
import com.example.myscope.fragment.base.LazyLoadFragment
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.friend.Friend
import com.example.myscope.manager.friend.FriendManager
import com.example.myscope.manager.friend.UnfriendList
import com.example.myscope.manager.user.UserManager
import kotlinx.android.synthetic.main.fragment_invite.*
import java.util.*

class InviteFragment : LazyLoadFragment() {
    private lateinit var adapter: InviteAdapter
    private val friends = arrayListOf<Friend>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_invite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initListView()
    }

    private fun initListView() {
        UserManager.instance.getMyUser()?.let {
            if (!::adapter.isInitialized){
                adapter = InviteAdapter(mActivity, friends, it.uid)
            }

            listView.adapter = adapter
        }
    }

    override fun requestData() {
        UserManager.instance.getMyUser()?.let {
            FriendManager.instance.getUnfriendList(it.uid)
        }
    }

    override fun update(o: Observable?, arg: Any?) {

        when (arg) {
            is UnfriendList -> {
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
            Response_SetFriend -> {
                UserManager.instance.getMyUser()?.let {
                    FriendManager.instance.getUnfriendList(it.uid)
                }
            }
            Response_DeleteFriend -> {
                UserManager.instance.getMyUser()?.let {
                    FriendManager.instance.getUnfriendList(it.uid)
                }
            }
        }
    }
}