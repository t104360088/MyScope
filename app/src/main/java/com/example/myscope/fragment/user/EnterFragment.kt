package com.example.myscope.fragment.user

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myscope.R
import com.example.myscope.activity.MainActivity
import com.example.myscope.fragment.base.BaseFragment
import com.example.myscope.manager.user.UserManager

class EnterFragment : BaseFragment() {
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
                startActivity(Intent(mActivity, MainActivity::class.java))
                mActivity.finish()
            } else {
                switchTo(LoginFragment(), null)
            }
        }, 1000)
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