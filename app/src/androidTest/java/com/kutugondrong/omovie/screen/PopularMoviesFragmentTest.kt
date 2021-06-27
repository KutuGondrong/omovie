package com.kutugondrong.omovie.screen

import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@UninstallModules(AppModule::class)
@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class PopularMoviesFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory: OMovieFragmentFactoryAndroidTest

    @Before
    fun init() {
        hiltRule.inject()
    }


    @Test
    fun testVisibility(){
//        var testViewModel: PopularMovieViewModel? = null
//        launchFragmentInHiltContainer<PopularMoviesFragment>(fragmentFactory = fragmentFactory) {
//
//
//        }
    }

    @Test
    fun testComponentVisibilityAndData_componentDisplayedWithCorrectValue(){

    }

    @Test
    fun testClickItemMovie_componentVisibilityChangedFollowingTheClickAction() {

    }

}