package com.example.myscope.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.myscope.ImageLoader
import com.example.myscope.R
import com.example.myscope.manager.chat.ChatRoom
import java.util.*
import kotlin.collections.ArrayList

class ChatRoomAdapter(context: Context, room: ArrayList<ChatRoom>) :
    ArrayAdapter<ChatRoom>(context, R.layout.item_chat_room, room) {

    private lateinit var holder: ViewHolder
    private val c: Calendar = Calendar.getInstance()

    private class ViewHolder(v: View) {
        val img_avatar: ImageView = v.findViewById(R.id.img_avatar)
        val tv_title: TextView = v.findViewById(R.id.tv_title)
        val tv_time: TextView = v.findViewById(R.id.tv_time)
        val tv_last_msg: TextView = v.findViewById(R.id.tv_last_msg)
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View? {
        val view: View
        val item = getItem(position)

        if(convertView == null){
            view = View.inflate(context, R.layout.item_chat_room,null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        ImageLoader.loadImage(holder.img_avatar, item.avatar)
        holder.tv_title.text = item.name
        holder.tv_last_msg.text = item.lastMsg

        c.timeInMillis = item.lastTime

        //等待改善
        holder.tv_time.text = String.format("%d/%02d/%02d-%02d:%02d", c.get(Calendar.YEAR),
            c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY),
            c.get(Calendar.MINUTE))

        return view
    }
}