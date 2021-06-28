package com.kutugondrong.omovie

import com.kutugondrong.omovie.screen.PopularMoviesFragmentTest
import com.kutugondrong.omovie.screen.TopRateMoviesFragmentTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    PopularMoviesFragmentTest::class,
    TopRateMoviesFragmentTest::class
)
class InstrumentationTestSuite