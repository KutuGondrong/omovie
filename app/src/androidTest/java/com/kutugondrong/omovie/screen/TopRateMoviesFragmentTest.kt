package com.kutugondrong.omovie.screen

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.*
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.kutugondrong.omovie.MockServerDispatcher
import com.kutugondrong.omovie.R
import com.kutugondrong.omovie.TaskExecutorWithIdlingResourceRule
import com.kutugondrong.omovie.helper.selectTabAtPosition
import com.kutugondrong.omovie.helper.waitViewShown
import com.kutugondrong.omovie.launchFragmentInHiltContainer
import com.kutugondrong.omovie.screen.detail.DetailMovieFragment
import com.kutugondrong.omovie.screen.main.MainFragment
import com.kutugondrong.omovie.screen.main.MainFragmentDirections
import com.kutugondrong.omovie.screen.toprate.TopRateMoviesFragment
import com.kutugondrong.omovie.screen.toprate.TopRateMoviesRecyclerViewAdapter
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import javax.inject.Inject


@UninstallModules(AppModule::class)
@HiltAndroidTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TopRateMoviesFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = TaskExecutorWithIdlingResourceRule()

    @Inject
    lateinit var fragmentFactory: OMovieFragmentFactoryAndroidTest

    lateinit var mockWebServer: MockWebServer

    @Inject lateinit var okHttp: OkHttpClient

    @Before
    fun init() {
        hiltRule.inject()
        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = MockServerDispatcher().RequestDispatcher()
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(OkHttp3IdlingResource.create("okhttp", okHttp))
    }

    @After
    open fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testVisibilityTopRateMoviesFragment(){
        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = fragmentFactory)
        onView(withId(R.id.tabLayout)).perform(selectTabAtPosition(1))
        waitViewShown(withId(R.id.listTopRateMovie))
    }

    @Test
    fun testScrollListTopRateMovie(){
        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = fragmentFactory)
        onView(withId(R.id.tabLayout)).perform(selectTabAtPosition(1))
        waitViewShown(withId(R.id.listTopRateMovie))
        for (i in 1..5) {
            onView(withId(R.id.listTopRateMovie)).perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(i*19, ViewActions.scrollTo())
            )
        }
    }

    @Test
    fun testOpenTopRateMovie_thenNNavigateToDetailMovieFragment(){
        val navController = Mockito.mock(NavController::class.java)
        var frag: TopRateMoviesFragment? = null
        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            frag = this.fragmentList[1] as TopRateMoviesFragment
        }
        onView(withId(R.id.tabLayout)).perform(selectTabAtPosition(1))
        waitViewShown(withId(R.id.listTopRateMovie))
        onView(withId(R.id.listTopRateMovie)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(2, ViewActions.scrollTo())
        )
        onView(withId(R.id.listTopRateMovie)).perform(
            actionOnItemAtPosition<TopRateMoviesRecyclerViewAdapter.ViewHolder>(2,
                    ViewActions.click()
                )
        )

        val data  = frag?.adapter?.getItemByPosition(2)

        Mockito.verify(navController).navigate(
            MainFragmentDirections.actionMainFragmentToDetailMovieFragment(
                data!!
            ))
        var bundle = Bundle()
        bundle.putParcelable("movie", data)
        launchFragmentInHiltContainer<DetailMovieFragment>(fragmentFactory = fragmentFactory, fragmentArgs = bundle)
        waitViewShown(withId(R.id.childState))
    }

}
