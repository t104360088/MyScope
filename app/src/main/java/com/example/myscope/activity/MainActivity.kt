package com.example.myscope.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myscope.R
import com.example.myscope.Response_SetUser
import com.example.myscope.fragment.article.ArticleTabFragment
import com.example.myscope.fragment.chat.ChatTabFragment
import com.example.myscope.fragment.user.PersonalInfoFragment
import com.example.myscope.manager.user.User
import com.example.myscope.manager.user.UserManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListen()
        showNavigationBottom()
        showNavigationDrawer()
        //findViewById<View>(R.id.navigation_bottom_friend).performClick() //clickItem
        setNavigation(0)
        showSnackbar("歡迎回來")
        UserManager.instance.addObserver(this)

        UserManager.instance.getCurrentUser()?.let {
            Log.e("MainActivity", "setUserData")
            val user = User(it.uid, it.email!!, System.currentTimeMillis(), true)
            UserManager.instance.setUserData(user)
        }
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
        //Open drawer
        setDrawer()?.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        //Drawer item action
        nav_drawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_logout -> {
                    UserManager.instance.signOut()
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
                R.id.nav_slideshow -> {

                }
                R.id.nav_tools -> {

                }
                R.id.nav_share -> {

                }
                R.id.nav_send -> {

                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }
    }

    private fun showSnackbar(msg: String, duration: Int = Snackbar.LENGTH_SHORT,
                     actionName: String = "", listener: View.OnClickListener? = null) {
        Snackbar.make(findViewById(android.R.id.content), msg, duration)
            .setAction(actionName, listener)
            .show()
    }

    //Change fragment
    private fun switchTo(fragment : Fragment, bundle: Bundle? = null, broken: Boolean = false) {
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

    //Open drawer gesture
    private fun setDrawerGesture(open: Boolean = true) {
        val status = if (open) DrawerLayout.LOCK_MODE_UNLOCKED
                    else DrawerLayout.LOCK_MODE_LOCKED_CLOSED
        drawerLayout.setDrawerLockMode(status)
    }

    override fun update(o: Observable?, arg: Any?) {
        when (arg) {
            Response_SetUser -> {
                Log.e("MainActivity", "Response_SetUser")
                UserManager.instance.getUserData()
            }
            is User -> {
                Log.e("MainActivity", "getUserData")
                val tv_name = findViewById<TextView>(R.id.tv_name)
                val tv_email = findViewById<TextView>(R.id.tv_email)
                tv_name.text = arg.name
                tv_email.text = arg.email
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
