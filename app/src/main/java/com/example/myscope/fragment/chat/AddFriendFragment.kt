package com.example.myscope.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.myscope.ImageLoader
import com.example.myscope.R
import com.example.myscope.Response_SetFriend
import com.example.myscope.fragment.base.ObserverFragment
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.friend.Friend
import com.example.myscope.manager.friend.FriendManager
import com.example.myscope.manager.user.User
import com.example.myscope.manager.user.UserManager
import kotlinx.android.synthetic.main.fragment_add_friend.*
import java.util.*

class AddFriendFragment : ObserverFragment() {
    private lateinit var targetUID: String
    private lateinit var name: String
    private lateinit var avatar: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_friend, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setActionbar()
        setListen()
        setVisible(false)
    }

    private fun setActionbar() {
        mActivity.setTitle("搜尋好友")
        mActivity.setBack()
    }

    private fun setListen() {
        ed_email.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if(!isEmailValid(ed_email.text.toString()))
                    Toast.makeText(mActivity, "請輸入正確Email格式", Toast.LENGTH_SHORT).show()
                else {
                    hideKeyBoard()
                    showLoading(progressBar)
                    UserManager.instance.queryUserData(ed_email.text.toString())
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        btn_add.setOnClickListener {
            UserManager.instance.getMyUser()?.uid?.let {
                val friend = Friend(it, targetUID, name, avatar, 1)

                showLoading(progressBar)
                FriendManager.instance.setFriendData(friend)
            }
        }
    }

    private fun setVisible(show: Boolean = true) {
        img_avatar.visibility = if (show) View.VISIBLE else View.GONE
        tv_name.visibility = if (show) View.VISIBLE else View.GONE
        btn_add.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun update(o: Observable?, arg: Any?) {
        when (arg) {
            is User -> {
                mActivity.runOnUiThread {
                    hideLoading(progressBar)
                    targetUID = arg.uid
                    name = arg.name ?: ""
                    avatar = arg.avatar ?: ""

                    ImageLoader.loadImage(img_avatar, arg.avatar)
                    tv_name.text = arg.name

                    setVisible()
                    btn_add.visibility = View.GONE

                    //If result is not user, get target status
                    UserManager.instance.getMyUser()?.uid?.let {
                        if (it != arg.uid) {
                            showLoading(progressBar)
                            FriendManager.instance.getFriendStatus(it, arg.uid)
                        }
                    }
                }
            }
            is Friend -> {
                mActivity.runOnUiThread {
                    hideLoading(progressBar)
                    btn_add.visibility = View.VISIBLE
                    when (arg.status) {
                        0 -> {
                            btn_add.text = "加好友"
                            setButtonStatus(true)
                        }
                        1 -> {
                            btn_add.text = "已送出邀請"
                            setButtonStatus(false)
                        }
                        2 -> {
                            btn_add.text = "對方已發送邀請"
                            setButtonStatus(false)
                        }
                        3 -> {
                            btn_add.text = "已成為好友"
                            setButtonStatus(false)
                        }
                    }
                }
            }
            is ErrorMsg -> {
                mActivity.runOnUiThread {
                    hideLoading(progressBar)
                    showSnackbar(arg.msg)
                }
            }

            Response_SetFriend -> {
                mActivity.runOnUiThread {
                    hideLoading(progressBar)
                    btn_add.text = "已送出邀請"
                    setButtonStatus(false)
                }
            }
        }
    }

    private fun setButtonStatus(isEnabled: Boolean) {
        btn_add.isEnabled = isEnabled

        if (isEnabled) {
            btn_add.setTextColor(resources.getColor(android.R.color.white))
            btn_add.background = resources.getDrawable(R.drawable.btn_primary)
        } else {
            btn_add.setTextColor(resources.getColor(R.color.colorPrimaryDark))
            btn_add.background = resources.getDrawable(R.drawable.btn_secondary)
        }
    }
}