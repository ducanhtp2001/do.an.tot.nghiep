package com.example.commyproject.ultil.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {

    private val fragments: ArrayList<Fragment> = ArrayList()
    private val titleFrm: ArrayList<String> = ArrayList()
    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleFrm[position]
    }

    fun add(fragment: Fragment?, title: String?) {
        if (fragment != null) {
            fragments.add(fragment)
        }
        if (title != null) {
            titleFrm.add(title)
        }
    }

}