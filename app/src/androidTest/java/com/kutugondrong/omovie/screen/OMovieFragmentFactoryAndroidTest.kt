package com.kutugondrong.omovie.screen

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.room.Room
import com.kutugondrong.omovie.BuildConfig
import com.kutugondrong.omovie.data.local.FavoriteMovieDatabase
import com.kutugondrong.omovie.data.remote.MovieApi
import com.kutugondrong.omovie.screen.detail.DetailMovieFragment
import com.kutugondrong.omovie.screen.favorite.FavoriteMoviesFragment
import com.kutugondrong.omovie.screen.main.MainFragment
import com.kutugondrong.omovie.screen.popular.PopularMoviesFragment
import com.kutugondrong.omovie.screen.toprate.TopRateMoviesFragment
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
class OMovieFragmentFactoryAndroidTest @Inject constructor() : FragmentFactory() {

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

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBaseUrl() = BuildConfig.SERVER_BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideMovieApi(retrofit: Retrofit): MovieApi =
        retrofit.create(MovieApi::class.java)

    @Singleton
    @Provides
    fun provideFavoriteMovieDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        FavoriteMovieDatabase::class.java,
        "movie_db"
    ).build()

    @Singleton
    @Provides
    fun provideFavoriteMovieDao(db: FavoriteMovieDatabase) = db.getFavoriteMovieDao()

}