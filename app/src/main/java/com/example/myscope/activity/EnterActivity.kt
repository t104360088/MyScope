package com.example.myscope.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.myscope.R
import com.example.myscope.fragment.user.LoginFragment
import com.example.myscope.manager.user.UserManager
import java.util.*

class EnterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter)

        val user = UserManager.instance.getCurrentUser()
        Log.e("EnterActivity", "userEmail:${user?.email}")

        if (user != null && user.isEmailVerified) {
            Log.e("EnterActivity", "autoLogin")
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        } else {
            val fm = this.supportFragmentManager
            val ft = fm.beginTransaction()
            val fragment = LoginFragment()
            ft.add(R.id.fl_fragment, fragment, fragment.javaClass.simpleName)
            ft.commit()
        }
    }

    override fun update(o: Observable?, arg: Any?) {

    }

    override fun onBackPressed() {
        val count = this.supportFragmentManager.backStackEntryCount
        Log.e("EnterActivity", "stackCount:$count")
        if (count < 1)
            finish()
        else
            super.onBackPressed()
    }
}
