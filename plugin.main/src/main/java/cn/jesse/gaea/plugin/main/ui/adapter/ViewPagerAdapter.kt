package cn.jesse.gaea.plugin.main.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import cn.jesse.gaea.plugin.main.ui.fragment.DiscoverFragment
import cn.jesse.gaea.plugin.main.ui.fragment.MainFragment

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    private val fragmentList: MutableList<Fragment> = ArrayList()

    init {
        fragmentList.add(MainFragment.newInstance())
        fragmentList.add(DiscoverFragment.newInstance())
    }

    /**
     * 外部添加fragment
     */
    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

}