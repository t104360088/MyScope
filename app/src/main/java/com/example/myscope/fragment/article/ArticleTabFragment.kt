package com.example.myscope.fragment.article

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.Response_Success
import com.example.myscope.activity.EnterActivity
import com.example.myscope.activity.MainActivity
import com.example.myscope.fragment.base.ObserverFragment
import com.example.myscope.manager.ErrorMsg
import com.example.myscope.manager.user.UserManager
import kotlinx.android.synthetic.main.fragment_article.*
import java.util.*

class ArticleTabFragment : ObserverFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setActionBar()
        setListen()
        (mActivity as MainActivity).showNavigationBottom(true)
        //UserManager.instance.setUserData()
    }

    private fun setActionBar() {
        mActivity.setTitle("貼文")
    }

    private fun setListen() {
        button.setOnClickListener {
            UserManager.instance.signOut()
            startActivity(Intent(mActivity, EnterActivity::class.java))
            mActivity.finish()
        }
    }

    override fun update(o: Observable?, arg: Any?) {
        //hideLoading(progressBar)
        when (arg) {
            Response_Success -> {
                Toast.makeText(mActivity, "設定完成", Toast.LENGTH_SHORT).show()
            }
            is ErrorMsg -> Toast.makeText(mActivity, arg.msg, Toast.LENGTH_SHORT).show()
        }
    }
}