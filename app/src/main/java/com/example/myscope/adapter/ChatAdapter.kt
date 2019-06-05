package com.example.myscope.adapter

import android.content.Context
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.myscope.ImageLoader
import com.example.myscope.R
import com.example.myscope.manager.chat.Chat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(context: Context, list: ArrayList<Chat>,
                  private val uid: String, private val listener: MsgListener):
    ArrayAdapter<Chat>(context, 0, list) {

    private lateinit var holder: ViewHolder
    private val msgTime = Calendar.getInstance()
    private val now = Calendar.getInstance()

    private class ViewHolder {
        lateinit var tv_message: TextView
        lateinit var tv_time: TextView
        var tv_read: TextView? = null
        var tv_name: TextView? = null
        var img_photo: ImageView? = null
        var circleImageView: ImageView? = null
        var messageLayout: FrameLayout? = null
    }

    interface MsgListener {
        fun onMessage(position: Int)
        fun onPhoto(position: Int)
    }

    //要再了解清楚
    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return super.getItemViewType(position)

        return when {
            position < count -> if (item.uid != uid) 0 else 1 //0: other 1: self
            else -> super.getItemViewType(position)
        }
    }

    override fun getViewTypeCount() = 2

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val itemType = getItemViewType(position)

        if (convertView == null) {
            view = when (itemType) {
                0 -> View.inflate(context, R.layout.item_chat_left, null)
                1 -> View.inflate(context, R.layout.item_chat_right, null)
                else -> View.inflate(context, R.layout.item_chat_right, null) //待改善
            }
            holder = ViewHolder()
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        holder.tv_message = view.findViewById(R.id.tv_msg)
        holder.tv_time = view.findViewById(R.id.tv_time)
        holder.messageLayout = view.findViewById(R.id.messageLayout)

        if (itemType != -1) {
            holder.tv_read = view.findViewById(R.id.tv_read)
            holder.img_photo = view.findViewById(R.id.img_photo)
        }

        //Other layout
        if (itemType == 0) {
            holder.circleImageView = view.findViewById(R.id.img_avatar)
            holder.tv_name = view.findViewById(R.id.tv_name)
            holder.circleImageView?.let {
                ImageLoader.loadImage(it, getItem(position).avatar)
            }
        }
        setTime(position)
        setMsg(position)
        setListen(position)

        return view
    }

    private fun setTime(position: Int) {
        getItem(position)?.let {
            var dateString = ""

            if (it.time != 0L) {
                msgTime.timeInMillis = it.time

                dateString = if (now.equals(msgTime, Calendar.YEAR)) {
                    if (now.equals(msgTime, Calendar.MONTH))
                        if (now.equals(msgTime, Calendar.DATE))
                            msgTime.format("HH:mm")
                        else
                            msgTime.format("MM/dd HH:mm")
                    else
                        msgTime.format("MM/dd HH:mm")
                } else
                    msgTime.format("yyyy/MM/dd")
            }
            holder.tv_time.text = dateString
        }
    }

    private fun setMsg(position: Int) {
        holder.img_photo?.visibility = View.GONE
        holder.messageLayout?.visibility = View.GONE
        holder.tv_message.visibility = View.GONE

        getItem(position)?.run {
            when(this.type){
//                2 ->{
//                    holder.img_photo?.let {
//                        val url = this.msg
//                        ImageLoader.loadImage(it, url, ImageType.Background)
//                        it.visibility = View.VISIBLE
//                    }
//                }
//                9 ->{
//                    holder.messageLayout?.visibility = View.VISIBLE
//                    holder.tv_message.visibility = View.VISIBLE
//                    holder.tv_message.text = String.format("[ ${this.msg} ]")
//                }
                else ->{
                    holder.messageLayout?.visibility = View.VISIBLE
                    holder.tv_message.visibility = View.VISIBLE
                    holder.tv_message.text = this.msg
                    holder.tv_message.movementMethod = LinkMovementMethod.getInstance()
                    holder.tv_message.autoLinkMask = Linkify.ALL
                }
            }

//            holder.tv_read?.visibility =
//                if (this.readRecord.size > 1) View.VISIBLE else View.GONE
            holder.tv_read?.visibility = View.GONE
        }
    }

    private fun setListen(position: Int){
        holder.tv_message.setTextIsSelectable(true)
        holder.tv_message.setOnClickListener {
            listener.onMessage(position)
        }

        holder.img_photo?.setOnClickListener {
            listener.onPhoto(position)
        }

        holder.img_photo?.setOnLongClickListener {
            listener.onMessage(position)
            return@setOnLongClickListener true
        }
    }

    private fun Calendar.equals(with: Calendar, type: Int): Boolean {
        return this.get(type) == with.get(type)
    }

    private fun Calendar.format(format: String): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(this.time)
    }
}