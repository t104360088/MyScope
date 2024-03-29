package com.example.myscope.fragment.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.Response_SignUp
import com.example.myscope.fragment.base.ObserverFragment
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.user.UserManager
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.*

class SignUpFragment : ObserverFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setActionBar()
        setListen()
        Log.e("SignUpFragment", "activity created")
    }

    private fun setActionBar() {
        mActivity.setTitle("註冊")
        mActivity.setBack()
    }

    private fun setListen() {
        btn_sign_up.setOnClickListener {
            val email = ed_email.text.toString()
            val pwd = ed_pwd.text.toString()
            if (email.isNotEmpty() && pwd.isNotEmpty()) {
                hideKeyBoard()
                showLoading(progressBar)
                UserManager.instance.signUp(email, pwd)
            } else
                Toast.makeText(mActivity, "請輸入帳號密碼", Toast.LENGTH_SHORT).show()
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        hideLoading(progressBar)
        when (arg) {
            Response_SignUp -> {
                popBack()
                Toast.makeText(mActivity, "請至信箱收取驗證信完成註冊", Toast.LENGTH_SHORT).show()
            }
            is ErrorMsg -> Toast.makeText(mActivity, arg.msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("SignUpFragment", "destroy")
    }
}