package com.example.freshworktest.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.freshworktest.adapter.ViewPagerAdapter
import com.example.freshworktest.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val titles = arrayOf("Trending","Favourite")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init()
    {
        supportActionBar?.elevation  = 0F
        binding.viewPager.adapter = ViewPagerAdapter(this,titles.size)

        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab, position ->
                tab.text = titles[position]
        }.attach()

    }
}