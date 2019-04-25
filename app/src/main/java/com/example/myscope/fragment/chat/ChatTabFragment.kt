package com.example.myscope.fragment.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myscope.R
import com.example.myscope.activity.MainActivity
import com.example.myscope.adapter.ViewPagerAdapter
import com.example.myscope.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_tab.*

class ChatTabFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        setActionBar()
        (mActivity as MainActivity).showNavigationBottom(true)
        //UserManager.instance.setUserData()
    }

    private fun setActionBar() {
        mActivity.setTitle("聊天")
    }

    private fun initView() {
        val chat = ChatFragment()
        val friend = FriendFragment()
        val invite = InviteFragment()
        val tabName = listOf("聊天室", "好友列表", "邀請列表")
        val tabFragment = listOf<Fragment>(chat, friend, invite)
        val adapter = ViewPagerAdapter(tabName, tabFragment, childFragmentManager)
        viewPager.offscreenPageLimit = 3 // Control initial fragment count in cache
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}