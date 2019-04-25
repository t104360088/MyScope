package com.example.myscope.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.myscope.R
import com.example.myscope.fragment.article.ArticleTabFragment
import com.example.myscope.fragment.chat.ChatTabFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListen()
        showNavigationBottom(true)
        findViewById<View>(R.id.navigation_bottom_friend).performClick() //clickItem
    }

    fun showNavigationBottom(open: Boolean) {
        navigation_bottom.visibility = if (open) View.VISIBLE else View.GONE
    }

    private fun setListen() {
        navigation_bottom.setOnNavigationItemSelectedListener {
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

    override fun update(o: Observable?, arg: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBackPressed() {
        Log.e("MainActivity", this.supportFragmentManager.backStackEntryCount.toString())
        super.onBackPressed()
    }
}
