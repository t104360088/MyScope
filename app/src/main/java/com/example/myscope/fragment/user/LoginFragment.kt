package com.example.myscope.fragment.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.Response_SetUser
import com.example.myscope.Response_SignIn
import com.example.myscope.UserInfoSharedPreferences
import com.example.myscope.activity.MainActivity
import com.example.myscope.fragment.base.ObserverFragment
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.user.User
import com.example.myscope.manager.user.UserManager
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*

class LoginFragment : ObserverFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setActionBar()
        setListen()
        getAccount()
        Log.e("LoginFragment", "activity created")
    }

    //Mark:Action
    private fun setActionBar() {
        mActivity.setTitle("登入")
    }

    private fun setListen() {
        tv_login.setOnClickListener {
            val email = ed_email.text.toString()
            val pwd = ed_pwd.text.toString()
            if (email.isNotEmpty() && pwd.isNotEmpty()) {
                hideKeyBoard()
                enableEditText(false)
                showLoading(progressBar)
                UserManager.instance.signIn(email, pwd)
            } else
                Toast.makeText(mActivity, "請輸入帳號密碼", Toast.LENGTH_SHORT).show()
        }
        tv_sign_up.setOnClickListener {
            switchTo(SignUpFragment())
        }
        tv_forget.setOnClickListener {
            switchTo(ForgetFragment())
        }
    }

    private fun enableEditText(enable: Boolean) {
        ed_email.isEnabled = enable
        ed_pwd.isEnabled = enable
    }

    //Mark:UserInfo
    private fun getAccount() {
        val sp = UserInfoSharedPreferences(mActivity)
        val email = sp.getEmail()
        val pwd = sp.getPassword()
        if (email.isNotEmpty() && pwd.isNotEmpty()) {
            ed_email.setText(email)
            ed_pwd.setText(pwd)
            cb_save_account.isChecked = true
        }
    }

    private fun saveAccount() {
        val sp = UserInfoSharedPreferences(mActivity)
        if (cb_save_account.isChecked) {
            sp.setEmail(ed_email.text.toString())
            sp.setPassword(ed_pwd.text.toString())
        } else {
            sp.clearLoginData()
        }
    }

    //Mark:ResponseHandler
    override fun update(o: Observable?, arg: Any?) {
        when (arg) {
            Response_SignIn -> {
                saveAccount()
                UserManager.instance.getUserData()
            }
            Response_SetUser -> {
                mActivity.runOnUiThread {
                    hideLoading(progressBar)
                    Log.e("LoginFragment", "Response_SetUser")
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
                    Log.e("LoginFragment", "Update user online status")
                    arg.onlineTime = System.currentTimeMillis()
                    UserManager.instance.setUserData(arg)
                }
            }
            is ErrorMsg -> {
                mActivity.runOnUiThread {
                    hideLoading(progressBar)
                    enableEditText(true)
                    Toast.makeText(mActivity, arg.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("LoginFragment", "destroy")
    }
}