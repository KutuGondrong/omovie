package com.kutugondrong.omovie.screen

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.kutugondrong.omovie.*
import com.kutugondrong.omovie.helper.waitViewShown
import com.kutugondrong.omovie.screen.detail.DetailMovieFragment
import com.kutugondrong.omovie.screen.main.MainFragment
import com.kutugondrong.omovie.screen.main.MainFragmentDirections
import com.kutugondrong.omovie.screen.popular.PopularMoviesFragment
import com.kutugondrong.omovie.screen.popular.PopularMoviesRecyclerViewAdapter
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
class PopularMoviesFragmentTest{

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
    fun testVisibilityPopularMoviesFragment(){
        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = fragmentFactory)
        waitViewShown(withId(R.id.listPopularMovie))
        onView(withId(R.id.listPopularMovie)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testScrollListPopularMovie(){
        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = fragmentFactory)
        waitViewShown(withId(R.id.listPopularMovie))
        for (i in 1..5) {
            onView(withId(R.id.listPopularMovie)).perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(i*19, ViewActions.scrollTo())
            )
        }
    }

    @Test
    fun testOpenPopularMovie_thenNNavigateToDetailMovieFragment(){
        val navController = Mockito.mock(NavController::class.java)
        var frag: PopularMoviesFragment? = null
        launchFragmentInHiltContainer<MainFragment>(fragmentFactory = fragmentFactory) {
            Navigation.setViewNavController(requireView(), navController)
            frag = this.fragmentList[0] as PopularMoviesFragment
        }
        waitViewShown(withId(R.id.listPopularMovie))
        for (i in 1..8) {
            onView(withId(R.id.listPopularMovie)).perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(i*19, ViewActions.scrollTo())
            )
        }
        onView(withId(R.id.listPopularMovie)).perform(
            actionOnItemAtPosition<PopularMoviesRecyclerViewAdapter.ViewHolder>(8*19-1,
                    ViewActions.click()
                )
        )

        val data  = frag?.adapter?.getItemByPosition(8*19-1)

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


