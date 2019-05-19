package com.example.myscope.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.myscope.ImageLoader
import com.example.myscope.R
import com.example.myscope.manager.friend.Friend

class FriendAdapter(context: Context, friends: ArrayList<Friend>) :
    ArrayAdapter<Friend>(context, R.layout.item_friend, friends) {

    private lateinit var holder: ViewHolder

    private class ViewHolder(v: View) {
        val img_avatar: ImageView = v.findViewById(R.id.img_avatar)
        val tv_name: TextView = v.findViewById(R.id.tv_name)
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View? {
        val view: View
        val item = getItem(position)

        if(convertView == null){
            view = View.inflate(context, R.layout.item_friend,null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        ImageLoader.loadImage(holder.img_avatar, item.avatar)
        holder.tv_name.text = item.name

        return view
    }
}