package com.example.myscope.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myscope.R

class ListDialogAdapter(context: Context, private val resource: Int, list: Array<String>):
    ArrayAdapter<String>(context, resource, list) {

    private class ViewHolder(v: View) {
        val tv_option: TextView = v.findViewById(R.id.tv_option)
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup): View? {
        val view: View
        val holder: ViewHolder

        if(convertView == null){
            view = View.inflate(context, resource, null)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        holder.tv_option.text = getItem(position)

        return view
    }
}