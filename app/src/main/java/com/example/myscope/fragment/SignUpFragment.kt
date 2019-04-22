package com.example.myscope.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.Response_Success
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.UserManager
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
    }

    private fun setActionBar() {
        mActivity.setTitle("註冊")
        mActivity.setBack(true)
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
            Response_Success -> {
                popBack()
                Toast.makeText(mActivity, "請至信箱收取驗證信完成註冊", Toast.LENGTH_SHORT).show()
            }
            is ErrorMsg -> Toast.makeText(mActivity, arg.msg, Toast.LENGTH_SHORT).show()
        }
    }
}