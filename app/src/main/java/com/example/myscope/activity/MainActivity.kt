package com.example.myscope.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.view.GravityCompat
import com.example.myscope.R
import com.example.myscope.fragment.article.ArticleTabFragment
import com.example.myscope.fragment.chat.ChatTabFragment
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
        findViewById<View>(R.id.navigation_bottom_friend).performClick() //clickItem
        showSnackbar("歡迎回來")
    }

    fun showNavigationBottom(visible: Boolean = true) {
        nav_bottom.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun showNavigationDrawer(visible: Boolean = true) {
        setDrawer(visible)
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
                R.id.navigation_bottom_friend -> ArticleTabFragment()
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
                R.id.nav_gallery -> {

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

    override fun update(o: Observable?, arg: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
