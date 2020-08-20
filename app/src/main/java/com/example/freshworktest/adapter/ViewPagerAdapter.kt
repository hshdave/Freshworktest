package com.example.freshworktest.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.freshworktest.views.FavouriteFragment
import com.example.freshworktest.views.SearchFragment

class ViewPagerAdapter(activity : AppCompatActivity, private val itemCount : Int):FragmentStateAdapter(activity) {


    override fun getItemCount(): Int = itemCount

    override fun createFragment(position: Int): Fragment {

        if (position == 0)
        {
            return SearchFragment()
        }else if (position == 1)
        {
            return FavouriteFragment()
        }
        return SearchFragment()
    }


}