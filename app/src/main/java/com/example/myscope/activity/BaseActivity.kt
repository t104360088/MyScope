package com.example.myscope.activity

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myscope.R
import java.util.*

abstract class BaseActivity : AppCompatActivity(), Observer{
    // Set application language
//    override fun attachBaseContext(newBase: Context) {
//        val config = newBase.resources.configuration
//        if (UserInfoSharedPreferences(newBase).getLanguage()=="zh")
//            config.setLocale(Locale.TRADITIONAL_CHINESE)
//        else
//            config.setLocale(Locale.ENGLISH)
//        super.attachBaseContext(newBase.createConfigurationContext(config))
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if (!Method.hasPermissions(this, *PERMISSIONS))
//            reStartApp()

        // Init ActionBar
        supportActionBar?.let {
            it.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            it.setCustomView(R.layout.actionbar)

            val parent = it.customView?.parent as Toolbar
            parent.setPadding(0, 0, 0, 0)
            parent.setContentInsetsAbsolute(0, 0)
            it.customView?.findViewById<View>(R.id.btn_back)?.setOnClickListener { onBackPressed() }
        }

        //DataManager.instance.addObserver(this)
    }

    private fun resetActionBar(){
        supportActionBar?.customView?.findViewById<View>(R.id.btn_back)?.visibility = View.GONE
        supportActionBar?.customView?.findViewById<View>(R.id.tv_btn)?.visibility = View.GONE
        supportActionBar?.customView?.findViewById<View>(R.id.img_btn)?.visibility = View.GONE
        supportActionBar?.customView?.findViewById<View>(R.id.img_btn2)?.visibility = View.GONE
        supportActionBar?.customView?.findViewById<View>(R.id.img_btn3)?.visibility = View.GONE
    }

    /**
     * Set Actionbar
     */
    fun setTitle(title: String, reset: Boolean = true) {
        if(reset)
            resetActionBar()

        val barTitle = supportActionBar?.customView?.findViewById<TextView>(R.id.tv_title)
        barTitle?.text = title
    }

    fun setBack(visible: Boolean) {
        supportActionBar?.customView?.findViewById<View>(R.id.btn_back)?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun setImageButton(position: Int, resource: Int): ImageView? {
        val imageView = when (position) {
            0 -> supportActionBar?.customView?.findViewById<ImageView>(R.id.img_btn)
            1 -> supportActionBar?.customView?.findViewById<ImageView>(R.id.img_btn2)
            else -> supportActionBar?.customView?.findViewById<ImageView>(R.id.img_btn3)
        }

        if (resource != 0) {
            imageView?.setImageResource(resource)
            imageView?.visibility = View.VISIBLE
        } else
            imageView?.visibility = View.GONE
        return imageView
    }

    fun setTextButton(text: String): TextView? {
        val btn = supportActionBar?.customView?.findViewById<TextView>(R.id.tv_btn)
        if (text.isNotEmpty()) {
            btn?.text = text
            btn?.visibility = View.VISIBLE
        } else
            btn?.visibility = View.GONE
        return btn
    }

//    override fun onStart() {
//        super.onStart()
//        DataManager.instance.addObserver(this)
//    }
//
//    override fun update(o: Observable?, arg: Any?) {
//        if(arg is BaseRespond){
//            when{
//                arg.status==2 ->{
//                    DataManager.instance.deleteObserver(this)
//                    DataManager.instance.cleanLocalData()
//
//                    cleanNotification()
//                    cancelAlarm()
//
//                    runOnUiThread {
//                        DialogManager.instance.showMessage(this,getString(R.string.key_expire))
//                            ?.setOnClickListener {
//                                DialogManager.instance.cancelDialog()
//                                reStartApp()
//                            }
//                    }
//                }
//                arg.status!=0 ->
//                    runOnUiThread {
//                        Toast.makeText(this, if(arg.errMsgs.isNotEmpty()) arg.errMsgs[0] else
//                            getString(R.string.unexplained_error), Toast.LENGTH_SHORT).show()
//                    }
//            }
//        }
//    }
//
//    fun cleanNotification(){
//        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        nm.cancelAll()
//    }
//
//    fun cancelAlarm(){
//        val intent = Intent(this, AlarmReceiver::class.java)
//
//        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        am.cancel(PendingIntent.getBroadcast(this,0, intent,0))
//    }
//
//    override fun onStop() {
//        super.onStop()
//        DataManager.instance.deleteObserver(this)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        DialogManager.instance.dismissAll()
//    }
}