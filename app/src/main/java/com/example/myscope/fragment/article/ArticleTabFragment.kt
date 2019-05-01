package com.example.myscope.fragment.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.Response_Success
import com.example.myscope.activity.MainActivity
import com.example.myscope.fragment.base.ObserverFragment
import com.example.myscope.manager.ErrorMsg
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
        //UserManager.instance.setUserData()
        //setPage(1, 2)
    }

    private fun setActionBar() {
        mActivity.setTitle("貼文")
        (mActivity as MainActivity).showNavigationDrawer()
    }

    private fun setListen() {
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