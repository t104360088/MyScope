package com.example.myscope.fragment.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
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
        setActionBar(0)

        //Switch page
        arguments?.let {
            viewPager.currentItem = it.getInt("Page")
        }
        Log.e("ChatTabFragment", "onActivityCreated")
    }

    private fun initView() {
        val chat = ChatFragment()
        val friend = FriendFragment()
        val invite = InviteFragment()
        val tabName = listOf("聊天室", "好友列表", "邀請列表")
        val tabFragment = listOf<Fragment>(chat, friend, invite)
        val adapter = ViewPagerAdapter(tabName, tabFragment, childFragmentManager)

        viewPager.offscreenPageLimit = 2 // Control initial fragment count in cache
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        viewPager.addOnPageChangeListener (object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                setActionBar(position)
            }
        })
    }

    private fun setActionBar(position: Int) {
        mActivity.setTitle("聊天")
        (mActivity as MainActivity).showNavigationDrawer()
        (mActivity as MainActivity).showNavigationBottom()

        when (position) {
            0 -> {
            }
            1 -> {
            }
            2 -> {
                mActivity.setImageButton(2, R.drawable.ic_search)?.setOnClickListener {
                    switchTo(AddFriendFragment())
                    (mActivity as MainActivity).showNavigationDrawer(false)
                    (mActivity as MainActivity).showNavigationBottom(false)
                }
            }
        }
    }
}