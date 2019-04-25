package com.example.myscope.fragment.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.Response_Success
import com.example.myscope.fragment.base.ObserverFragment
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.user.UserManager
import kotlinx.android.synthetic.main.fragment_forget.*
import java.util.*

class ForgetFragment : ObserverFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forget, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setActionBar()
        setListen()
        Log.e("ForgetFragment", "activity created")
    }

    private fun setActionBar() {
        mActivity.setTitle("忘記密碼")
        mActivity.setBack(true)
    }

    private fun setListen() {
        btn_send.setOnClickListener {
            val email = ed_email.text.toString()
            if (email.isNotEmpty()) {
                hideKeyBoard()
                showLoading(progressBar)
                UserManager.instance.sendPasswordResetEmail(email)
            } else
                Toast.makeText(mActivity, "請輸入帳戶的電子信箱", Toast.LENGTH_SHORT).show()
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        hideLoading(progressBar)
        when (arg) {
            Response_Success -> {
                popBack()
                Toast.makeText(mActivity, "請至信箱收取信件重設密碼", Toast.LENGTH_SHORT).show()
            }
            is ErrorMsg -> Toast.makeText(mActivity, arg.msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("ForgetFragment", "destroy")
    }
}