package com.acedrops.acedrops.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.acedrops.acedrops.view.dash.product.DescriptionFragment
import com.acedrops.acedrops.view.dash.product.ReviewFragment

class PageAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                DescriptionFragment()
            }
            1 -> {
                ReviewFragment()
            }
            else -> {
                DescriptionFragment()
            }
        }
    }
}