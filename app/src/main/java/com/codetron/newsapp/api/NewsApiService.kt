package com.codetron.newsapp.api

import com.codetron.newsapp.model.ArticleData
import com.codetron.newsapp.model.SourcesData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines?country=us&apiKey=${API_KEY}")
    suspend fun getTopHeadlines(): Response<ArticleData>

    @GET("top-headlines?apiKey=${API_KEY}")
    suspend fun getTopHeadlinesWithSource(
        @Query("sources") id: String
    ): Response<ArticleData>

    @GET("top-headlines/sources?country=us&apiKey=${API_KEY}")
    suspend fun getSources(): Response<SourcesData>

    companion object {
        private const val BASE_URL = "https://newsapi.org/v2/"
        // TODO: add API KEY
        private const val API_KEY = ""

        private var INSTANCE: NewsApiService? = null

        fun instance(): NewsApiService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: RetrofitInstance
                    .get(BASE_URL)
                    .create(NewsApiService::class.java).also {
                        INSTANCE = it
                    }
            }
        }
    }
}