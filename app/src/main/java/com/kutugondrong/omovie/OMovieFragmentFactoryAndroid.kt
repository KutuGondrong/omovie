package com.kutugondrong.omovie

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.kutugondrong.omovie.screen.detail.DetailMovieFragment
import com.kutugondrong.omovie.screen.favorite.FavoriteMoviesFragment
import com.kutugondrong.omovie.screen.main.MainFragment
import com.kutugondrong.omovie.screen.popular.PopularMoviesFragment
import com.kutugondrong.omovie.screen.toprate.TopRateMoviesFragment
import javax.inject.Inject

class OMovieFragmentFactoryAndroid @Inject constructor() : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className){
            MainFragment::class.java.name -> MainFragment()
            PopularMoviesFragment::class.java.name -> PopularMoviesFragment()
            TopRateMoviesFragment::class.java.name -> TopRateMoviesFragment()
            FavoriteMoviesFragment::class.java.name -> FavoriteMoviesFragment()
            DetailMovieFragment::class.java.name -> DetailMovieFragment()
            else -> super.instantiate(classLoader, className)
        }

    }
}