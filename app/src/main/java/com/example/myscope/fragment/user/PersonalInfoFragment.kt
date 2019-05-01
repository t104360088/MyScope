package com.example.myscope.fragment.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.fragment.base.ObserverFragment
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.user.User
import com.example.myscope.manager.user.UserManager
import kotlinx.android.synthetic.main.fragment_personal_info.*
import java.util.*

class PersonalInfoFragment : ObserverFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.e("PersonalInfoFragment", "onCreateView")
        return inflater.inflate(R.layout.fragment_personal_info, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Toast.makeText(mActivity, "個人頁面", Toast.LENGTH_SHORT).show()
        setActionBar()
        UserManager.instance.getUserData()
    }

    private fun setActionBar() {
        mActivity.setTitle("個人頁面")
        mActivity.setBack()
    }

    override fun update(o: Observable?, arg: Any?) {
        when (arg) {
            is User -> {
                ed_name.setText(arg.name)
                ed_email.setText(arg.email)
            }
            is ErrorMsg -> { showSnackbar(arg.msg) }
        }
    }
}