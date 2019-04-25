package com.example.myscope.fragment.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myscope.R
import com.example.myscope.fragment.base.BaseFragment

class InviteFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.e("InviteFragment", "onCreateView")
        return inflater.inflate(R.layout.fragment_invite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Toast.makeText(mActivity, "邀請列表", Toast.LENGTH_SHORT).show()
    }
}