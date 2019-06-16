package com.example.myscope.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.myscope.R

abstract class BaseActivity : AppCompatActivity() {
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

        // Init ActionBar
        supportActionBar?.let {
            it.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            it.setCustomView(R.layout.actionbar)

            val parent = it.customView?.parent as Toolbar
            parent.setPadding(0, 0, 0, 0)
            parent.setContentInsetsAbsolute(0, 0)
        }
    }

    private fun resetActionBar() {
        supportActionBar?.customView?.findViewById<View>(R.id.img_left)?.visibility = View.GONE
        supportActionBar?.customView?.findViewById<View>(R.id.img_left2)?.visibility = View.GONE
        supportActionBar?.customView?.findViewById<View>(R.id.tv_btn)?.visibility = View.GONE
        supportActionBar?.customView?.findViewById<View>(R.id.img_right)?.visibility = View.GONE
        supportActionBar?.customView?.findViewById<View>(R.id.img_right2)?.visibility = View.GONE
        supportActionBar?.customView?.findViewById<View>(R.id.img_right3)?.visibility = View.GONE
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

    fun setBack(visible: Boolean = true) {
        val btn = supportActionBar?.customView?.findViewById<ImageView>(R.id.img_left)
        if (visible) {
            btn?.setImageResource(R.drawable.back)
            btn?.setOnClickListener { onBackPressed() }
            btn?.visibility = View.VISIBLE
        } else
            btn?.visibility = View.GONE
    }

    fun setDrawer(visible: Boolean = true): ImageView? {
        val btn = supportActionBar?.customView?.findViewById<ImageView>(R.id.img_left2)
        if (visible) {
            btn?.setImageResource(R.drawable.menu)
            btn?.visibility = View.VISIBLE
            return btn
        } else
            btn?.visibility = View.GONE
        return null
    }

    fun setImageButton(position: Int, resource: Int): ImageView? {
        val imageView = when (position) {
            0 -> supportActionBar?.customView?.findViewById<ImageView>(R.id.img_right)
            1 -> supportActionBar?.customView?.findViewById<ImageView>(R.id.img_right2)
            else -> supportActionBar?.customView?.findViewById<ImageView>(R.id.img_right3)
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

    //Avoid keyboard showing issue of press actionbar back button
    override fun onBackPressed() {
        this.currentFocus?.let {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
        super.onBackPressed()
    }
}