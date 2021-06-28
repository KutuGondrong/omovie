package com.kutugondrong.omovie.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kutugondrong.omovie.R
import com.kutugondrong.omovie.databinding.FragmentMainBinding
import com.kutugondrong.omovie.screen.favorite.FavoriteMoviesFragment
import com.kutugondrong.omovie.screen.popular.PopularMoviesFragment
import com.kutugondrong.omovie.screen.toprate.TopRateMoviesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    val fragmentList = arrayListOf(
        PopularMoviesFragment(),
        TopRateMoviesFragment(),
        FavoriteMoviesFragment()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMainBinding.bind(view)

        binding.apply {
            val tabListStr = arrayListOf(
                requireActivity().resources.getString(R.string.popular),
                requireActivity().resources.getString(R.string.top_rated),
                requireActivity().resources.getString(R.string.favorite)
            )

            val adapter = MainPagerAdapter(
                fragmentList,
                requireActivity().supportFragmentManager,
                lifecycle
            )

            mainPager.adapter = adapter

            TabLayoutMediator(tabLayout, mainPager) { tab, position ->
                tab.text = tabListStr[position]
            }.attach()
        }
    }
}