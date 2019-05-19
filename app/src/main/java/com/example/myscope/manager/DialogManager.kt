package com.example.myscope.manager

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.AnimationDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.example.myscope.R
import com.example.myscope.adapter.ListDialogAdapter

class DialogManager {

    //Singleton
    companion object {
        val instance: DialogManager by lazy { DialogManager() }
    }

    //private var loadingDialog: Dialog? = null
    private var dialog: Dialog? = null

//    fun dismissAll(){
//        loadingDialog?.dismiss()
//        dialog?.dismiss()
//    }

//    fun showLoading(activity: Activity){
//        if(!activity.isDestroyed){
//            loadingDialog?.dismiss()
//
//            loadingDialog = AlertDialog.Builder(activity).create()
//            loadingDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            loadingDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
//            loadingDialog?.show()
//
//            val view = View.inflate(activity, R.layout.dialog_loading,null)
//            view.setOnClickListener { loadingDialog?.dismiss() }
//            val img_load = view.findViewById<ImageView>(R.id.img_load)
//            (img_load.background as AnimationDrawable).start()
//            loadingDialog?.setContentView(view)
//        }
//    }
//
//    fun cancelLoading() = loadingDialog?.dismiss()

    fun showMessage(activity: Activity, message: String, flag:Boolean = false): TextView?{
        if(!activity.isDestroyed){
            dialog?.dismiss()

            dialog = AlertDialog.Builder(activity).create()
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog?.setCancelable(false)
            dialog?.show()

            val view = View.inflate(activity, R.layout.dialog_message, null)
            val tv_message = view.findViewById<TextView>(R.id.tv_msg)
            val tv_cancel = view.findViewById<TextView>(R.id.tv_cancel)
            val tv_ok = view.findViewById<TextView>(R.id.tv_ok)

            tv_message.text = message
            tv_cancel.visibility = if(flag) View.VISIBLE else View.GONE
            tv_cancel.setOnClickListener { dialog?.dismiss() }
            tv_ok.setOnClickListener { dialog?.dismiss() }
            dialog?.setContentView(view)
            return tv_ok
        }
        return null
    }

    fun showList(activity: Activity, strList: Array<String>): ListView?{
        if(!activity.isDestroyed){
            dialog?.dismiss()

            dialog = AlertDialog.Builder(activity).create()
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog?.show()

            val view = View.inflate(activity, R.layout.dialog_listview, null)
            dialog?.setContentView(view)

            val arrayAdapter = ListDialogAdapter(activity, R.layout.item_textview, strList)
            val listView = view.findViewById<ListView>(R.id.listView)
            listView.adapter = arrayAdapter

            listView.setOnItemClickListener { parent, view, position, id ->
                dialog?.dismiss()
            }
            return listView
        }
        return null
    }

    fun showCustom(activity: Activity, layout: Int, keyboard: Boolean = false): View?{
        if(!activity.isDestroyed){
            dialog?.dismiss()

            dialog = AlertDialog.Builder(activity).create()
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog?.show()
            if(keyboard)
                dialog?.window?.clearFlags(
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                            WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

            val view = View.inflate(activity, layout, null)
            dialog?.setContentView(view)
            return view
        }
        return null
    }

    fun dismissDialog() = dialog?.dismiss()
}