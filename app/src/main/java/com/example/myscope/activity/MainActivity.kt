package com.example.myscope.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myscope.*
import com.example.myscope.fragment.article.ArticleTabFragment
import com.example.myscope.fragment.chat.ChatTabFragment
import com.example.myscope.fragment.user.PersonalInfoFragment
import com.example.myscope.manager.user.UserManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.header_drawer.*
import java.util.*

class MainActivity : ObserverActivity() {
    //private var isFirstOnline = true
    //private var canUpdated = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("MainActivity", "onCreate")
        setListen()
        showNavigationBottom()
        showNavigationDrawer()
        setNavigation(0)
        showSnackbar("歡迎回來")
        //研究登入流程是否要再修改
        //UserManager.instance.getUserData()
    }

    fun showNavigationBottom(visible: Boolean = true) {
        nav_bottom.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun showNavigationDrawer(visible: Boolean = true) {
        setDrawer(visible)
        setDrawerGesture(visible)
    }

    //Navigate view
    fun setNavigation(position: Int) {

        //因為找不到切換方式，menu.getItem(p).setClick 試過沒用，暫時土法煉鋼
        when (position) {
            0 -> findViewById<View>(R.id.navigation_bottom_article).performClick()
            else -> findViewById<View>(R.id.navigation_bottom_chat).performClick()
        }
    }

    private fun setListen() {
        setNavigationBottom()
        setNavigationDrawer()
    }

    private fun setNavigationBottom() {
        nav_bottom.setOnNavigationItemSelectedListener {
            val fm = this.supportFragmentManager
            val ft = fm.beginTransaction()
            val fragment = when (it.itemId) {
                R.id.navigation_bottom_article -> ArticleTabFragment()
                else -> ChatTabFragment()
            }
            ft.replace(R.id.fl_fragment, fragment, fragment.javaClass.simpleName)
            ft.commit()

            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun setNavigationDrawer() {
        //Actionbar open drawer
        setDrawer()?.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        //Drawer item action
        nav_drawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_logout -> {
                    //Clean data
                    val sp = UserInfoSharedPreferences(this@MainActivity)
                    sp.clearUserData()
                    UserManager.instance.signOut()

                    //Adjust the close animation
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler().postDelayed({
                        startActivity(Intent(this, EnterActivity::class.java))
                        this.finish()
                    }, 250)
                    return@setNavigationItemSelectedListener true
                }
                R.id.nav_info -> {
                    switchTo(PersonalInfoFragment())
                    showNavigationBottom(false)
                    setDrawerGesture(false)
                }
                R.id.nav_share -> {
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "text/plain"
                    i.putExtra(Intent.EXTRA_SUBJECT, "MyScope分享")
                    i.putExtra(Intent.EXTRA_TEXT, "我在使用MyScope，快來一起建立自己的社交圈吧!")
                    startActivity(Intent.createChooser(i, "MyScope分享"))
                }
                R.id.nav_send -> {
                    val sp = UserInfoSharedPreferences(this@MainActivity)
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",
                        "t104360088@ntut.org.tw",null))
                    intent.putExtra(Intent.EXTRA_SUBJECT, "${sp.getName()} 對 MyScope 的建議與回饋")
                    intent.putExtra(Intent.EXTRA_TEXT, "寫下你的想法")
                    startActivity(Intent.createChooser(intent,"Send email..."))
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        //Listen drawer status
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerOpened(drawerView: View) {
                //Update drawer's header
                Log.e("MainActivity", "setDrawerHeader")
                val name = header_drawer.findViewById<TextView>(R.id.tv_name)
                val email = header_drawer.findViewById<TextView>(R.id.tv_email)
                val avatar = header_drawer.findViewById<ImageView>(R.id.img_avatar)
                val bg = header_drawer.findViewById<ImageView>(R.id.img_bg)
                val sp = UserInfoSharedPreferences(this@MainActivity)
                name.text = sp.getName()
                email.text = sp.getEmail()
                ImageLoader.loadImage(avatar, sp.getAvatar())
                ImageLoader.loadImage(bg, sp.getBackground(), ImageType.Background)
            }

            override fun onDrawerClosed(drawerView: View) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerStateChanged(newState: Int) {}
        })
    }

    //Open drawer gesture
    private fun setDrawerGesture(open: Boolean = true) {
        val status = if (open) DrawerLayout.LOCK_MODE_UNLOCKED
                    else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        drawerLayout.setDrawerLockMode(status)
    }

    private fun showSnackbar(msg: String, duration: Int = Snackbar.LENGTH_SHORT,
                     actionName: String = "", listener: View.OnClickListener? = null) {
        Snackbar.make(findViewById(android.R.id.content), msg, duration)
            .setAction(actionName, listener)
            .show()
    }

    //Change fragment
    fun switchTo(fragment : Fragment, bundle: Bundle? = null, broken: Boolean = false) {
        val fm = this.supportFragmentManager
        if(broken || fm.findFragmentByTag(fragment.javaClass.simpleName) == null){
            if(broken && fm.backStackEntryCount > 0) {
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                Log.e("switchTo", "清空堆疊")
            }

            Log.e("switchTo", fragment.javaClass.simpleName)
            fragment.arguments = bundle
            val ft = fm.beginTransaction()
            ft.setCustomAnimations(
                R.anim.abc_fade_in,
                R.anim.abc_fade_out,
                R.anim.abc_grow_fade_in_from_bottom,
                R.anim.abc_shrink_fade_out_from_bottom
            )
            ft.replace(R.id.fl_fragment, fragment, fragment.javaClass.simpleName)
            ft.addToBackStack(fragment.javaClass.simpleName) //Add stack
            ft.commit()
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        when (arg) {
//            Response_SetUser -> {
//                if (canUpdated) {
//                    canUpdated = false
//                    Log.e("MainActivity", "Response_SetUser")
//                }
//            }
//            is User -> {
//                runOnUiThread {
//                    if (canUpdated) {
//                        canUpdated = false
//
//                        //Save to Local Database
//                        val sp = UserInfoSharedPreferences(this)
//                        arg.name?.let { sp.setName(it) }
//                        arg.email.let { sp.setEmail(it) }
//                        arg.avatar?.let { sp.setAvatar(it) }
//                        arg.background?.let { sp.setBackground(it) }
//
//                        //Update user online status
//                        if (isFirstOnline) {
//                            Log.e("MainActivity", "Update user online status")
//                            isFirstOnline = false
//                            arg.onlineTime = System.currentTimeMillis()
//                            UserManager.instance.setUserData(arg)
//                        }
//                    }
//                }
//            }
//            is ErrorMsg -> {
//                runOnUiThread {
//                    if (canUpdated) {
//                        canUpdated = false
//                        showSnackbar(arg.msg)
//                    }
//                }
//            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("MainActivity", "onDestroy")
    }
}
