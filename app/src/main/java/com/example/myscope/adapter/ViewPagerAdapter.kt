package com.example.myscope.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(private val tabName: List<String>,
                       private val tabFragment: List<Fragment>,
                       fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount() = tabName.size

    override fun getPageTitle(position: Int): CharSequence? {
        return tabName[position]
    }

    override fun getItem(position: Int): Fragment {
        return tabFragment[position]
    }
}