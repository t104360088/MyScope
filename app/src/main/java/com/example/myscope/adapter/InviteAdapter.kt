package com.example.myscope.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.myscope.ImageLoader
import com.example.myscope.R
import com.example.myscope.manager.friend.Friend
import com.example.myscope.manager.friend.FriendManager
import kotlin.collections.ArrayList

class InviteAdapter(context: Context, friends: ArrayList<Friend>,
                    private val uid: String) :
    ArrayAdapter<Friend>(context, R.layout.item_invite, friends) {

    private lateinit var holder: ViewHolder

    private class ViewHolder(v: View) {
        val img_avatar: ImageView = v.findViewById(R.id.img_avatar)
        val tv_name: TextView = v.findViewById(R.id.tv_name)
        val tv_agree: TextView = v.findViewById(R.id.tv_agree)
        val tv_reject: TextView = v.findViewById(R.id.tv_reject)
        val tv_cancel: TextView = v.findViewById(R.id.tv_cancel)
    }

    override fun areAllItemsEnabled(): Boolean = false

    override fun isEnabled(position: Int): Boolean = false

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View? {
        val view: View
        val item = getItem(position)

        if(convertView == null){
            view = View.inflate(context, R.layout.item_invite,null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        ImageLoader.loadImage(holder.img_avatar, item.avatar)
        holder.tv_name.text = item.name
        setButtonVisible(item.status)
        setListen(position)

        return view
    }

    private fun setButtonVisible(status: Int) {
        holder.tv_agree.visibility = if (status == 2) View.VISIBLE else View.GONE
        holder.tv_reject.visibility = if (status == 2) View.VISIBLE else View.GONE
        holder.tv_cancel.visibility = if (status == 1) View.VISIBLE else View.GONE
    }

    private fun setListen(position: Int) {
        holder.tv_agree.setOnClickListener {
            val targetUID = getItem(position).targetUID
            val name = getItem(position).name
            val avatar = getItem(position).avatar
            val friend = Friend(uid, targetUID, name, avatar, 3)
            FriendManager.instance.setFriendData(friend)
            Log.e("InviteAdapter", "agree: ${getItem(position).name}")
        }
        holder.tv_reject.setOnClickListener {
            val targetUID = getItem(position).targetUID
            FriendManager.instance.deleteFriend(uid, targetUID)
            Log.e("InviteAdapter", "reject: ${getItem(position).name}")
        }
        holder.tv_cancel.setOnClickListener {
            val targetUID = getItem(position).targetUID
            FriendManager.instance.deleteFriend(uid, targetUID)
            Log.e("InviteAdapter", "cancel: ${getItem(position).name}")
        }
    }
}