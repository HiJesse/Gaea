package cn.jesse.gaea.plugin.main.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import cn.jesse.gaea.plugin.main.ui.fragment.MainFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val fragmentList: MutableList<Fragment> = ArrayList()

    init {
        fragmentList.add(MainFragment.newInstance() as Fragment)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

}