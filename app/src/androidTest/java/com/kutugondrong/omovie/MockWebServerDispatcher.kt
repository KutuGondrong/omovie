package com.kutugondrong.omovie

import com.kutugondrong.omovie.data.remote.MovieApi
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.InputStreamReader

class MockServerDispatcher {

    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return when (request.path) {
                "3/movie/popular?api_key=${MovieApi.API_KEY}" -> MockResponse().setResponseCode(200).setBody(getJsonContent("fake_movie.json"))
                "3/movie/top_rated?api_key=${MovieApi.API_KEY}" -> MockResponse().setResponseCode(200).setBody(getJsonContent("fake_movie.json"))
                else -> MockResponse().setResponseCode(400)
            }
        }
    }
    internal inner class ErrorDispatcher : Dispatcher() {

        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(400)
        }
    }

    private fun getJsonContent(fileName: String): String {
        return InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(fileName)).use { it.readText() }
    }
}