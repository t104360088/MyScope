package com.example.myscope.fragment.user

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myscope.R
import com.example.myscope.Response_SetUser
import com.example.myscope.UserInfoSharedPreferences
import com.example.myscope.activity.MainActivity
import com.example.myscope.fragment.base.ObserverFragment
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.user.User
import com.example.myscope.manager.user.UserManager
import java.util.*

class EnterFragment : ObserverFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_enter, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Log.e("EnterFragment", "onActivityCreated")

        val user = UserManager.instance.getCurrentUser()
        Log.e("EnterFragment", "userEmail:${user?.email}")

        Handler().postDelayed({
            if (user != null && user.isEmailVerified) {
                Log.e("EnterFragment", "autoLogin")
                UserManager.instance.getUserData()
            } else {
                switchTo(LoginFragment(), null)
            }
        }, 1000)
    }

    override fun update(o: Observable?, arg: Any?) {
        when (arg) {
            Response_SetUser -> {
                mActivity.runOnUiThread {
                    Log.e("EnterFragment", "Response_SetUser")
                    startActivity(Intent(mActivity, MainActivity::class.java))
                    mActivity.finish()
                }
            }
            is User -> {
                mActivity.runOnUiThread {
                    //Save to Local Database
                    val sp = UserInfoSharedPreferences(mActivity)
                    arg.name?.let { sp.setName(it) }
                    arg.email.let { sp.setEmail(it) }
                    arg.avatar?.let { sp.setAvatar(it) }
                    arg.background?.let { sp.setBackground(it) }

                    //Update user online status
                    Log.e("EnterFragment", "Update user online status")
                    arg.onlineTime = System.currentTimeMillis()
                    UserManager.instance.setUserData(arg)
                }
            }
            is ErrorMsg -> {
                mActivity.runOnUiThread {
                    switchTo(LoginFragment(), null)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("EnterFragment", "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.e("EnterFragment", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("EnterFragment", "onDestroy")
    }
}